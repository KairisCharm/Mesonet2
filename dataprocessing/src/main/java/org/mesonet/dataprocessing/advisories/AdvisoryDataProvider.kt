package org.mesonet.dataprocessing.advisories

import io.reactivex.Observable
import org.mesonet.models.advisories.Advisory

interface AdvisoryDataProvider
{
    fun GetDataObservable(): Observable<Advisory.AdvisoryList>
    fun Dispose()
}