package org.mesonet.dataprocessing.site.mesonetdata

import org.mesonet.dataprocessing.formulas.UnitConverter
import org.mesonet.dataprocessing.reflection.MesonetModelParser
import org.mesonet.dataprocessing.site.MesonetSiteDataController
import org.mesonet.dataprocessing.userdata.Preferences
import org.mesonet.core.ThreadHandler
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
                                                private var mModelParser: MesonetModelParser) : Observable(), Observer {



    private var mDataDownloader: DataDownloader
    private var mMesonetModel: MesonetModel? = null


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
        SetData(mModelParser.Parse(MesonetModel::class, inMesonetDataString)!!)
    }


    internal fun SetData(inMesonetModel: MesonetModel) {
        if (inMesonetModel.STID?.toLowerCase() != mSiteDataController.CurrentSelection().toLowerCase())
            return

        mPreferences.GetPreferencesObservable().addObserver(this)

        mMesonetModel = inMesonetModel
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
        if (mMesonetModel == null)
            return null

        if (mMesonetModel!!.TAIR!!.toDouble() < -900.0)
            return null

        var tempUnits = UnitConverter.TempUnits.kCelsius

        if (inUnitPreference == Preferences.UnitPreference.kImperial)
            tempUnits = UnitConverter.TempUnits.kFahrenheit

        val result = mUnitConverter.GetTempInPreferredUnits(mMesonetModel!!.TAIR, mMesonetModel, tempUnits)
                ?: return null

        return result.toDouble()
    }


    internal fun ProcessApparentTemp(inUnitPreference: Preferences.UnitPreference): Double? {
        if (mMesonetModel == null)
            return null

        if (mMesonetModel!!.TAIR!!.toDouble() < -900.0)
            return null

        var tempUnits = UnitConverter.TempUnits.kCelsius

        if (inUnitPreference == Preferences.UnitPreference.kImperial)
            tempUnits = UnitConverter.TempUnits.kFahrenheit

        val apparentTemp = mDerivedValues.GetApparentTemperature(mMesonetModel!!.TAIR, mMesonetModel!!.WSPD, mMesonetModel!!.RELH)

        val result = mUnitConverter.GetTempInPreferredUnits(apparentTemp, mMesonetModel, tempUnits)
                ?: return null

        return result.toDouble()
    }


    internal fun ProcessDewpoint(inUnitPreference: Preferences.UnitPreference): Double? {
        if (mMesonetModel == null)
            return null

        if (mMesonetModel!!.TAIR!!.toDouble() < -900.0 || mMesonetModel!!.RELH!!.toDouble() < -900.0)
            return null

        var tempUnits = UnitConverter.TempUnits.kCelsius

        if (inUnitPreference == Preferences.UnitPreference.kImperial)
            tempUnits = UnitConverter.TempUnits.kFahrenheit

        val apparentTemp = mDerivedValues.GetDewpoint(mMesonetModel!!.TAIR, mMesonetModel!!.RELH)

        val result = mUnitConverter.GetTempInPreferredUnits(apparentTemp, mMesonetModel, tempUnits)
                ?: return null

        return result.toDouble()
    }


    internal fun ProcessWindSpd(inUnitPreference: Preferences.UnitPreference): Double? {
        if (mMesonetModel == null)
            return null

        if (mMesonetModel!!.WSPD == null)
            return null

        if (mMesonetModel!!.WSPD!!.toDouble() < -900.0)
            return null

        var speedUnits = UnitConverter.SpeedUnits.kMps

        if (inUnitPreference == Preferences.UnitPreference.kImperial)
            speedUnits = UnitConverter.SpeedUnits.kMph

        val result = mUnitConverter.GetSpeedInPreferredUnits(mMesonetModel!!.WSPD, mMesonetModel, speedUnits)
                ?: return null

        return result.toDouble()
    }


    internal fun ProcessWindDirection(): UnitConverter.CompassDirections? {
        if (mMesonetModel == null)
            return null

        return if (mMesonetModel!!.WDIR!!.toDouble() < -900.0) null else mUnitConverter.GetCompassDirection(mMesonetModel!!.WDIR)

    }


    internal fun ProcessMaxWind(inUnitPreference: Preferences.UnitPreference): Double? {
        if (mMesonetModel == null)
            return null

        if (mMesonetModel!!.WMAX!!.toDouble() < -900.0)
            return null

        var speedUnits = UnitConverter.SpeedUnits.kMps

        if (inUnitPreference == Preferences.UnitPreference.kImperial)
            speedUnits = UnitConverter.SpeedUnits.kMph

        val result = mUnitConverter.GetSpeedInPreferredUnits(mMesonetModel!!.WMAX, mMesonetModel, speedUnits)
                ?: return null

        return result.toDouble()
    }


    internal fun Process24HrRain(inUnitPreference: Preferences.UnitPreference): Double? {
        if (mMesonetModel == null || mMesonetModel!!.RAIN_24 == null)
            return null

        if (mMesonetModel!!.RAIN_24H!!.toDouble() < -900.0)
            return null

        var lengthUnits = UnitConverter.LengthUnits.kMm

        if (inUnitPreference == Preferences.UnitPreference.kImperial)
            lengthUnits = UnitConverter.LengthUnits.kIn

        val result = mUnitConverter.GetLengthInPreferredUnits(mMesonetModel!!.RAIN_24H, mMesonetModel, lengthUnits)
                ?: return null

        return result.toDouble()
    }


    internal fun ProcessHumidity(): Int? {
        return if (mMesonetModel == null || mMesonetModel!!.RELH == null) null else mMesonetModel!!.RELH?.toInt()

    }


    internal fun ProcessPressure(inUnitPreference: Preferences.UnitPreference): Double? {
        if (mMesonetModel == null)
            return null

        if (mMesonetModel!!.PRES!!.toDouble() < -900.0)
            return null

        var pressureUnits = UnitConverter.PressureUnits.kMb

        if (inUnitPreference == Preferences.UnitPreference.kImperial)
            pressureUnits = UnitConverter.PressureUnits.kInHg

        val result = mUnitConverter.GetPressureInPreferredUnits(mDerivedValues.GetMSLPressure(mMesonetModel!!.TAIR!!, mMesonetModel!!.PRES, mSiteDataController.CurrentStationElevation()), mMesonetModel, pressureUnits)
                ?: return null

        return result.toDouble()
    }


    /*TODO begin add tests*/
    internal fun ProcessTime(): Date? {
        return if (mMesonetModel == null || mMesonetModel!!.TIME == null) null else Date(mMesonetModel!!.TIME!! * 1000)

    }


    override fun update(observable: Observable, o: Any?) {
        mThreadHandler.Run("MesonetData", Runnable {
            mPreferences.GetSelectedStid(object: Preferences.StidListener{
                override fun StidFound(inStidPreference: String) {
                    if (mMesonetModel == null || !mMesonetModel?.STID?.toLowerCase().equals(inStidPreference)) {
                        mMesonetModel = null
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

