package org.mesonet.dataprocessing.advisories

import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.mesonet.models.advisories.Advisory
import org.mesonet.network.DataDownloader

import javax.inject.Inject

class AdvisoryDataProvider @Inject constructor(internal var mDataDownloader: DataDownloader)
{
    val mCurrentList: MutableList<Advisory>? = null
    val mDataObservable = Observable.create(ObservableOnSubscribe<MutableList<Advisory>> {

        if(mCurrentList != null)
            it.onNext(mCurrentList)

        mDataDownloader.GetAdvisoriesList().observeOn(Schedulers.computation()).subscribe (object: Observer<MutableList<Advisory>>{
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: MutableList<Advisory>) {
                it.onNext(t)
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }
        })
    }).subscribeOn(Schedulers.computation())



    fun GetDataObservable(): Observable<MutableList<Advisory>>
    {
        return mDataObservable
    }
}
