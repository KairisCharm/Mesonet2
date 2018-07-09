package org.mesonet.app.site.forecast

import android.app.Activity
import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import org.mesonet.app.R
import org.mesonet.app.databinding.ForecastFragmentBinding
import org.mesonet.dataprocessing.site.forecast.SemiDayForecastDataController

class ForecastListView(inActivity: Activity) : LinearLayout(inActivity) {
	private var mBinding: ForecastFragmentBinding? = null

	init {
		mBinding = DataBindingUtil.inflate(LayoutInflater.from(inActivity), R.layout.forecast_fragment, this, true)

		val totalForecastsCount = resources.getInteger(R.integer.forecastsPerPage)

		for (i in 0 until totalForecastsCount) {
			val forecastLayout = ForecastLayout(inActivity)
			mBinding!!.layout.addView(forecastLayout)
			forecastLayout.layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f)
		}
	}


	internal fun SetSemiDayForecast(inForecastIndex: Int, inController: SemiDayForecastDataController) {
		if (mBinding != null) {
			if (inForecastIndex < mBinding!!.layout.childCount) {
				inController.GetForecastDataSubject().observeOn(AndroidSchedulers.mainThread()).subscribe {
					(mBinding!!.layout.getChildAt(inForecastIndex) as ForecastLayout).SetData(it)
				}

			}
		}
	}
}