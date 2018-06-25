package org.mesonet.dataprocessing.maps

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.schedulers.Schedulers
import org.mesonet.models.maps.MapsList
import org.mesonet.network.DataDownloader
import java.util.*
import javax.inject.Inject


class MapsDataProvider @Inject constructor(internal var mDataDownloader: DataDownloader): Observable<List<MapsDataProvider.MapDataDisplayGroup>>()
{
    enum class MapViewHolderTypes {
        kHeader, kProduct, kGroup
    }


    private var mLastUpdate = 0L


    @Inject
    internal lateinit var mMapsListParser: MapsListParser

    private var mMapsList: MapsList? = null


    init {
        subscribeOn(Schedulers.computation())
    }


    override fun subscribeActual(inObserver: Observer<in List<MapDataDisplayGroup>>) {
        if(mMapsList != null)
            inObserver.onNext(LoadMapsList(mMapsList))

        if((mLastUpdate - Date().time) > 300000) {
            mDataDownloader.GetMaps().observeOn(Schedulers.computation()).map {
                mMapsList = it
                LoadMapsList(mMapsList)
            }.subscribe(inObserver)
        }
    }


    internal fun LoadMapsList(inMapsList: MapsList?): List<MapDataDisplayGroup>
    {
        val allGroups = inMapsList?.GetMain()
        val allSections = inMapsList?.GetSections()
        val allProducts = inMapsList?.GetProducts()

        var groupsWithSectionsWithGoodProducts = ArrayList<MapDataDisplayGroupImpl>()
        var sectionsWithGoodProducts = HashMap<String, MapDataDisplayGroupImpl.MapGroupSectionImpl>()
        var goodProducts = HashMap<String, MapDataDisplayGroupImpl.MapGroupSectionImpl.MapsProductImpl>()

        val orphanedGoodProducts = ArrayList<String>()
        val orphanedSectionsWithGoodProducts = ArrayList<String>()

        if(allProducts != null)
        {
            goodProducts = HashMap(allProducts.filter { !it.value.GetUrl().isNullOrBlank() }.mapValues {
                MapDataDisplayGroupImpl.MapGroupSectionImpl.MapsProductImpl(it.value.GetTitle(), it.value.GetUrl())
            })

            orphanedGoodProducts.addAll(goodProducts.keys)
        }

        if(goodProducts.isNotEmpty())
        {
            if(allSections != null) {
                sectionsWithGoodProducts = HashMap(allSections.filter { it.value.GetProducts().intersect(goodProducts.keys).isNotEmpty()}
                                                              .mapValues {
                                                                            MapDataDisplayGroupImpl.MapGroupSectionImpl(it.value.GetTitle(), HashMap(it.value.GetProducts().filter { goodProducts.keys.contains(it) }
                                                                                                                                                                            .associate { it to goodProducts[it]!!}))
                                                              })

                sectionsWithGoodProducts.forEach{ orphanedGoodProducts.removeAll(it.value.GetProducts().keys) }
            }
        }

        if(sectionsWithGoodProducts.isNotEmpty())
        {
            if(allGroups != null)
            {
                groupsWithSectionsWithGoodProducts = ArrayList(allGroups.filter { it.GetSections().intersect(sectionsWithGoodProducts.keys).isNotEmpty() }.map {
                    MapDataDisplayGroupImpl(it.GetTitle(), HashMap(it.GetSections().filter{sectionsWithGoodProducts.keys.contains(it)}.associate { it to sectionsWithGoodProducts[it]!! }))
                })

                groupsWithSectionsWithGoodProducts.forEach{ orphanedSectionsWithGoodProducts.removeAll(it.GetSections().keys)}
            }
        }

        if(orphanedGoodProducts.isNotEmpty())
        {
            val uncategorizedKey = "uncategorized"
            sectionsWithGoodProducts[uncategorizedKey] = MapDataDisplayGroupImpl.MapGroupSectionImpl("", HashMap(orphanedGoodProducts.associate { it to goodProducts[it]!! }))
            orphanedSectionsWithGoodProducts.add(uncategorizedKey)
        }

        if(orphanedSectionsWithGoodProducts.isNotEmpty())
        {
            groupsWithSectionsWithGoodProducts.add(MapDataDisplayGroupImpl("Uncategorized", HashMap(orphanedSectionsWithGoodProducts.associate { it to sectionsWithGoodProducts[it]!! })))
        }

        return groupsWithSectionsWithGoodProducts
    }



    class MapDataDisplayGroupImpl(var mTitle: String? = null,
                                  var mSections: HashMap<String, MapDataDisplayGroup.MapGroupSection> = HashMap()): MapDataDisplayGroup
    {
        override fun GetTitle(): String?
        {
            return mTitle
        }

        override fun GetSections(): HashMap<String, MapDataDisplayGroup.MapGroupSection>
        {
            return mSections
        }



        class MapGroupSectionImpl(var mTitle: String? = null, var mProducts: HashMap<String, MapDataDisplayGroup.MapGroupSection.MapsProduct> = HashMap()): MapDataDisplayGroup.MapGroupSection
        {
            override fun GetTitle(): String?
            {
                return mTitle
            }

            override fun GetProducts(): HashMap<String, MapDataDisplayGroup.MapGroupSection.MapsProduct>
            {
                return mProducts
            }



            class MapsProductImpl(var mTitle: String? = null, var mImageUrl: String? = null): MapDataDisplayGroup.MapGroupSection.MapsProduct
            {
                override fun GetTitle(): String? {
                    return mTitle
                }

                override fun GetImageUrl(): String? {
                    return mImageUrl
                }
            }
        }
    }


    interface MapDataDisplayGroup
    {
        fun GetTitle(): String?
        fun GetSections(): HashMap<String, MapGroupSection>


        interface MapGroupSection
        {
            fun GetTitle(): String?
            fun GetProducts(): HashMap<String, MapsProduct>


            interface MapsProduct
            {
                fun GetTitle(): String?
                fun GetImageUrl(): String?
            }
        }
    }
}
