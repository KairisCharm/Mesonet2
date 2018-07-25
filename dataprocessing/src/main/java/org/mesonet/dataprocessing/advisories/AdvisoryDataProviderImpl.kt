package org.mesonet.dataprocessing.advisories

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.mesonet.models.advisories.Advisory
import org.mesonet.network.DataProvider
import java.util.concurrent.TimeUnit

import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AdvisoryDataProviderImpl @Inject constructor(internal var mDataProvider: DataProvider): AdvisoryDataProvider
{
    private var mAdvisoryDisposable: Disposable? = null

    private var mAdvisoryDoOnSubscribe: Observable<Advisory.AdvisoryList>
    val mDataSubject: BehaviorSubject<Advisory.AdvisoryList> = BehaviorSubject.create()

    init{
        mAdvisoryDoOnSubscribe = mDataSubject.doOnSubscribe{
            if(mAdvisoryDisposable == null || mAdvisoryDisposable?.isDisposed != true) {
                Observable.interval(0, 1, TimeUnit.MINUTES).subscribeOn(Schedulers.computation()).subscribe(object: Observer<Long>
                {
                    override fun onComplete() {}

                    override fun onSubscribe(d: Disposable) {
                        mAdvisoryDisposable = d
                    }

                    override fun onNext(t: Long) {
                        if (mDataSubject.hasObservers()) {
                            mDataProvider.GetAdvisoriesList().observeOn(Schedulers.computation()). subscribe(object : Observer<Advisory.AdvisoryList> {
                                override fun onComplete() {}
                                override fun onSubscribe(d: Disposable) {}
                                override fun onNext(t: Advisory.AdvisoryList) {
                                    if (!mDataSubject.hasValue() || compareValues(mDataSubject.value, t) != 0)
                                        mDataSubject.onNext(t)
                                }

                                override fun onError(e: Throwable) {
                                    e.printStackTrace()
                                }
                            })
                        } else {
                            mAdvisoryDisposable?.dispose()
                        }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                })
            }
        }
    }



    override fun GetDataObservable(): Observable<Advisory.AdvisoryList>
    {
        return mAdvisoryDoOnSubscribe
    }



    override fun Dispose() {
        mAdvisoryDisposable?.dispose()
        mDataSubject.onComplete()
    }
}
