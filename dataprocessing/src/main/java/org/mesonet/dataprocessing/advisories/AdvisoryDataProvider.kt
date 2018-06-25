package org.mesonet.dataprocessing.advisories

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.schedulers.Schedulers
import org.mesonet.models.advisories.Advisory
import org.mesonet.network.DataDownloader

import javax.inject.Inject

class AdvisoryDataProvider @Inject constructor(internal var mDataDownloader: DataDownloader): Observable<List<Advisory>>()
{
    override fun subscribeActual(observer: Observer<in List<Advisory>>?) {
        mDataDownloader.GetAdvisoriesList().observeOn(Schedulers.computation()).subscribe {
            observer?.onNext(it)
        }
    }
}
