package org.mesonet.dataprocessing.site

import io.reactivex.Observable
import org.mesonet.dataprocessing.SelectSiteListener
import org.mesonet.dataprocessing.filterlist.FilterListDataProvider



interface MesonetSiteDataController: FilterListDataProvider, SelectSiteListener
{
    fun GetCurrentSelectionSubject(): Observable<String>
    fun ToggleFavorite(inStid: String): Observable<Boolean>
    fun CurrentStationName(): String
    fun CurrentIsFavorite(): Boolean
    fun CurrentStationElevation(): Number?
    fun GetMeteogramUrl(): String
}