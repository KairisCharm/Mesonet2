package org.mesonet.dataprocessing.site.forecast

import android.content.Context
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

import org.mesonet.dataprocessing.formulas.UnitConverter
import org.mesonet.dataprocessing.site.MesonetSiteDataController
import org.mesonet.dataprocessing.userdata.Preferences
import org.mesonet.models.site.forecast.Forecast
import org.mesonet.network.DataDownloader
import java.util.concurrent.TimeUnit

import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

@Singleton
class FiveDayForecastDataControllerImpl @Inject constructor(inMesonetSiteDataController: MesonetSiteDataController,
                                                            private var mPreferences: Preferences,
                                                            private var mUnitConverter: UnitConverter,
                                                            private var mContext: Context,
                                                            private var mDataDownloader: DataDownloader) : FiveDayForecastDataController, Observer<String> {



    private var mCurrentSite: String? = null

    private var mDataSubject: BehaviorSubject<List<SemiDayForecastDataController>> = BehaviorSubject.create()

    private var mDisposable: Disposable? = null


    init {
        inMesonetSiteDataController.GetCurrentSelectionSubject().observeOn(Schedulers.computation()).subscribe(this)
    }


    override fun GetForecastDataSubject(): BehaviorSubject<List<SemiDayForecastDataController>>
    {
        return mDataSubject
    }


    override fun onComplete() {

    }

    override fun onSubscribe(d: Disposable) {

    }

    override fun onNext(t: String) {
        if(mCurrentSite == null || !mCurrentSite.equals(t))
        {
            mDataSubject.onNext(ArrayList())

            Observable.interval(0, 1, TimeUnit.MINUTES).observeOn(Schedulers.computation()).subscribe(object: Observer<Long>{
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {
                    mDisposable = d
                }

                override fun onNext(time: Long) {
                    if(mCurrentSite != t || mDataSubject.hasObservers()) {
                        mCurrentSite = t
                        mDataDownloader.GetForecast(mCurrentSite?: "").observeOn(Schedulers.computation()).subscribe(object: Observer<List<Forecast>>
                        {
                            override fun onComplete() {

                            }

                            override fun onSubscribe(d: Disposable) {

                            }

                            override fun onNext(t: List<Forecast>) {
                                SetData(t)
                            }

                            override fun onError(e: Throwable) {
                                e.printStackTrace()
                                onNext(ArrayList())
                            }

                        })
                    }
                    else
                    {
                        mDisposable?.dispose()
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

    }



    internal fun SetData(inForecast: List<Forecast>?) {
        if (inForecast != null) {

            if(!mDataSubject.hasValue() || mDataSubject.value.isEmpty()) {
                val result = ArrayList<SemiDayForecastDataController>()
                for (i in 0..9) {
                    if(i < inForecast.size)
                        result.add(SemiDayForecastDataController(mContext, mPreferences, mUnitConverter, inForecast[i], mDataDownloader))
                    else
                        result.add(SemiDayForecastDataController(mContext, mPreferences, mUnitConverter, null, mDataDownloader))
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
}