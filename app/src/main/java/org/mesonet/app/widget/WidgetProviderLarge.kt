package org.mesonet.app.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import org.mesonet.app.R
import org.mesonet.dataprocessing.site.forecast.FiveDayForecastDataController
import javax.inject.Inject


class WidgetProviderLarge @Inject constructor() : WidgetProvider() {
    @Inject
    lateinit var mContext: Context


    @Inject
    lateinit var mFiveDayForecastDataController: FiveDayForecastDataController


    override fun GetLayoutId(): Int {
        return R.layout.widget_large
    }


    internal fun SetForecastViews(inRemoteViews: RemoteViews, inAppWidgetIds: IntArray) {
        if (mFiveDayForecastDataController.GetCount() > 1) {
            mFiveDayForecastDataController.GetForecast(0).GetForecastDataObservable().observeOn(AndroidSchedulers.mainThread()).subscribe {
                val iconUrl1 = it.GetIconUrl()
                inRemoteViews.setTextViewText(R.id.widget_forecast_condition1, it.GetStatus())
                inRemoteViews.setTextViewText(R.id.widget_forecast_loworhightemp1, it.GetTemp())
                inRemoteViews.setTextViewText(R.id.widget_forecast_loworhigh1, it.GetHighOrLow())
                inRemoteViews.setTextViewText(R.id.widget_forecast_time1, it.GetTime())
                if (!iconUrl1.isEmpty())
                    Picasso.with(mContext).load(iconUrl1).into(inRemoteViews, R.id.widget_forecast_image1, inAppWidgetIds)
            }

            mFiveDayForecastDataController.GetForecast(1).GetForecastDataObservable().observeOn(AndroidSchedulers.mainThread()).subscribe {
                val iconUrl2 = it.GetIconUrl()
                inRemoteViews.setTextViewText(R.id.widget_forecast_condition2, it.GetStatus())
                inRemoteViews.setTextViewText(R.id.widget_forecast_loworhightemp2, it.GetTemp())
                inRemoteViews.setTextViewText(R.id.widget_forecast_loworhigh2, it.GetHighOrLow())
                inRemoteViews.setTextViewText(R.id.widget_forecast_time2, it.GetTime())
                if (!iconUrl2.isEmpty())
                    Picasso.with(mContext).load(iconUrl2).into(inRemoteViews, R.id.widget_forecast_image2, inAppWidgetIds)
            }
        }
    }


    override fun Update(inContext: Context, inAppWidgetManager: AppWidgetManager, inRemoteViews: RemoteViews, inAppWidgetIds: IntArray) {
        super.Update(inContext, inAppWidgetManager, inRemoteViews, inAppWidgetIds)

        mFiveDayForecastDataController.GetForecastDataObservable().observeOn(AndroidSchedulers.mainThread()).subscribe {
            SetForecastViews(inRemoteViews, inAppWidgetIds)

            UpdateWidgets(inContext, inAppWidgetManager, inRemoteViews, inAppWidgetIds)
        }
    }


    override fun GetClickLayoutId(): Int {
        return R.id.widget_large_layout
    }


    override fun GetWidgetClickIntent(inContext: Context): Intent {
        return Intent(inContext, WidgetProviderLarge::class.java)
    }
}