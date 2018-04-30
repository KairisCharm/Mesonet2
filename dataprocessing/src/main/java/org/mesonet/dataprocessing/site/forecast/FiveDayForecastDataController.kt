package org.mesonet.dataprocessing.site.forecast


import android.app.Activity
import org.mesonet.core.PerActivity
import org.mesonet.core.ThreadHandler

import org.mesonet.dataprocessing.formulas.UnitConverter
import org.mesonet.dataprocessing.reflection.ForecastModelParser
import org.mesonet.dataprocessing.site.MesonetSiteDataController
import org.mesonet.dataprocessing.userdata.Preferences
import org.mesonet.network.DataDownloader
import java.util.*

import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

@PerActivity
class FiveDayForecastDataController @Inject constructor(internal var mMesonetSiteDataController: MesonetSiteDataController,
                                                        internal var mDataDownloader: DataDownloader,
                                                        internal var mThreadHandler: ThreadHandler,
                                                        internal var mModelParser: ForecastModelParser,
                                                        internal var mPreferences: Preferences,
                                                        internal var mUnitConverter: UnitConverter,
                                                        internal var mActivity: Activity) : Observable(), Observer {




    private var mSemiDayForecasts: MutableList<SemiDayForecastDataController> = ArrayList()

    private var mCurrentSite: String? = null

    private var mTaskId: UUID? = null

    init {
        mThreadHandler.Run("ForecastData", Runnable {
            mMesonetSiteDataController.addObserver(this)
            Update2()
        })
    }


    internal fun Update2() {
        if (!mMesonetSiteDataController.SiteDataFound()) {
            return
        }
        mTaskId = mDataDownloader.StartDownloads("http://www.mesonet.org/index.php/app/forecast/" + mMesonetSiteDataController.CurrentSelection(), object : DataDownloader.DownloadCallback {
            override fun DownloadComplete(inResponseCode: Int, inResult: String?) {
                mThreadHandler.Run("ForecastData", Runnable {
                    SetData(inResult)
                })
            }

            override fun DownloadFailed() {

            }
        }, 60000)
    }

    override fun update(o: Observable, arg: Any?) {
        mThreadHandler.Run("ForecastData", Runnable {
            if(mCurrentSite == null || !mCurrentSite.equals(mMesonetSiteDataController.CurrentSelection()))
            {
                mCurrentSite = mMesonetSiteDataController.CurrentSelection()
                mSemiDayForecasts = ArrayList()
                StopUpdates()
                StartUpdates()
                setChanged()
                notifyObservers()
            }
        })
    }


    internal fun SetData(inForecast: String?) {
        SetData(mModelParser.Parse(inForecast))
    }


    internal fun SetData(inForecast: List<ForecastModel>?) {
        if (inForecast != null) {

            for (i in inForecast.indices) {
                if(mSemiDayForecasts.size <= i)
                    mSemiDayForecasts.add(SemiDayForecastDataController(mActivity, mPreferences, mUnitConverter, inForecast[i], mThreadHandler))
                else
                    mSemiDayForecasts[i].SetData(inForecast[i])
            }
        }

        setChanged()
        notifyObservers()
    }


    fun GetCount(): Int {
        return mSemiDayForecasts.size
    }


    fun GetForecast(inIndex: Int): SemiDayForecastDataController {
        return mSemiDayForecasts[inIndex]
    }

    fun StartUpdates()
    {
        mThreadHandler.Run("ForecastData", Runnable {
            Update2()
        })
    }

    fun StopUpdates()
    {
        mThreadHandler.Run("ForecastData", Runnable {
            if(mTaskId != null)
                mDataDownloader.StopDownloads(mTaskId!!)
        })
    }


    fun GetObservable(): Observable {
        return this
    }

    override fun addObserver(o: Observer?) {
        super.addObserver(o)
        setChanged()
        notifyObservers()
    }
}
