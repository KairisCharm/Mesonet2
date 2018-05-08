package org.mesonet.app.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.widget.RemoteViews
import com.squareup.picasso.Picasso
import org.mesonet.app.R
import org.mesonet.app.WidgetProvider
import org.mesonet.dataprocessing.site.MesonetSiteDataController
import org.mesonet.dataprocessing.site.forecast.FiveDayForecastDataController
import java.util.*
import javax.inject.Inject

class WidgetProviderLarge @Inject constructor() : WidgetProvider()
{
    @Inject
    lateinit var mContext: Context


    @Inject
    lateinit var mFiveDayForecastDataController: FiveDayForecastDataController



    override fun GetLayoutId() : Int
    {
        return R.layout.widget_large
    }


    internal fun SetForecastViews(inRemoteViews: RemoteViews, inAppWidgetIds: IntArray) {
        if(mFiveDayForecastDataController.GetCount() > 1) {
            val iconUrl1 = mFiveDayForecastDataController.GetForecast(0).GetIconUrl()
            val iconUrl2 = mFiveDayForecastDataController.GetForecast(1).GetIconUrl()

            inRemoteViews.setTextViewText(R.id.widget_forecast_condition1, mFiveDayForecastDataController.GetForecast(0).GetStatus())
            inRemoteViews.setTextViewText(R.id.widget_forecast_loworhightemp1, mFiveDayForecastDataController.GetForecast(0).GetTemp())
            inRemoteViews.setTextViewText(R.id.widget_forecast_loworhigh1, mFiveDayForecastDataController.GetForecast(0).GetHighOrLow())
            inRemoteViews.setTextViewText(R.id.widget_forecast_time1, mFiveDayForecastDataController.GetForecast(0).GetTime())
            if (!iconUrl1.isEmpty())
                Picasso.with(mContext).load(iconUrl1).into(inRemoteViews, R.id.widget_forecast_image1, inAppWidgetIds)
            inRemoteViews.setTextViewText(R.id.widget_forecast_condition2, mFiveDayForecastDataController.GetForecast(1).GetStatus())
            inRemoteViews.setTextViewText(R.id.widget_forecast_loworhightemp2, mFiveDayForecastDataController.GetForecast(1).GetTemp())
            inRemoteViews.setTextViewText(R.id.widget_forecast_loworhigh2, mFiveDayForecastDataController.GetForecast(1).GetHighOrLow())
            inRemoteViews.setTextViewText(R.id.widget_forecast_time2, mFiveDayForecastDataController.GetForecast(1).GetTime())
            if (!iconUrl2.isEmpty())
                Picasso.with(mContext).load(iconUrl2).into(inRemoteViews, R.id.widget_forecast_image2, inAppWidgetIds)
        }
    }


    override fun Update(inContext: Context, inAppWidgetManager: AppWidgetManager, inRemoteViews: RemoteViews, inAppWidgetIds: IntArray)
    {
        super.Update(inContext, inAppWidgetManager, inRemoteViews, inAppWidgetIds)

        mFiveDayForecastDataController.SingleUpdate(object: FiveDayForecastDataController.SingleUpdateListener{
            override fun UpdateComplete() {
                Handler(Looper.getMainLooper()).post({
                    SetForecastViews(inRemoteViews, inAppWidgetIds)

                    UpdateWidgets(inContext, inAppWidgetManager, inRemoteViews, inAppWidgetIds)
                })
            }

            override fun UpdateFailed() {
            }

        })
    }



    override fun GetClickLayoutId(): Int{
        return R.id.widget_large_layout
    }


    override fun GetWidgetClickIntent(inContext: Context): Intent
    {
        return Intent(inContext, WidgetProviderLarge::class.java)
    }
}