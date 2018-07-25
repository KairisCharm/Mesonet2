package org.mesonet.dataprocessing.site.forecast

import android.content.Context
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.mesonet.core.PerActivity

import org.mesonet.dataprocessing.formulas.UnitConverter
import org.mesonet.dataprocessing.site.MesonetSiteDataController
import org.mesonet.dataprocessing.userdata.Preferences
import org.mesonet.models.site.forecast.Forecast
import org.mesonet.network.DataProvider
import java.util.concurrent.TimeUnit

import javax.inject.Inject
import kotlin.collections.ArrayList

@PerActivity
class FiveDayForecastDataControllerImpl @Inject constructor(private var mMesonetSiteDataController: MesonetSiteDataController,
                                                            private var mPreferences: Preferences,
                                                            private var mUnitConverter: UnitConverter,
                                                            private var mDataProvider: DataProvider) : FiveDayForecastDataController, Observer<String> {
    private var mCurrentSite: String? = null
    private var mContext: Context? = null


    private var mDataSubject: BehaviorSubject<List<SemiDayForecastDataController>> = BehaviorSubject.create()

    private var mUpdateDisposable: Disposable? = null
    private var mCurrentSiteDisposable: Disposable? = null
    private var mInit = false

    internal fun Init(inContext: Context) {
        mInit = true
        mContext = inContext
        mMesonetSiteDataController.GetCurrentSelectionSubject(inContext).observeOn(Schedulers.computation()).subscribe(this)
    }


    override fun GetForecastDataSubject(inContext: Context): BehaviorSubject<List<SemiDayForecastDataController>>
    {
        if(!mInit)
            Init(inContext)

        return mDataSubject
    }


    override fun onComplete() {}
    override fun onSubscribe(d: Disposable) {
        mCurrentSiteDisposable = d
    }

    override fun onNext(t: String)
    {
        if (mCurrentSite == null || !mCurrentSite.equals(t)) {
            if (mDataSubject.hasValue()) {
                for (forecastController in mDataSubject.value) {
                    forecastController.StartReloading()
                }
            }

            Observable.interval(0, 1, TimeUnit.MINUTES).subscribeOn(Schedulers.computation()).subscribe(object : Observer<Long> {
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable)
                {
                    mUpdateDisposable = d
                }

                override fun onNext(time: Long)
                {
                    if (mCurrentSite != t || mDataSubject.hasObservers()) {
                        mCurrentSite = t
                        mDataProvider.GetForecast(mCurrentSite
                                ?: "").observeOn(Schedulers.computation()).subscribe(object : Observer<List<Forecast>> {
                            override fun onComplete() {}
                            override fun onSubscribe(d: Disposable) {}
                            override fun onNext(t: List<Forecast>) {
                                SetData(t)
                            }

                            override fun onError(e: Throwable) {
                                e.printStackTrace()
                                onNext(ArrayList())
                            }
                        })
                    } else {
                        mUpdateDisposable?.dispose()
                    }
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    onNext(0)
                }

            })
        }
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
    }

    internal fun SetData(inForecast: List<Forecast>?) {
        if (inForecast != null) {
            if(!mDataSubject.hasValue() || mDataSubject.value.isEmpty()) {
                val result = ArrayList<SemiDayForecastDataController>()

                val myContext = mContext
                if(myContext != null) {
                    for (i in 0..9) {
                        if (i < inForecast.size)
                            result.add(SemiDayForecastDataController(myContext, mPreferences, mUnitConverter, inForecast[i], mDataProvider))
                        else
                            result.add(SemiDayForecastDataController(myContext, mPreferences, mUnitConverter, null, mDataProvider))
                    }
                }

                mDataSubject.onNext(result)
            }
            else
            {
                for(i in 0..9)
                {
                    if(i < inForecast.size)
                        mDataSubject.value[i].SetData(inForecast[i])
                    else
                        mDataSubject.value[i].SetData(null)
                }
            }
        }
    }



    override fun GetCount(): Int {
        return mDataSubject.value.size
    }


    override fun GetForecast(inIndex: Int): SemiDayForecastDataController {
        return mDataSubject.value[inIndex]
    }


    override fun Dispose() {
        if(mDataSubject.hasValue())
        {
            for(forecastController in mDataSubject.value)
            {
                forecastController.Dispose()
            }
        }
        mCurrentSiteDisposable?.dispose()
        mUpdateDisposable?.dispose()
        mDataSubject.onComplete()
    }
}