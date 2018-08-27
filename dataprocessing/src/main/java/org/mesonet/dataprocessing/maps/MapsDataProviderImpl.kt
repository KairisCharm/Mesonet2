package org.mesonet.dataprocessing.maps

import android.content.Context
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.mesonet.core.PerContext
import org.mesonet.dataprocessing.ConnectivityStatusProvider
import org.mesonet.dataprocessing.PageStateInfo
import org.mesonet.dataprocessing.maps.MapsDataProvider.Companion.kAbbreviatedDisplayLimit
import org.mesonet.dataprocessing.maps.MapsDataProvider.Companion.kGenericSectionHeaderText
import org.mesonet.models.maps.MapsList
import org.mesonet.network.DataProvider
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet


@PerContext
class MapsDataProviderImpl @Inject constructor(internal var mDataProvider: DataProvider,
                                               internal var mConnectivityStatusProvider: ConnectivityStatusProvider): MapsDataProvider
{
    private var mLastUpdate = 0L

    private var mMapsSubject: BehaviorSubject<MutableList<MapsDataProvider.MapAbbreviatedGroupDisplayData>> = BehaviorSubject.create()
    private var mPageStateSubject: BehaviorSubject<PageStateInfo> = BehaviorSubject.create()

    private val mConnectivityObservable = mConnectivityStatusProvider.ConnectivityStatusObservable()

    override fun OnCreate(inContext: Context) {}

    override fun OnResume(inContext: Context) {

        if(!mMapsSubject.hasValue() || mLastUpdate == 0L || (mLastUpdate - Date().time) > 600000) {
            mConnectivityObservable.observeOn(Schedulers.computation()).subscribe(object : Observer<Boolean> {
                var disposable: Disposable? = null
                override fun onComplete() {}

                override fun onSubscribe(d: Disposable) {
                    disposable = d
                }

                override fun onNext(t: Boolean) {
                    if(mMapsSubject.hasValue())
                    {
                        mPageStateSubject.onNext(object: PageStateInfo{
                            override fun GetPageState(): PageStateInfo.PageState {
                                return PageStateInfo.PageState.kData
                            }

                            override fun GetErrorMessage(): String? {
                                return ""
                            }

                        })
                    }
                    else if(t)
                    {
                        mPageStateSubject.onNext(object: PageStateInfo{
                            override fun GetPageState(): PageStateInfo.PageState {
                                return PageStateInfo.PageState.kLoading
                            }

                            override fun GetErrorMessage(): String? {
                                return ""
                            }

                        })
                    }
                    else
                    {
                        mPageStateSubject.onNext(object: PageStateInfo{
                            override fun GetPageState(): PageStateInfo.PageState {
                                return PageStateInfo.PageState.kError
                            }

                            override fun GetErrorMessage(): String? {
                                return "No Connection"
                            }

                        })
                    }
                    disposable?.dispose()
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }
            })
        }

        if(mLastUpdate == 0L || (mLastUpdate - Date().time) > 600000)
        {
            mDataProvider.GetMaps().retryWhen { mConnectivityObservable }.observeOn(Schedulers.computation()).subscribe(object: Observer<MapsList>{
                var disposable: Disposable? = null
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable)
                {
                    disposable = d
                }

                override fun onNext(t: MapsList) {
                    mLastUpdate = Date().time
                    mMapsSubject.onNext(LoadMapsList(t))

                    mPageStateSubject.onNext(object: PageStateInfo{
                        override fun GetPageState(): PageStateInfo.PageState {
                            return PageStateInfo.PageState.kData
                        }

                        override fun GetErrorMessage(): String? {
                            return ""
                        }

                    })

                    disposable?.dispose()
                    disposable = null
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }
            })
        }
    }

    override fun OnPause() {}
    override fun OnDestroy() {
        mMapsSubject.onComplete()
    }



    override fun GetMapsListObservable(): Observable<MutableList<MapsDataProvider.MapAbbreviatedGroupDisplayData>>
    {
        return mMapsSubject
    }


    override fun GetPageStateObservable(): Observable<PageStateInfo>
    {
        return mPageStateSubject
    }


    internal fun LoadMapsList(inMapsList: MapsList?): MutableList<MapsDataProvider.MapAbbreviatedGroupDisplayData>
    {
        val allGroups = inMapsList?.GetMain()
        val allSections = inMapsList?.GetSections()
        val allProducts = inMapsList?.GetProducts()
        val abbreviatedGroups = ArrayList<MapsDataProvider.MapAbbreviatedGroupDisplayData>()

        val fullGroupList = ArrayList<MapFullGroupDisplayDataImpl>()

        var previousGroupIndex: Int? = null
        var previousSectionKey: String? = null

        for(key in allProducts?.keys?: HashSet())
        {
            if(!allProducts?.get(key)?.GetUrl().isNullOrBlank()) {
                val newProduct = MapFullGroupDisplayDataImpl.MapGroupSectionImpl.MapsProductImpl()

                newProduct.mTitle = allProducts?.get(key)?.GetTitle()?.replace("&deg;", "°") ?: ""
                newProduct.mImageUrl = mDataProvider.GetMapImageUrl(allProducts?.get(key)?.GetUrl()?: "")
                newProduct.mSectionTitle = ""

                var sectionKey = allSections?.keys?.first { allSections[it]?.GetProducts()?.contains(key)?: false }
                val groupIndex = allGroups?.indices?.first { allGroups[it].GetSections().contains(sectionKey) }

                if(sectionKey != previousSectionKey)
                {
                    if(groupIndex != previousGroupIndex)
                    {
                        fullGroupList.add(MapFullGroupDisplayDataImpl())

                        if(groupIndex == null)
                            fullGroupList.last().mTitle = ""
                        else
                            fullGroupList.last().mTitle = allGroups[groupIndex].GetTitle()

                        previousGroupIndex = groupIndex
                    }

                    if(sectionKey == null)
                    {
                        var uncategorizedIndex = 0
                        while(fullGroupList.last().mSections.containsKey("uncategorized$uncategorizedIndex"))
                            uncategorizedIndex++

                        sectionKey = "uncategorized$uncategorizedIndex"

                    }

                    fullGroupList.last().mSections[sectionKey] = MapFullGroupDisplayDataImpl.MapGroupSectionImpl()
                    (fullGroupList.last().mSections[sectionKey] as MapFullGroupDisplayDataImpl.MapGroupSectionImpl).mTitle = allSections?.get(sectionKey)?.GetTitle()?: ""

                    previousSectionKey = sectionKey
                }

                (fullGroupList.last().mSections[sectionKey] as MapFullGroupDisplayDataImpl.MapGroupSectionImpl).mProducts[key] = newProduct
            }
        }

        for(i in fullGroupList.indices)
        {
            val productList = ArrayList<MapFullGroupDisplayDataImpl.MapGroupSectionImpl.MapsProductImpl>()

            var totalProducts = 0

            fullGroupList[i].mSections.values.forEach{ totalProducts += it.GetProducts().size }

            val sectionIterator = fullGroupList[i].mSections.keys.iterator()
            var sectionKey = sectionIterator.next()
            var productIterator = (fullGroupList[i].mSections[sectionKey] as MapFullGroupDisplayDataImpl.MapGroupSectionImpl).mProducts.keys.iterator()
            var productKey = productIterator.next()

            while(productList.size < totalProducts && productList.size < kAbbreviatedDisplayLimit)
            {
                val fullProduct = (fullGroupList[i].mSections[sectionKey] as MapFullGroupDisplayDataImpl.MapGroupSectionImpl).mProducts[productKey]
                productList.add(MapFullGroupDisplayDataImpl.MapGroupSectionImpl.MapsProductImpl(fullProduct?.GetTitle(), fullGroupList[i].mSections[sectionKey]?.GetTitle()?: "", fullProduct?.GetImageUrl()))

                if(productIterator.hasNext())
                    productKey = productIterator.next()

                else if(sectionIterator.hasNext())
                {
                    sectionKey = sectionIterator.next()

                    productIterator = (fullGroupList[i].mSections[sectionKey] as MapFullGroupDisplayDataImpl.MapGroupSectionImpl).mProducts.keys.iterator()
                    productKey = productIterator.next()
                }
            }

            abbreviatedGroups.add(MapAbbreviatedGroupDisplayDataImpl(fullGroupList[i].mTitle, totalProducts, productList as MutableList<MapsDataProvider.MapFullGroupDisplayData.MapGroupSection.MapsProduct>, fullGroupList[i]))
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


    private class MapFullGroupDisplayDataImpl(var mTitle: String? = null,
                                              val mSections: LinkedHashMap<String, MapsDataProvider.MapFullGroupDisplayData.MapGroupSection> = LinkedHashMap()): MapsDataProvider.MapFullGroupDisplayData
    {
        override fun GetTitle(): String? {
            return mTitle
        }

        override fun GetSections(): HashMap<String, MapsDataProvider.MapFullGroupDisplayData.MapGroupSection> {
            return mSections
        }
        class MapGroupSectionImpl(var mTitle: String? = null,
                                  var mTitleAsSubtext: String? = null,
                                  var mProducts: LinkedHashMap<String, MapsDataProvider.MapFullGroupDisplayData.MapGroupSection.MapsProduct> = LinkedHashMap()): MapsDataProvider.MapFullGroupDisplayData.MapGroupSection
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
