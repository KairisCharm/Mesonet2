package org.mesonet.cache.site

import io.reactivex.Observable
import org.mesonet.models.site.MesonetSiteList

interface SiteCache {
    fun GetSites(): Observable<MesonetSiteList>
    fun SaveSites(inMesonetSites: MesonetSiteList): Observable<Void>
    fun GetFavorites(): Observable<MutableList<String>>
    fun SaveFavorites(inFavorites: List<String>): Observable<Void>
}