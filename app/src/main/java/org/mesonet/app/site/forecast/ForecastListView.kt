package org.mesonet.app.site.forecast

import android.app.Activity
import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import org.mesonet.app.R
import org.mesonet.app.databinding.ForecastListViewBinding
import org.mesonet.dataprocessing.site.forecast.ForecastData
import org.mesonet.dataprocessing.site.forecast.SemiDayForecastDataController

class ForecastListView(inActivity: Activity) : LinearLayout(inActivity) {
	private var mBinding: ForecastListViewBinding? = null

	init {
		mBinding = DataBindingUtil.inflate(LayoutInflater.from(inActivity), R.layout.forecast_list_view, this, true)

		val totalForecastsCount = resources.getInteger(R.integer.forecastsPerPage)

		for (i in 0 until totalForecastsCount) {
			val forecastLayout = ForecastLayout(inActivity)
			mBinding?.layout?.addView(forecastLayout)
			forecastLayout.layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f)
		}
	}


	internal fun SetSemiDayForecast(inForecastIndex: Int, inController: SemiDayForecastDataController) {
		if (mBinding != null) {
			if (inForecastIndex < mBinding?.layout?.childCount?: 0) {
				inController.GetForecastDataSubject().observeOn(AndroidSchedulers.mainThread()).subscribe(object: Observer<ForecastData>
				{
					override fun onComplete() {

					}

					override fun onSubscribe(d: Disposable) {

					}

					override fun onNext(t: ForecastData) {
						(mBinding?.layout?.getChildAt(inForecastIndex) as ForecastLayout).SetData(t)
					}

					override fun onError(e: Throwable) {
						e.printStackTrace()
						onNext(object: ForecastData{
							override fun GetTime(): String {
								return ""
							}

							override fun GetIconUrl(): String {
								return ""
							}

							override fun GetStatus(): String {
								return ""
							}

							override fun GetHighOrLowTemp(): String {
								return ""
							}

							override fun GetWindDescription(): String {
								return ""
							}

							override fun compareTo(other: ForecastData): Int {
								return 1
							}

						})
					}
				})
			}
		}
	}
}