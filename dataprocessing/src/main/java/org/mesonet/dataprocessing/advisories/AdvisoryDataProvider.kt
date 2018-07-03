package org.mesonet.dataprocessing.advisories

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.mesonet.models.advisories.Advisory
import org.mesonet.network.DataDownloader

import javax.inject.Inject

class AdvisoryDataProvider @Inject constructor(internal var mDataDownloader: DataDownloader): Observable<MutableList<Advisory>>()
{
    override fun subscribeActual(observer: Observer<in MutableList<Advisory>>?) {
//        mDataDownloader.GetAdvisoriesList().observeOn(Schedulers.computation()).subscribe (object: Observer<MutableList<Advisory>>{
//            override fun onComplete() {
//            }
//
//            override fun onSubscribe(d: Disposable) {
//            }
//
//            override fun onNext(t: MutableList<Advisory>) {
//                observer?.onNext(t)
//            }
//
//            override fun onError(e: Throwable) {
//                e.printStackTrace()
//            }
//        })
    }
}
