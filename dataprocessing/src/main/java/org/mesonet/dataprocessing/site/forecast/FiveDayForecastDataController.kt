package org.mesonet.dataprocessing.site.forecast

import android.content.Context
import io.reactivex.Observable
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
                                                        private var mDataDownloader: DataDownloader) : Observable<List<SemiDayForecastDataController>>(), Observer<String> {


    private var mSemiDayForecasts: MutableList<SemiDayForecastDataController> = ArrayList()

    private var mCurrentSite: String? = null

    init {
        mMesonetSiteDataController.observeOn(Schedulers.computation()).subscribe(this)
    }


    override fun subscribeActual(observer: Observer<in List<SemiDayForecastDataController>>?) {
        mDataDownloader.GetForecast(mMesonetSiteDataController.CurrentSelection()).observeOn(Schedulers.computation()).subscribe{
            SetData(it)

            observer?.onNext(mSemiDayForecasts)
        }
    }


    override fun onComplete() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSubscribe(d: Disposable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onNext(t: String) {
        if(mCurrentSite == null || !mCurrentSite.equals(mMesonetSiteDataController.CurrentSelection()))
        {
            mCurrentSite = mMesonetSiteDataController.CurrentSelection()
            mSemiDayForecasts = ArrayList()
            subscribe()
        }
    }

    override fun onError(e: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



    internal fun SetData(inForecast: List<Forecast>?) {
        if (inForecast != null) {

            for (i in inForecast.indices) {
                if(mSemiDayForecasts.size <= i)
                    mSemiDayForecasts.add(SemiDayForecastDataController(mContext, mPreferences, mUnitConverter, inForecast[i]))
                else
                    mSemiDayForecasts[i].SetData(inForecast[i])
            }
        }

        subscribe()
    }


    fun GetCount(): Int {
        return mSemiDayForecasts.size
    }


    fun GetForecast(inIndex: Int): SemiDayForecastDataController {
        return mSemiDayForecasts[inIndex]
    }
}