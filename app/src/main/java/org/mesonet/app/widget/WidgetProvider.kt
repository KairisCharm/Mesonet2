package org.mesonet.app.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import org.mesonet.app.MainActivity
import org.mesonet.app.R
import org.mesonet.core.PerActivity
import org.mesonet.dataprocessing.site.MesonetSiteDataController
import org.mesonet.dataprocessing.site.mesonetdata.MesonetUIController
import javax.inject.Inject


@PerActivity
open class WidgetProvider @Inject constructor() : AppWidgetProvider() {
    @Inject
    lateinit var mMesonetSiteDataController: MesonetSiteDataController

    @Inject
    lateinit var mMesonetUIController: MesonetUIController

    override fun onReceive(inContext: Context, inIntent: Intent) {
        AndroidInjection.inject(this, inContext)

        super.onReceive(inContext, inIntent)
    }


    override fun onUpdate(inContext: Context, inAppWidgetManager: AppWidgetManager, inAppWidgetIds: IntArray) {
        super.onUpdate(inContext, inAppWidgetManager, inAppWidgetIds)

        val remoteViews = RemoteViews(inContext.packageName, GetLayoutId())

        Update(inContext, inAppWidgetManager, remoteViews, inAppWidgetIds)
    }


    internal open fun Update(inContext: Context, inAppWidgetManager: AppWidgetManager, inRemoteViews: RemoteViews, inAppWidgetIds: IntArray) {
        mMesonetUIController.GetDisplayFieldsObservable().observeOn(AndroidSchedulers.mainThread()).subscribe {
            SetMesonetViews(inRemoteViews, inAppWidgetIds)

            UpdateWidgets(inContext, inAppWidgetManager, inRemoteViews, inAppWidgetIds)
        }
    }


    internal fun UpdateWidgets(inContext: Context, inAppWidgetManager: AppWidgetManager, inRemoteViews: RemoteViews, inAppWidgetIds: IntArray) {
        for (widgetId in inAppWidgetIds) {
            val mainActIntent = Intent(inContext, MainActivity::class.java)
            val mainActPendingIntent = PendingIntent.getActivity(inContext, 0, mainActIntent, 0)

            val widgetProvIntent = GetWidgetClickIntent(inContext)
            widgetProvIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
            widgetProvIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, inAppWidgetIds)
            val widgetProvPendingIntent = PendingIntent.getBroadcast(inContext, 0, widgetProvIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            inRemoteViews.setOnClickPendingIntent(GetClickLayoutId(), mainActPendingIntent)

            inRemoteViews.setOnClickPendingIntent(R.id.widget_update, widgetProvPendingIntent)

            inAppWidgetManager.updateAppWidget(widgetId, inRemoteViews)
        }
    }


    internal open fun GetLayoutId(): Int {
        return R.layout.widget_small
    }


    internal fun SetMesonetViews(inRemoteViews: RemoteViews, inAppWidgetIds: IntArray) {
        mMesonetUIController.GetDisplayFieldsObservable().observeOn(AndroidSchedulers.mainThread()).subscribe {
            inRemoteViews.setTextViewText(R.id.widget_place, mMesonetSiteDataController.CurrentStationName())
            inRemoteViews.setTextViewText(R.id.widget_tair, it.GetAirTempString())

            inRemoteViews.setTextViewText(R.id.widget_feelslike, it.GetApparentTempString())
            inRemoteViews.setTextViewText(R.id.widget_wind, it.GetWindString())
            inRemoteViews.setTextViewText(R.id.widget_time, it.GetTimeString())
        }
    }


    internal open fun GetClickLayoutId(): Int {
        return R.id.widget_small_layout
    }


    internal open fun GetWidgetClickIntent(inContext: Context): Intent {
        return Intent(inContext, WidgetProvider::class.java)
    }
}