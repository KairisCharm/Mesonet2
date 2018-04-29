package org.mesonet.dataprocessing.filterlist

import android.location.Location
import org.mesonet.core.PerActivity
import org.mesonet.core.ThreadHandler
import org.mesonet.dataprocessing.BasicListData
import org.mesonet.dataprocessing.LocationProvider
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@PerActivity
class FilterListController @Inject constructor()
{
    private var mSortByNearest = false


    @Inject
    internal lateinit var mDeviceLocation: LocationProvider

    @Inject
    internal lateinit var mThreadHandler: ThreadHandler


    private fun SortList(inCurrentValue: String?, inSearchFields: Map<String, BasicListData>, inLocation: Location?): Map<String, BasicListData> {
        var sortData = MapToPairs(inSearchFields)
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

                if(inCurrentValue != null) {
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



    fun FillList(inSearchText: String?, inListData: Map<String, BasicListData>?, inListener: ListFilterListener) {
        mThreadHandler.Run("FilterListData", Runnable {
            if (mSortByNearest) {
                mDeviceLocation.GetLocation(object : LocationProvider.LocationListener {
                    override fun LastLocationFound(inLocation: Location?) {
                        FillList(inSearchText, inListData, inLocation, inListener)
                    }

                    override fun LocationUnavailable() {
                        FillList(inSearchText, inListData, null, inListener)
                    }
                })
            } else {
                FillList(inSearchText,inListData,null, inListener)
            }
        })
    }



    private fun FillList(inSearchText: String?, inListData: Map<String, BasicListData>?, inLocation: Location?, inListener: ListFilterListener) {

        inListener.ListFiltered(MapToPairs(SortList(inSearchText, inListData!!, inLocation)))
    }



    fun SortByNearest(inSortText: String?, inListData: Map<String, BasicListData>?, inListener: ListFilterListener)
    {
        mThreadHandler.Run("FilterListData", Runnable {
            mSortByNearest = true
            FillList(inSortText, inListData, inListener)
        })
    }



    internal fun MapToPairs(inMap : Map<String, BasicListData>) : MutableList<Pair<String, BasicListData>>
    {
        return inMap.toList() as MutableList<Pair<String, BasicListData>>
    }


    internal fun PairsToMap(inPairs: MutableList<Pair<String, BasicListData>>) : Map<String, BasicListData>
    {
        return inPairs.toMap()
    }


    interface ListFilterListener
    {
        fun ListFiltered(inFilteredResults: MutableList<Pair<String, BasicListData>>)
    }
}