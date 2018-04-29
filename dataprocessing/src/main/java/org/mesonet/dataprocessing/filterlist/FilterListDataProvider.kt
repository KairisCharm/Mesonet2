package org.mesonet.dataprocessing.filterlist

import org.mesonet.dataprocessing.BasicListData
import java.util.*

interface FilterListDataProvider {
    fun AllViewHolderData(inListener: FilterListDataListener)
    fun CurrentSelection(): String
    fun GetDataObservable(): Observable

    interface FilterListDataListener
    {
        fun ListDataBuilt(inListData: Map<String, BasicListData>?)
    }
}