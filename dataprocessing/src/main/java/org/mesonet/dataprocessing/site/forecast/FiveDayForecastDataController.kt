package org.mesonet.dataprocessing.site.forecast

import android.content.Context
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

import org.mesonet.dataprocessing.formulas.UnitConverter
import org.mesonet.dataprocessing.site.MesonetSiteDataController
import org.mesonet.dataprocessing.userdata.Preferences
import org.mesonet.models.site.forecast.Forecast
import org.mesonet.network.DataDownloader

import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

@Singleton
class FiveDayForecastDataController @Inject constructor(private var mMesonetSiteDataController: MesonetSiteDataController,
                                                        private var mPreferences: Preferences,
                                                        private var mUnitConverter: UnitConverter,
                                                        private var mContext: Context,
                                                        private var mDataDownloader: DataDownloader) : Observer<String> {


    private var mSemiDayForecasts: MutableList<SemiDayForecastDataController> = ArrayList()

    private var mCurrentSite: String? = null

    private var mDataObservable = Observable.create(ObservableOnSubscribe<List<SemiDayForecastDataController>>{observer ->
        mDataDownloader.GetForecast(mMesonetSiteDataController.CurrentSelection()).observeOn(Schedulers.computation()).subscribe(object: Observer<List<Forecast>>
        {
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: List<Forecast>) {
                SetData(t)

                observer.onNext(mSemiDayForecasts)
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

        })
    }).subscribeOn(Schedulers.computation())

    init {
        mMesonetSiteDataController.GetCurrentSelectionObservable().observeOn(Schedulers.computation()).subscribe(this)
    }


    fun GetForecastDataObservable(): Observable<List<SemiDayForecastDataController>>
    {
        return mDataObservable
    }


    override fun onComplete() {

    }

    override fun onSubscribe(d: Disposable) {

    }

    override fun onNext(t: String) {
        if(mCurrentSite == null || !mCurrentSite.equals(mMesonetSiteDataController.CurrentSelection()))
        {
            mCurrentSite = mMesonetSiteDataController.CurrentSelection()
            mSemiDayForecasts = ArrayList()
            mDataObservable.subscribe()
        }
    }

    override fun onError(e: Throwable) {

    }



    internal fun SetData(inForecast: List<Forecast>?) {
        if (inForecast != null) {

            for (i in inForecast.indices) {
                if(mSemiDayForecasts.size <= i)
                    mSemiDayForecasts.add(SemiDayForecastDataController(mContext, mPreferences, mUnitConverter, inForecast[i], mDataDownloader))
                else
                    mSemiDayForecasts[i].SetData(inForecast[i])
            }
        }
    }


    fun GetCount(): Int {
        return mSemiDayForecasts.size
    }


    fun GetForecast(inIndex: Int): SemiDayForecastDataController {
        return mSemiDayForecasts[inIndex]
    }
}