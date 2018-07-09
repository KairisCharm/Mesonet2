package org.mesonet.app.site.forecast

import android.app.Activity
import android.databinding.DataBindingUtil
import android.os.Build
import android.text.Layout.BREAK_STRATEGY_HIGH_QUALITY
import android.text.Layout.BREAK_STRATEGY_SIMPLE
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.squareup.picasso.Picasso
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

import org.mesonet.app.R
import org.mesonet.app.databinding.ForecastLayoutBinding
import org.mesonet.dataprocessing.site.forecast.ForecastData
import org.mesonet.dataprocessing.site.forecast.ForecastLayoutController



class ForecastLayout(inActivity: Activity) : RelativeLayout(inActivity), Observer<ForecastLayoutController.ForecastDisplayData>
{
    internal val mBinding: ForecastLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.forecast_layout, this, true)
    private val mActivity = inActivity
    private var mForecastImageUrl: String? = null



    internal fun SetData(inForecastData: ForecastData)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mBinding.statusTextView.breakStrategy = BREAK_STRATEGY_SIMPLE
        }

        ForecastLayoutController(context, inForecastData).GetForecastObservable().observeOn(AndroidSchedulers.mainThread())?.subscribe(this)
    }


    override fun onComplete() {}
    override fun onSubscribe(d: Disposable) {}
    override fun onError(e: Throwable) {}

    override fun onNext(t: ForecastLayoutController.ForecastDisplayData) {
        mBinding.forecastDisplayData = t
        mBinding.invalidateAll()

        val imageUrl = t.GetImageUrl()

        if(!imageUrl.isEmpty() && imageUrl != mForecastImageUrl) {
            mForecastImageUrl = imageUrl
            Picasso.with(mActivity).load(imageUrl).into(mBinding.image)
        }
    }
}