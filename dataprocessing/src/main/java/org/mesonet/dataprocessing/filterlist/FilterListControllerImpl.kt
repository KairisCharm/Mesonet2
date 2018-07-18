package org.mesonet.dataprocessing.filterlist

import android.content.Context
import android.location.Location
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.mesonet.core.PerActivity
import org.mesonet.dataprocessing.BasicListData
import org.mesonet.dataprocessing.LocationProvider
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

@PerActivity
class FilterListControllerImpl @Inject constructor(): FilterListController {
    private var mSortByNearest = false


    @Inject
    internal lateinit var mDeviceLocation: LocationProvider


    private fun SortList(inCurrentValue: String?, inSearchFields: Map<String, BasicListData>, inLocation: Location?): Map<String, BasicListData> {
        val sortData = MapToPairs(inSearchFields)
        Collections.sort<Pair<String, BasicListData>>(sortData, object : Comparator<Pair<String, BasicListData>> {
            override fun compare(stringPairPair: Pair<String, BasicListData>, t1: Pair<String, BasicListData>): Int {
                val name1 = stringPairPair.second.GetName()
                val name2 = t1.second.GetName()

                var result = 0

                if (inLocation != null) {
                    if (stringPairPair.second.GetLocation().distanceTo(inLocation) < t1.second.GetLocation().distanceTo(inLocation))
                        result = -1
                    else if (stringPairPair.second.GetLocation().distanceTo(inLocation) > t1.second.GetLocation().distanceTo(inLocation))
                        result = 1
                }


                if (result == 0) {
                    if (stringPairPair.second.IsFavorite() && !t1.second.IsFavorite())
                        result = -1
                    else if (!stringPairPair.second.IsFavorite() && t1.second.IsFavorite())
                        result = 1
                }

                if (inCurrentValue != null) {
                    if (result == 0)
                        result = HasSearchValue(inCurrentValue, name1, name2)

                    if (result == 0)
                        result = HasSearchValue(inCurrentValue, name2, name1)
                }

                if (result == 0)
                    result = name1.compareTo(name2)

                return result
            }


            private fun HasSearchValue(inSearchValue: String, inName1: String, inName2: String): Int {
                val lowerSearchValue = inSearchValue.toLowerCase()
                val lowerName1 = inName1.toLowerCase()
                val lowerName2 = inName2.toLowerCase()

                if (lowerName1 == lowerSearchValue && lowerName2 != lowerSearchValue)
                    return -1

                if (lowerName2 == lowerSearchValue && lowerName1 != lowerSearchValue)
                    return 1

                if (lowerName1.contains(lowerSearchValue) && !lowerName2.contains(lowerSearchValue))
                    return -1

                return if (lowerName2.contains(lowerSearchValue) && !lowerName1.contains(lowerSearchValue)) 1 else 0

            }
        })


        return PairsToMap(sortData)
    }


    override fun TryGetLocationAndFillListObservable(inContext: Context?, inSearchText: String?, inListData: Map<String, BasicListData>?): Observable<MutableList<Pair<String, BasicListData>>> {
        return Observable.create (ObservableOnSubscribe<MutableList<Pair<String, BasicListData>>>{observer ->
            if (mSortByNearest) {
                mDeviceLocation.GetLocation(inContext).observeOn(Schedulers.computation()).subscribe(object: Observer<LocationProvider.LocationResult>
                {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        onNext(object: LocationProvider.LocationResult{
                            override fun LocationResult(): Location? {
                                return null
                            }

                        })
                    }

                    override fun onNext(t: LocationProvider.LocationResult) {
                        observer.onNext(GenerateList(inSearchText, inListData, t.LocationResult()))
                    }
                })
            } else {
                observer.onNext(GenerateList(inSearchText, inListData, null))
            }
        }).subscribeOn(Schedulers.computation())
    }


    private fun GenerateList(inSearchText: String?, inListData: Map<String, BasicListData>?, inLocation: Location?): MutableList<Pair<String, BasicListData>> {
        return MapToPairs(SortList(inSearchText, inListData?: HashMap(), inLocation))
    }


    override fun SortByNearest(inContext: Context?, inSortText: String?, inListData: Map<String, BasicListData>?): Observable<MutableList<Pair<String, BasicListData>>> {
        mSortByNearest = true
        return TryGetLocationAndFillListObservable(inContext, inSortText, inListData)
    }


    internal fun MapToPairs(inMap: Map<String, BasicListData>): MutableList<Pair<String, BasicListData>> {
        return inMap.toList() as MutableList<Pair<String, BasicListData>>
    }


    internal fun PairsToMap(inPairs: MutableList<Pair<String, BasicListData>>): Map<String, BasicListData> {
        return inPairs.toMap()
    }
}