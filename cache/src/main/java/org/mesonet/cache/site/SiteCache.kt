package org.mesonet.cache.site

import android.content.Context
import io.reactivex.Observable
import org.mesonet.models.site.MesonetSiteList

interface SiteCache {
    fun GetSites(inContext: Context): Observable<MesonetSiteList>
    fun SaveSites(inContext: Context, inMesonetSites: MesonetSiteList): Observable<Void>
    fun GetFavorites(inContext: Context): Observable<MutableList<String>>
    fun SaveFavorites(inContext: Context, inFavorites: List<String>): Observable<Void>
}