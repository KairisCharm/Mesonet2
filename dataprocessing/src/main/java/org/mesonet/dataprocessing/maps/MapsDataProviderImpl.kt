package org.mesonet.dataprocessing.maps

import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.mesonet.dataprocessing.maps.MapsDataProvider.Companion.kAbbreviatedDisplayLimit
import org.mesonet.dataprocessing.maps.MapsDataProvider.Companion.kGenericSectionHeaderText
import org.mesonet.models.maps.MapsList
import org.mesonet.network.DataProvider
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MapsDataProviderImpl @Inject constructor(internal var mDataProvider: DataProvider): MapsDataProvider
{
    private var mLastUpdate = 0L


    private var mMapsList: MapsList? = null


    var mMapsListObservable = Observable.create(ObservableOnSubscribe<MutableList<MapsDataProvider.MapAbbreviatedGroupDisplayData>> {subscriber ->
        if(mMapsList != null) {
            subscriber.onNext(LoadMapsList(mMapsList))
            subscriber.onComplete()
        }

        if(mLastUpdate == 0L || (mLastUpdate - Date().time) > 300000) {
            (mDataProvider.GetMaps().observeOn(Schedulers.computation()).map {
                mLastUpdate = Date().time
                mMapsList = it
                val result = LoadMapsList(mMapsList)
                subscriber.onNext(result)
                subscriber.onComplete()
                result
            }).subscribe(object: Observer<MutableList<MapsDataProvider.MapAbbreviatedGroupDisplayData>> {
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: MutableList<MapsDataProvider.MapAbbreviatedGroupDisplayData>) {}
                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }
            })
        }
    }).subscribeOn(Schedulers.computation())


    override fun GetMapsListObservable(): Observable<MutableList<MapsDataProvider.MapAbbreviatedGroupDisplayData>>
    {
        return mMapsListObservable
    }


    internal fun LoadMapsList(inMapsList: MapsList?): MutableList<MapsDataProvider.MapAbbreviatedGroupDisplayData>
    {
        val uncategorizedKey = "uncategorized"

        val allGroups = inMapsList?.GetMain()
        val allSections = inMapsList?.GetSections()
        val allProducts = inMapsList?.GetProducts()

        var groupsWithSectionsWithGoodProducts = ArrayList<MapFullGroupDisplayDataImpl>()
        val sectionsWithGoodProducts = LinkedHashMap<String, MapFullGroupDisplayDataImpl.MapGroupSectionImpl>()
        var goodProducts = LinkedHashMap<String, MapFullGroupDisplayDataImpl.MapGroupSectionImpl.MapsProductImpl>()

        val abbreviatedGroups = ArrayList<MapsDataProvider.MapAbbreviatedGroupDisplayData>()

        val orphanedGoodProducts = ArrayList<String>()
        val orphanedSectionsWithGoodProducts = ArrayList<String>()

        if(allProducts != null)
        {
            goodProducts = LinkedHashMap(allProducts.filter { !it.value.GetUrl().isNullOrBlank() }.mapValues {
                MapFullGroupDisplayDataImpl.MapGroupSectionImpl.MapsProductImpl(it.value.GetTitle(), null, mDataProvider.GetMapImageUrl((it.value.GetUrl()?: "")))
            })

            orphanedGoodProducts.addAll(goodProducts.keys)
        }

        if(goodProducts.isNotEmpty())
        {
            if(allSections != null) {
                sectionsWithGoodProducts.putAll( LinkedHashMap(allSections.filter { it.value.GetProducts().intersect(goodProducts.keys).isNotEmpty()}
                                                              .mapValues {
                                                                  var title = it.value.GetTitle()
                                                                  var titleAsSubtext = it.value.GetTitle()
                                                                  if(title == null || title.isBlank())
                                                                  {
                                                                      title = kGenericSectionHeaderText
                                                                      titleAsSubtext = ""
                                                                  }
                                                                  MapFullGroupDisplayDataImpl.MapGroupSectionImpl(title, titleAsSubtext, HashMap(it.value.GetProducts().filter { goodProducts.keys.contains(it) }
                                                                                                                                                                            .associate { it to goodProducts[it]!!}))
                                                              }))

                sectionsWithGoodProducts.forEach{ orphanedGoodProducts.removeAll(it.value.GetProducts().keys) }
            }
        }

        if(sectionsWithGoodProducts.isNotEmpty())
        {
            if(allGroups != null)
            {
                groupsWithSectionsWithGoodProducts = ArrayList(allGroups.filter { it.GetSections().intersect(sectionsWithGoodProducts.keys).isNotEmpty() }.map {
                    MapFullGroupDisplayDataImpl(it.GetTitle(), HashMap(it.GetSections().filter{sectionsWithGoodProducts.keys.contains(it)}.associate { it to sectionsWithGoodProducts[it]!! }))
                })

                groupsWithSectionsWithGoodProducts.forEach{ orphanedSectionsWithGoodProducts.removeAll(it.GetSections().keys)}
            }
        }

        if(orphanedGoodProducts.isNotEmpty())
        {
            sectionsWithGoodProducts[uncategorizedKey] = MapFullGroupDisplayDataImpl.MapGroupSectionImpl("Other", "", HashMap(orphanedGoodProducts.associate { it to goodProducts[it]!! }))
            orphanedSectionsWithGoodProducts.add(uncategorizedKey)
        }

        if(orphanedSectionsWithGoodProducts.isNotEmpty())
        {
            groupsWithSectionsWithGoodProducts.add(MapFullGroupDisplayDataImpl("Uncategorized", HashMap(orphanedSectionsWithGoodProducts.associate { it to sectionsWithGoodProducts[it]!! })))
        }


        for(group in groupsWithSectionsWithGoodProducts)
        {
            var totalProducts = 0
            val groupProducts = ArrayList<MapsDataProvider.MapFullGroupDisplayData.MapGroupSection.MapsProduct>()

            for(section in group.GetSections())
            {
                totalProducts += section.value.GetProducts().size
                for(product in section.value.GetProducts()) {
                    if (groupProducts.size >= kAbbreviatedDisplayLimit) {
                        break
                    }

                    groupProducts.add(MapFullGroupDisplayDataImpl.MapGroupSectionImpl.MapsProductImpl(product.value.GetTitle(), section.value.GetTitleAsSubtext(), product.value.GetImageUrl()))
                }
            }

            if(totalProducts > kAbbreviatedDisplayLimit)
                groupProducts.remove(groupProducts.last())

            abbreviatedGroups.add(MapAbbreviatedGroupDisplayDataImpl(group.GetTitle(), totalProducts, groupProducts, group))
        }

        return abbreviatedGroups
    }



    class MapAbbreviatedGroupDisplayDataImpl(var mTitle: String? = null,
                                             var mFullListSize: Int,
                                             var mProducts: MutableList<MapsDataProvider.MapFullGroupDisplayData.MapGroupSection.MapsProduct> = ArrayList(),
                                             var mMapFullGroupDisplayData: MapsDataProvider.MapFullGroupDisplayData): MapsDataProvider.MapAbbreviatedGroupDisplayData
    {
        override fun GetFullListSize(): Int {
            return mFullListSize
        }

        override fun GetTitle(): String?
        {
            return mTitle
        }


        override fun GetProducts(): MutableList<MapsDataProvider.MapFullGroupDisplayData.MapGroupSection.MapsProduct>
        {
            return mProducts
        }


        override fun GetMapFullGroupDisplayData(): MapsDataProvider.MapFullGroupDisplayData {
            return mMapFullGroupDisplayData
        }


        override fun GetGroupDisplayLimit(): Int {
            return kAbbreviatedDisplayLimit
        }
    }


    private class MapFullGroupDisplayDataImpl(val mTitle: String? = null,
                                              val mSections: HashMap<String, MapsDataProvider.MapFullGroupDisplayData.MapGroupSection> = HashMap()): MapsDataProvider.MapFullGroupDisplayData
    {
        override fun GetTitle(): String? {
            return mTitle
        }

        override fun GetSections(): HashMap<String, MapsDataProvider.MapFullGroupDisplayData.MapGroupSection> {
            return mSections
        }
        class MapGroupSectionImpl(var mTitle: String? = null,
                                  var mTitleAsSubtext: String? = null,
                                  var mProducts: HashMap<String, MapsDataProvider.MapFullGroupDisplayData.MapGroupSection.MapsProduct> = HashMap()): MapsDataProvider.MapFullGroupDisplayData.MapGroupSection
        {
            override fun GetTitle(): String?
            {
                return mTitle
            }

            override fun GetTitleAsSubtext(): String? {
                return mTitleAsSubtext
            }

            override fun GetProducts(): HashMap<String, MapsDataProvider.MapFullGroupDisplayData.MapGroupSection.MapsProduct>
            {
                return mProducts
            }


            class MapsProductImpl(var mTitle: String? = null, var mSectionTitle: String? = null, var mImageUrl: String? = null): MapsDataProvider.MapFullGroupDisplayData.MapGroupSection.MapsProduct
            {
                override fun GetTitle(): String? {
                    return mTitle
                }

                override fun GetSectionTitle(): String? {
                    return mSectionTitle
                }

                override fun GetImageUrl(): String? {
                    return mImageUrl
                }
            }
        }
    }
}
