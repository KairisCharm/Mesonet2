package org.mesonet.app.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import dagger.android.AndroidInjection
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
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
        mMesonetUIController.GetDisplayFieldsSubject(inContext).observeOn(AndroidSchedulers.mainThread()).subscribe(object: Observer<MesonetUIController.MesonetDisplayFields>
        {
            var disposable: Disposable? = null
            override fun onComplete() {}
            override fun onSubscribe(d: Disposable)
            {
                disposable = d
            }

            override fun onNext(t: MesonetUIController.MesonetDisplayFields) {
                disposable?.dispose()
                SetMesonetViews(inContext, inAppWidgetManager, inRemoteViews, inAppWidgetIds)
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }
        })
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


    internal fun SetMesonetViews(inContext: Context, inAppWidgetManager: AppWidgetManager, inRemoteViews: RemoteViews, inAppWidgetIds: IntArray) {
        mMesonetSiteDataController.GetCurrentSelectionSubject(inContext).observeOn(AndroidSchedulers.mainThread()).subscribe(object: Observer<String>{
            var siteDisposable: Disposable? = null
            override fun onComplete() {}
            override fun onSubscribe(d: Disposable) {
                siteDisposable = d
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(t: String) {
                siteDisposable?.dispose()
                mMesonetUIController.GetDisplayFieldsSubject(inContext).observeOn(AndroidSchedulers.mainThread()).subscribe(object : Observer<MesonetUIController.MesonetDisplayFields> {
                    var uiDisposable: Disposable? = null
                    override fun onComplete() {}
                    override fun onSubscribe(d: Disposable)
                    {
                        uiDisposable = d
                    }

                    override fun onNext(t: MesonetUIController.MesonetDisplayFields) {
                        uiDisposable?.dispose()
                        inRemoteViews.setTextViewText(R.id.widget_place, t.GetStationName())
                        inRemoteViews.setTextViewText(R.id.widget_tair, t.GetAirTempString())

                        inRemoteViews.setTextViewText(R.id.widget_feelslike, t.GetApparentTempString())
                        inRemoteViews.setTextViewText(R.id.widget_wind, t.GetWindString())
                        inRemoteViews.setTextViewText(R.id.widget_time, t.GetTimeString())

                        UpdateWidgets(inContext, inAppWidgetManager, inRemoteViews, inAppWidgetIds)
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                })
            }
        })
    }


    internal open fun GetClickLayoutId(): Int {
        return R.id.widget_small_layout
    }


    internal open fun GetWidgetClickIntent(inContext: Context): Intent {
        return Intent(inContext, WidgetProvider::class.java)
    }


    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        mMesonetSiteDataController.Dispose()
        super.onDeleted(context, appWidgetIds)
    }
}