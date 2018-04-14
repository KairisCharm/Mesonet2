package org.mesonet.app.forecast

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.LinearLayoutCompat
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import org.mesonet.app.R
import org.mesonet.app.databinding.ForecastFragmentBinding


class ForecastListView(context: Context) : LinearLayout(context) {
    internal var mBinding: ForecastFragmentBinding? = null

    init {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.forecast_fragment, this, true)

        val totalForecastsCount = resources.getInteger(R.integer.forecastsPerPage)

        for (i in 0 until totalForecastsCount) {
            val forecastLayout = ForecastLayout(getContext())
            mBinding!!.layout.addView(forecastLayout)
            forecastLayout.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f)
        }
    }


    fun SetSemiDayForecast(inForecastIndex: Int, inController: SemiDayForecastDataController) {
        if (mBinding != null) {
            if (inForecastIndex < mBinding!!.layout.childCount) {
                (mBinding!!.layout.getChildAt(inForecastIndex) as ForecastLayout).SetData(inController)
            }
        }
    }
}
