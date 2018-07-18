package org.mesonet.dataprocessing.filterlist

import android.content.Context
import io.reactivex.Observable
import org.mesonet.dataprocessing.BasicListData

interface FilterListController
{
    fun SortByNearest(inContext: Context?, inSortText: String?, inListData: Map<String, BasicListData>?): Observable<MutableList<Pair<String, BasicListData>>>
    fun TryGetLocationAndFillListObservable(inContext: Context?, inSearchText: String?, inListData: Map<String, BasicListData>?): Observable<MutableList<Pair<String, BasicListData>>>
}