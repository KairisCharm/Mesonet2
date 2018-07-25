package org.mesonet.dataprocessing.site

import android.content.Context
import io.reactivex.Observable
import org.mesonet.dataprocessing.SelectSiteListener
import org.mesonet.dataprocessing.filterlist.FilterListDataProvider



interface MesonetSiteDataController: FilterListDataProvider, SelectSiteListener
{
    fun GetCurrentSelectionSubject(inContext: Context): Observable<String>
    fun ToggleFavorite(inContext: Context, inStid: String): Observable<Boolean>
    fun CurrentStationName(): String
    fun CurrentIsFavorite(): Boolean
    fun CurrentStationElevation(): Number?
    fun GetMeteogramUrl(): String
    fun Dispose()
}