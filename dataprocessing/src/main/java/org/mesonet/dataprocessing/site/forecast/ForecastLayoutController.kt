package org.mesonet.dataprocessing.site.forecast

import android.content.Context
import android.os.Build
import org.mesonet.dataprocessing.R
import org.mesonet.core.ThreadHandler
import org.mesonet.models.site.forecast.Forecast
import java.util.*
import javax.inject.Inject

class ForecastLayoutController @Inject constructor(private var mContext: Context, private var mThreadHandler: ThreadHandler) : Observable(), Observer
{
    private var mForecastData: ForecastData? = null
    private var mBackgroundColor: Int = 0
    private var mImageUrl: String = ""



    fun GetBackgroundColor(): Int {
        return mBackgroundColor
    }


    fun GetData(): ForecastData?
    {
        return mForecastData
    }


    fun GetImageUrl(): String
    {
        return mImageUrl
    }


    fun SetData(inForecastData: ForecastData) {
        mThreadHandler.Run("ForecastData", Runnable {
            mForecastData = inForecastData
            inForecastData.GetObservable().addObserver(this)

            mImageUrl = inForecastData.GetIconUrl()

            var colorResource = R.color.colorPrimary

            if(!inForecastData.GetHighOrLow().isEmpty()) {
                val highOrLow = Forecast.HighOrLow.valueOf(inForecastData.GetHighOrLow())

                colorResource = when (highOrLow) {
                    Forecast.HighOrLow.High -> R.color.forecastDayBackground
                    Forecast.HighOrLow.Low -> R.color.forecastNightBackground
                }
            }

            mBackgroundColor =
                    if (Build.VERSION. SDK_INT >= Build.VERSION_CODES.M)
                        mContext.resources.getColor(colorResource, mContext.theme)
                    else
                        mContext.resources.getColor(colorResource)

            setChanged()
            notifyObservers()
        })
    }



    override fun update(observable: Observable, o: Any?) {
        mThreadHandler.Run("ForecastData", Runnable {
            if(observable is ForecastData)
                SetData(observable as ForecastData)

            setChanged()
            notifyObservers()
        })
    }
}