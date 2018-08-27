package org.mesonet.dataprocessing.filterlist

import android.app.Activity
import android.content.Context
import io.reactivex.Observable
import io.reactivex.Single
import org.mesonet.dataprocessing.BasicListData

interface FilterListController
{
    fun SortByNearest(inSortText: String?, inListData: Map<String, BasicListData>?): Single<MutableList<Pair<String, BasicListData>>>
    fun SortByName(inSortText: String?, inListData: Map<String, BasicListData>?): Single<MutableList<Pair<String, BasicListData>>>
    fun TryGetLocationAndFillListObservable(inSearchText: String?, inListData: Map<String, BasicListData>?): Single<MutableList<Pair<String, BasicListData>>>
}