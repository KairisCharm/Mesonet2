package org.mesonet.dataprocessing.site.mesonetdata

import org.mesonet.core.DefaultUnits
import org.mesonet.dataprocessing.formulas.UnitConverter
import org.mesonet.models.site.mesonetdata.MesonetModelParser
import org.mesonet.dataprocessing.site.MesonetSiteDataController
import org.mesonet.dataprocessing.userdata.Preferences
import org.mesonet.core.ThreadHandler
import org.mesonet.models.site.mesonetdata.MesonetData
import org.mesonet.models.site.mesonetdata.MesonetDataCreator
import org.mesonet.network.DataDownloader
import java.net.HttpURLConnection
import java.util.*

import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MesonetDataController @Inject constructor(private var mSiteDataController: MesonetSiteDataController,
                                                private var mPreferences: Preferences,
                                                private var mThreadHandler: ThreadHandler,
                                                private var mDerivedValues: DerivedValues,
                                                private var mUnitConverter: UnitConverter,
                                                private var mMesonetDataCreator: MesonetDataCreator) : Observable(), Observer {



    private var mDataDownloader: DataDownloader
    private var mMesonetData: MesonetData? = null


    init {
        mDataDownloader = DataDownloader(mThreadHandler)
        mSiteDataController.GetDataObservable().addObserver(this)
        mPreferences.GetPreferencesObservable().addObserver(this)
        mSiteDataController.addObserver(this)
    }



    fun SingleUpdate(inUpdateListener: SingleUpdateListener)
    {
        mThreadHandler.Run("MesonetData", Runnable {
            mDataDownloader.SingleUpdate(GetUrl(), object : DataDownloader.DownloadCallback{
                override fun DownloadComplete(inResponseCode: Int, inResult: String?) {
                    if (inResponseCode >= HttpURLConnection.HTTP_OK && inResponseCode < HttpURLConnection.HTTP_MULT_CHOICE) {
                        SetData(inResult!!)
                        inUpdateListener.UpdateComplete()
                    }
                    else
                        inUpdateListener.UpdateFailed()
                }

                override fun DownloadFailed() {
                    inUpdateListener.UpdateFailed()
                }

            })
        })
    }


    fun StartUpdates() {
        if(!mDataDownloader.IsUpdating()) {
            mThreadHandler.Run("MesonetData", Runnable {
                if (!mSiteDataController.SiteDataFound() || mSiteDataController.CurrentSelection().isEmpty()) {
                    StopUpdates()
                } else {

                    mDataDownloader.StartDownloads(GetUrl(),
                            object : DataDownloader.DownloadCallback {
                                override fun DownloadComplete(inResponseCode: Int,
                                                              inResult: String?) {
                                    if (inResponseCode >= HttpURLConnection.HTTP_OK && inResponseCode < HttpURLConnection.HTTP_MULT_CHOICE) {
                                        SetData(inResult!!)
                                    }
                                }


                                override fun DownloadFailed() {

                                }
                            }, 60000)
                }
            })
        }
    }



    internal fun SetData(inMesonetDataString: String) {
        SetData(mMesonetDataCreator.CreateMesonetData(inMesonetDataString))
    }


    internal fun SetData(inMesonetData: MesonetData?) {
        if(inMesonetData == null)
            return

        if (inMesonetData.GetStID()?.toLowerCase() != mSiteDataController.CurrentSelection().toLowerCase())
            return

        mPreferences.GetPreferencesObservable().addObserver(this)

        mMesonetData = inMesonetData
        setChanged()
        notifyObservers()
    }


    fun StopUpdates() {
        mThreadHandler.Run("MesonetData", Runnable {
            mDataDownloader.StopDownloads()
        })
    }



    internal fun GetUrl(): String {
        return "http://www.mesonet.org/index.php/app/latest_iphone/" + mSiteDataController.CurrentSelection()
    }



    internal fun ProcessTemp(inUnitPreference: Preferences.UnitPreference): Double? {
        if (mMesonetData == null)
            return null

        if (mMesonetData!!.GetTAir()!!.toDouble() < -900.0)
            return null

        var tempUnits = DefaultUnits.TempUnits.kCelsius

        if (inUnitPreference == Preferences.UnitPreference.kImperial)
            tempUnits = DefaultUnits.TempUnits.kFahrenheit

        val result = mUnitConverter.GetTempInPreferredUnits(mMesonetData!!.GetTAir(), mMesonetData, tempUnits)
                ?: return null

        return result.toDouble()
    }


    internal fun ProcessApparentTemp(inUnitPreference: Preferences.UnitPreference): Double? {
        if (mMesonetData == null)
            return null

        if (mMesonetData!!.GetTAir()!!.toDouble() < -900.0)
            return null

        var tempUnits = DefaultUnits.TempUnits.kCelsius

        if (inUnitPreference == Preferences.UnitPreference.kImperial)
            tempUnits = DefaultUnits.TempUnits.kFahrenheit

        val apparentTemp = mDerivedValues.GetApparentTemperature(mMesonetData!!.GetTAir(), mMesonetData!!.GetWSpd(), mMesonetData!!.GetRelH())

        val result = mUnitConverter.GetTempInPreferredUnits(apparentTemp, mMesonetData, tempUnits)
                ?: return null

        return result.toDouble()
    }


    internal fun ProcessDewpoint(inUnitPreference: Preferences.UnitPreference): Double? {
        if (mMesonetData == null)
            return null

        if (mMesonetData!!.GetTAir()!!.toDouble() < -900.0 || mMesonetData!!.GetRelH()!!.toDouble() < -900.0)
            return null

        var tempUnits = DefaultUnits.TempUnits.kCelsius

        if (inUnitPreference == Preferences.UnitPreference.kImperial)
            tempUnits = DefaultUnits.TempUnits.kFahrenheit

        val apparentTemp = mDerivedValues.GetDewpoint(mMesonetData!!.GetTAir(), mMesonetData!!.GetRelH())

        val result = mUnitConverter.GetTempInPreferredUnits(apparentTemp, mMesonetData, tempUnits)
                ?: return null

        return result.toDouble()
    }


    internal fun ProcessWindSpd(inUnitPreference: Preferences.UnitPreference): Double? {
        if (mMesonetData == null)
            return null

        if (mMesonetData!!.GetWSpd() == null)
            return null

        if (mMesonetData!!.GetWSpd()!!.toDouble() < -900.0)
            return null

        var speedUnits = DefaultUnits.SpeedUnits.kMps

        if (inUnitPreference == Preferences.UnitPreference.kImperial)
            speedUnits = DefaultUnits.SpeedUnits.kMph

        val result = mUnitConverter.GetSpeedInPreferredUnits(mMesonetData!!.GetWSpd(), mMesonetData, speedUnits)
                ?: return null

        return result.toDouble()
    }


    internal fun ProcessWindDirection(): DefaultUnits.CompassDirections? {
        if (mMesonetData == null)
            return null

        return if (mMesonetData!!.GetWDir()!!.toDouble() < -900.0) null else mUnitConverter.GetCompassDirection(mMesonetData!!.GetWDir())

    }


    internal fun ProcessMaxWind(inUnitPreference: Preferences.UnitPreference): Double? {
        if (mMesonetData == null)
            return null

        if (mMesonetData!!.GetWMax()!!.toDouble() < -900.0)
            return null

        var speedUnits = DefaultUnits.SpeedUnits.kMps

        if (inUnitPreference == Preferences.UnitPreference.kImperial)
            speedUnits = DefaultUnits.SpeedUnits.kMph

        val result = mUnitConverter.GetSpeedInPreferredUnits(mMesonetData!!.GetWMax(), mMesonetData, speedUnits)
                ?: return null

        return result.toDouble()
    }


    internal fun Process24HrRain(inUnitPreference: Preferences.UnitPreference): Double? {
        if (mMesonetData == null || mMesonetData!!.GetRain24H() == null)
            return null

        if (mMesonetData!!.GetRain24H()!!.toDouble() < -900.0)
            return null

        var lengthUnits = DefaultUnits.LengthUnits.kMm

        if (inUnitPreference == Preferences.UnitPreference.kImperial)
            lengthUnits = DefaultUnits.LengthUnits.kIn

        val result = mUnitConverter.GetLengthInPreferredUnits(mMesonetData!!.GetRain24H(), mMesonetData, lengthUnits)
                ?: return null

        return result.toDouble()
    }


    internal fun ProcessHumidity(): Int? {
        return if (mMesonetData == null || mMesonetData!!.GetRelH() == null) null else mMesonetData!!.GetRelH()?.toInt()

    }


    internal fun ProcessPressure(inUnitPreference: Preferences.UnitPreference): Double? {
        if (mMesonetData == null)
            return null

        if (mMesonetData!!.GetPres()!!.toDouble() < -900.0)
            return null

        var pressureUnits = DefaultUnits.PressureUnits.kMb

        if (inUnitPreference == Preferences.UnitPreference.kImperial)
            pressureUnits = DefaultUnits.PressureUnits.kInHg

        val result = mUnitConverter.GetPressureInPreferredUnits(mDerivedValues.GetMSLPressure(mMesonetData!!.GetTAir()!!, mMesonetData!!.GetPres(), mSiteDataController.CurrentStationElevation()), mMesonetData, pressureUnits)
                ?: return null

        return result.toDouble()
    }


    /*TODO begin add tests*/
    internal fun ProcessTime(): Date? {
        return if (mMesonetData == null || mMesonetData!!.GetTime() == null) null else Date(mMesonetData!!.GetTime()!! * 1000)

    }


    override fun update(observable: Observable, o: Any?) {
        mThreadHandler.Run("MesonetData", Runnable {
            mPreferences.GetSelectedStid(object: Preferences.StidListener{
                override fun StidFound(inStidPreference: String) {
                    if (mMesonetData == null || !mMesonetData?.GetStID()?.toLowerCase().equals(inStidPreference)) {
                        mMesonetData = null
                        StopUpdates()
                        while(mDataDownloader.IsUpdating());
                        StartUpdates()
                    }

                    setChanged()
                    notifyObservers()
                }
            })
        })
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

