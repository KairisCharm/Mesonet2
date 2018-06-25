package org.mesonet.dataprocessing.site.forecast

import android.content.Context
import android.os.Build
import io.reactivex.Observable
import io.reactivex.Observer
import org.mesonet.dataprocessing.R
import org.mesonet.models.site.forecast.Forecast



class ForecastLayoutController constructor(private var mContext: Context, private var mForecastDataObservable: Observable<ForecastData>): Observable<ForecastLayoutController.ForecastDisplayData>()
{
    override fun subscribeActual(observer: Observer<in ForecastDisplayData>?) {
        if(observer != null) {
            mForecastDataObservable.map {
                val forecastData = ForecastDisplayDataImpl()
                forecastData.mImageUrl = it.GetIconUrl()

                var colorResource = R.color.colorPrimary

                if (!it.GetHighOrLow().isEmpty()) {
                    val highOrLow = Forecast.HighOrLow.valueOf(it.GetHighOrLow())

                    colorResource = when (highOrLow) {
                        Forecast.HighOrLow.High -> R.color.forecastDayBackground
                        Forecast.HighOrLow.Low -> R.color.forecastNightBackground
                    }
                }

                forecastData.mBackgroundColor =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                            mContext.resources.getColor(colorResource, mContext.theme)
                        else
                            mContext.resources.getColor(colorResource)

                forecastData
            }.subscribe(observer)
        }
    }



    private class ForecastDisplayDataImpl: ForecastDisplayData
    {
        internal var mForecastData: ForecastData? = null
        internal var mBackgroundColor: Int = 0
        internal var mImageUrl: String = ""

        override fun GetBackgroundColor(): Int {
            return mBackgroundColor
        }


        override fun GetData(): ForecastData?
        {
            return mForecastData
        }


        override fun GetImageUrl(): String
        {
            return mImageUrl
        }
    }



    interface ForecastDisplayData
    {
        fun GetBackgroundColor(): Int
        fun GetData(): ForecastData?
        fun GetImageUrl(): String
    }
}