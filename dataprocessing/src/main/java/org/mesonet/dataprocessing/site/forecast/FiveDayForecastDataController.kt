package org.mesonet.dataprocessing.site.forecast


import android.content.Context
import org.mesonet.core.ThreadHandler

import org.mesonet.dataprocessing.formulas.UnitConverter
import org.mesonet.dataprocessing.reflection.ForecastModelParser
import org.mesonet.dataprocessing.site.MesonetSiteDataController
import org.mesonet.dataprocessing.userdata.Preferences
import org.mesonet.network.DataDownloader
import java.net.HttpURLConnection
import java.util.*

import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

@Singleton
class FiveDayForecastDataController @Inject constructor(private var mMesonetSiteDataController: MesonetSiteDataController,
                                                        private var mThreadHandler: ThreadHandler,
                                                        private var mModelParser: ForecastModelParser,
                                                        private var mPreferences: Preferences,
                                                        private var mUnitConverter: UnitConverter,
                                                        private var mContext: Context) : Observable(), Observer {



    private var mDataDownloader: DataDownloader
    private var mSemiDayForecasts: MutableList<SemiDayForecastDataController> = ArrayList()

    private var mCurrentSite: String? = null

    init {
        mDataDownloader = DataDownloader(mThreadHandler)
        mThreadHandler.Run("ForecastData", Runnable {
            mMesonetSiteDataController.addObserver(this)
        })
    }


    fun SingleUpdate(inSingleUpdateListener: SingleUpdateListener)
    {
        mThreadHandler.Run("ForecastData", Runnable {
            mDataDownloader.SingleUpdate("http://www.mesonet.org/index.php/app/forecast/" + mMesonetSiteDataController.CurrentSelection(), object : DataDownloader.DownloadCallback {
                override fun DownloadComplete(inResponseCode: Int, inResult: String?) {
                    mThreadHandler.Run("ForecastData", Runnable {
                        if (inResponseCode >= HttpURLConnection.HTTP_OK && inResponseCode < HttpURLConnection.HTTP_MULT_CHOICE) {
                            SetData(inResult)
                            inSingleUpdateListener.UpdateComplete()
                        }
                        else
                            inSingleUpdateListener.UpdateFailed()
                    })
                }

                override fun DownloadFailed() {
                    inSingleUpdateListener.UpdateFailed()
                }
            })
        })
    }


    fun StartUpdates() {
        if(!mDataDownloader.IsUpdating()) {
            mThreadHandler.Run("ForecastData", Runnable {
                if (mMesonetSiteDataController.SiteDataFound()) {

                    mDataDownloader.StartDownloads("http://www.mesonet.org/index.php/app/forecast/" + mMesonetSiteDataController.CurrentSelection(), object : DataDownloader.DownloadCallback {
                        override fun DownloadComplete(inResponseCode: Int, inResult: String?) {
                            mThreadHandler.Run("ForecastData", Runnable {
                                if (inResponseCode >= HttpURLConnection.HTTP_OK && inResponseCode < HttpURLConnection.HTTP_MULT_CHOICE)
                                    SetData(inResult)
                            })
                        }

                        override fun DownloadFailed() {

                        }
                    }, 60000)
                }
            })
        }
    }

    override fun update(o: Observable, arg: Any?) {
        mThreadHandler.Run("ForecastData", Runnable {
            if(mCurrentSite == null || !mCurrentSite.equals(mMesonetSiteDataController.CurrentSelection()))
            {
                mCurrentSite = mMesonetSiteDataController.CurrentSelection()
                mSemiDayForecasts = ArrayList()
                StopUpdates()
                while(mDataDownloader.IsUpdating());
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
                    mSemiDayForecasts.add(SemiDayForecastDataController(mContext, mPreferences, mUnitConverter, inForecast[i], mThreadHandler))
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



    fun StopUpdates()
    {
        mThreadHandler.Run("ForecastData", Runnable {
            mDataDownloader.StopDownloads()
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


    interface SingleUpdateListener
    {
        fun UpdateComplete()
        fun UpdateFailed()
    }
}
