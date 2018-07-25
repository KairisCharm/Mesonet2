package org.mesonet.app.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.squareup.picasso.Picasso
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import org.mesonet.app.R
import org.mesonet.dataprocessing.site.forecast.FiveDayForecastDataController
import org.mesonet.dataprocessing.site.forecast.ForecastData
import org.mesonet.dataprocessing.site.forecast.SemiDayForecastDataController
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
            mFiveDayForecastDataController.GetForecast(0).GetForecastDataSubject().observeOn(AndroidSchedulers.mainThread()).subscribe(object: Observer<ForecastData>{
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: ForecastData) {
                    val iconUrl1 = t.GetIconUrl()
                    val highOrLow = t.GetHighOrLowTemp().split(" ")
                    inRemoteViews.setTextViewText(R.id.widget_forecast_condition1, t.GetStatus())
                    inRemoteViews.setTextViewText(R.id.widget_forecast_loworhightemp1, highOrLow.first())
                    inRemoteViews.setTextViewText(R.id.widget_forecast_loworhigh1, highOrLow.last())
                    inRemoteViews.setTextViewText(R.id.widget_forecast_time1, t.GetTime())
                    if (!iconUrl1.isEmpty())
                        Picasso.with(mContext).load(iconUrl1).into(inRemoteViews, R.id.widget_forecast_image1, inAppWidgetIds)
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    onNext(object: ForecastData{
                        override fun IsLoading(): Boolean {
                            return false
                        }

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

            mFiveDayForecastDataController.GetForecast(1).GetForecastDataSubject().observeOn(AndroidSchedulers.mainThread()).subscribe(object: Observer<ForecastData>{
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: ForecastData) {
                    val iconUrl2 = t.GetIconUrl()
                    val highOrLow = t.GetHighOrLowTemp().split(" ")
                    inRemoteViews.setTextViewText(R.id.widget_forecast_condition2, t.GetStatus())
                    inRemoteViews.setTextViewText(R.id.widget_forecast_loworhightemp2, highOrLow.first())
                    inRemoteViews.setTextViewText(R.id.widget_forecast_loworhigh2, highOrLow.last())
                    inRemoteViews.setTextViewText(R.id.widget_forecast_time2, t.GetTime())
                    if (!iconUrl2.isEmpty())
                        Picasso.with(mContext).load(iconUrl2).into(inRemoteViews, R.id.widget_forecast_image2, inAppWidgetIds)
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    onNext(object: ForecastData{
                        override fun IsLoading(): Boolean {
                            return false
                        }

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


    override fun Update(inContext: Context, inAppWidgetManager: AppWidgetManager, inRemoteViews: RemoteViews, inAppWidgetIds: IntArray) {
        super.Update(inContext, inAppWidgetManager, inRemoteViews, inAppWidgetIds)

        mFiveDayForecastDataController.GetForecastDataSubject().observeOn(AndroidSchedulers.mainThread()).subscribe (object: Observer<List<SemiDayForecastDataController>>
        {
            override fun onComplete() {}
            override fun onSubscribe(d: Disposable) {}

            override fun onNext(t: List<SemiDayForecastDataController>) {
                SetForecastViews(inRemoteViews, inAppWidgetIds)

                UpdateWidgets(inContext, inAppWidgetManager, inRemoteViews, inAppWidgetIds)
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                onNext(ArrayList())
            }

        })
    }


    override fun GetClickLayoutId(): Int {
        return R.id.widget_large_layout
    }


    override fun GetWidgetClickIntent(inContext: Context): Intent {
        return Intent(inContext, WidgetProviderLarge::class.java)
    }


    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        mFiveDayForecastDataController.Dispose()
        super.onDeleted(context, appWidgetIds)
    }
}