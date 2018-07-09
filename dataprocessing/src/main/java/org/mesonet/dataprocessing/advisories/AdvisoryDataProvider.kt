package org.mesonet.dataprocessing.advisories

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.mesonet.models.advisories.Advisory
import org.mesonet.network.DataDownloader
import java.util.concurrent.TimeUnit

import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AdvisoryDataProvider @Inject constructor(internal var mDataDownloader: DataDownloader)
{
    private var mAdvisoryDisposable: Disposable? = null

    val mDataSubject: BehaviorSubject<Advisory.AdvisoryList> = BehaviorSubject.create()



    init{
        mAdvisoryDisposable = Observable.interval(0, 1, TimeUnit.MINUTES).subscribeOn(Schedulers.computation()).subscribe {
            if(mDataSubject.hasObservers()) {
                mDataDownloader.GetAdvisoriesList().observeOn(Schedulers.computation()).subscribe {
                    if (!mDataSubject.hasValue() || compareValues(mDataSubject.value, it) != 0)
                        mDataSubject.onNext(it)
                }
            }
            else
            {
                mAdvisoryDisposable?.dispose()
            }
        }
    }



    fun GetDataSubject(): BehaviorSubject<Advisory.AdvisoryList>
    {
        return mDataSubject
    }
}
