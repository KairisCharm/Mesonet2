package org.mesonet.dataprocessing.radar

import io.reactivex.subjects.BehaviorSubject
import org.mesonet.dataprocessing.SelectSiteListener
import org.mesonet.dataprocessing.filterlist.FilterListDataProvider
import org.mesonet.models.radar.RadarDetails

interface RadarSiteDataProvider: FilterListDataProvider, SelectSiteListener
{
    fun GetSelectedSiteNameSubject(): BehaviorSubject<String>
    fun GetSelectedSiteDetailSubject(): BehaviorSubject<RadarDetails>
}