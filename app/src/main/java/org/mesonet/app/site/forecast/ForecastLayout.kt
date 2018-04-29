package org.mesonet.app.site.forecast

import android.app.Activity
import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.squareup.picasso.Picasso

import org.mesonet.app.R
import org.mesonet.app.databinding.ForecastLayoutBinding
import org.mesonet.dataprocessing.site.forecast.ForecastData
import org.mesonet.dataprocessing.site.forecast.ForecastLayoutController
import org.mesonet.core.ThreadHandler

import java.util.Observable
import java.util.Observer


class ForecastLayout(inActivity: Activity, private var mThreadHandler: ThreadHandler) : RelativeLayout(inActivity), Observer {
    internal val mBinding: ForecastLayoutBinding
    private val mActivity = inActivity
    private var mForecastImageUrl: String? = null


    init {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.forecast_layout, this, true)
        mBinding.forecastLayoutController = ForecastLayoutController(context, mThreadHandler)
        mBinding.forecastLayoutController?.addObserver(this)
    }



    internal fun SetData(inForecastData: ForecastData)
    {
        mBinding.forecastLayoutController?.SetData(inForecastData)
    }




    override fun update(observable: Observable, o: Any?) {
        mActivity.runOnUiThread({
            mBinding.invalidateAll()

            val imageUrl = mBinding.forecastLayoutController?.GetImageUrl()

            if(imageUrl != null && !imageUrl.isEmpty() && !imageUrl.equals(mForecastImageUrl)) {
                mForecastImageUrl = imageUrl
                Picasso.with(mActivity).load(imageUrl).into(mBinding.image)
            }
        })
    }
}