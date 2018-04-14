package org.mesonet.app.forecast

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import org.mesonet.app.R
import org.mesonet.app.baseclasses.BaseFragment
import org.mesonet.app.databinding.ForecastFragmentBinding


class ForecastFragment : BaseFragment() {
    internal var mBinding: ForecastFragmentBinding? = null

    override fun onCreateView(inInflater: LayoutInflater, inParent: ViewGroup?, inSavedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inInflater, R.layout.forecast_fragment, inParent, true)

        val forecastsPerPage = getResources().getInteger(R.integer.forecastsPerPage)

        for (i in 0 until forecastsPerPage) {
            val forecastLayout = ForecastLayout(getContext()!!)
            mBinding!!.layout.addView(forecastLayout)
        }

        return mBinding!!.root
    }


    fun SetSemiDayForecast(inForecastIndex: Int, inController: SemiDayForecastDataController) {
        if (mBinding != null) {
            if (inForecastIndex < mBinding!!.layout.childCount) {
                (mBinding!!.layout.getChildAt(inForecastIndex) as ForecastLayout).SetData(inController)
            }
        }
    }
}
