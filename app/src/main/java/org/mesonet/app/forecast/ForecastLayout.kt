package org.mesonet.app.forecast

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.OnRebindCallback
import android.databinding.ViewDataBinding
import android.os.Build
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.RelativeLayout

import com.squareup.picasso.Picasso

import org.mesonet.app.R
import org.mesonet.app.databinding.ForecastLayoutBinding

import java.util.Observable
import java.util.Observer


class ForecastLayout(context: Context) : RelativeLayout(context), Observer {
    internal var mBinding: ForecastLayoutBinding? = null


    init {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.forecast_layout, this, true)
        mBinding!!.forecastLayout = this
    }


    fun GetBackgroundColor(): Int {
        var result = R.color.colorPrimary
        if (mBinding != null) {
            val forecastData = mBinding!!.forecastData

            if (forecastData != null) {
                val highOrLow = ForecastModel.HighOrLow.valueOf(forecastData.GetHighOrLow())

                if (highOrLow != null) {
                    when (highOrLow) {
                        ForecastModel.HighOrLow.High -> result = R.color.forecastDayBackground
                        ForecastModel.HighOrLow.Low -> result = R.color.forecastNightBackground
                    }
                }
            }
        }

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) resources.getColor(result, context.theme) else resources.getColor(result)

    }


    fun SetData(inForecastData: ForecastData) {
        mBinding!!.forecastData = inForecastData
        inForecastData.GetObservable().addObserver(this)

        val url = inForecastData.GetIconUrl()

        if (url != null && !url.isEmpty())
            Picasso.with(context).load(inForecastData.GetIconUrl()).into(mBinding!!.image)
    }

    override fun update(observable: Observable, o: Any?) {
        mBinding!!.invalidateAll()
    }
}
