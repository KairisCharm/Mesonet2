package org.mesonet.app.site.forecast

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.mesonet.app.R
import org.mesonet.app.baseclasses.BaseFragment
import org.mesonet.app.databinding.ForecastFragmentBinding
import org.mesonet.dataprocessing.site.forecast.SemiDayForecastDataController
import org.mesonet.core.ThreadHandler
import javax.inject.Inject

class ForecastFragment : BaseFragment() {
    private var mBinding: ForecastFragmentBinding? = null

    @Inject
    internal lateinit var mThreadHandler: ThreadHandler

    override fun onCreateView(inInflater: LayoutInflater, inParent: ViewGroup?, inSavedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inInflater, R.layout.forecast_fragment, inParent, true)

        val forecastsPerPage = getResources().getInteger(R.integer.forecastsPerPage)

        for (i in 0 until forecastsPerPage) {
            val forecastLayout = ForecastLayout(activity!!, mThreadHandler)
            mBinding!!.layout.addView(forecastLayout)
        }

        return mBinding!!.root
    }


    internal fun SetSemiDayForecast(inForecastIndex: Int, inController: SemiDayForecastDataController) {
        if (mBinding != null) {
            if (inForecastIndex < mBinding!!.layout.childCount) {
                (mBinding!!.layout.getChildAt(inForecastIndex) as ForecastLayout).SetData(inController)
            }
        }
    }
}