package org.mesonet.app.site.mesonetdata

import org.mesonet.app.dependencyinjection.PerFragment
import org.mesonet.app.formulas.UnitConverter
import org.mesonet.app.reflection.MesonetModelParser
import org.mesonet.app.site.caching.SiteCache
//import org.mesonet.app.site.mesonetdata.dependencyinjection.DaggerMesonetDataComponent;
import org.mesonet.app.userdata.Preferences

import java.util.Date
import java.util.Observable
import java.util.Observer

import javax.inject.Inject


@PerFragment
abstract class BaseMesonetDataController(internal var mSiteDataController: MesonetSiteDataController, internal var mPreferences: Preferences?) : Observable(), MesonetData, Observer {
    internal var mMesonetModel: MesonetModel? = null

    @Inject
    lateinit var mCache: SiteCache

    @Inject
    lateinit var mDerivedValues: DerivedValues

    @Inject
    lateinit var mUnitConverter: UnitConverter

    @Inject
    lateinit var mModelParser: MesonetModelParser


    private var mUpdating = false


    init {
        mSiteDataController.GetDataObservable().addObserver(this)
        mPreferences!!.GetPreferencesObservable().addObserver(this)
        mSiteDataController.addObserver(this)
    }


    internal fun SetData(inMesonetDataString: String) {
        SetData(mModelParser.Parse(MesonetModel::class, inMesonetDataString)!!)
    }


    internal fun SetData(inMesonetModel: MesonetModel) {
        if (inMesonetModel.STID?.toLowerCase() != mSiteDataController.CurrentSelection().toLowerCase())
            return

        mPreferences!!.GetPreferencesObservable().addObserver(this)
        mMesonetModel = inMesonetModel
        setChanged()
        notifyObservers()
    }


    open fun StartUpdating() {
        mUpdating = true
    }


    fun StopUpdating() {
        mUpdating = false
    }


    protected fun IsUpdating(): Boolean {
        return mUpdating
    }


    override fun GetTemp(): Double? {
        if (mMesonetModel == null)
            return null

        if (mMesonetModel!!.TAIR!!.toDouble() < -900.0)
            return null

        var tempUnits = UnitConverter.TempUnits.kCelsius

        if (mPreferences == null || mPreferences!!.GetUnitPreference() == Preferences.UnitPreference.kImperial)
            tempUnits = UnitConverter.TempUnits.kFahrenheit

        val result = mUnitConverter.GetTempInPreferredUnits(mMesonetModel!!.TAIR, mMesonetModel, tempUnits)
                ?: return null

        return result.toDouble()
    }


    override fun GetApparentTemp(): Double? {
        if (mMesonetModel == null)
            return null

        if (mMesonetModel!!.TAIR!!.toDouble() < -900.0)
            return null

        var tempUnits = UnitConverter.TempUnits.kCelsius

        if (mPreferences == null || mPreferences!!.GetUnitPreference() == Preferences.UnitPreference.kImperial)
            tempUnits = UnitConverter.TempUnits.kFahrenheit

        val apparentTemp = mDerivedValues.GetApparentTemperature(mMesonetModel!!.TAIR, mMesonetModel!!.WSPD, mMesonetModel!!.RELH)

        val result = mUnitConverter!!.GetTempInPreferredUnits(apparentTemp, mMesonetModel, tempUnits)
                ?: return null

        return result.toDouble()
    }


    override fun GetDewpoint(): Double? {
        if (mMesonetModel == null)
            return null

        if (mMesonetModel!!.TAIR!!.toDouble() < -900.0 || mMesonetModel!!.RELH!!.toDouble() < -900.0)
            return null

        var tempUnits = UnitConverter.TempUnits.kCelsius

        if (mPreferences == null || mPreferences!!.GetUnitPreference() == Preferences.UnitPreference.kImperial)
            tempUnits = UnitConverter.TempUnits.kFahrenheit

        val apparentTemp = mDerivedValues.GetDewpoint(mMesonetModel!!.TAIR, mMesonetModel!!.RELH)

        val result = mUnitConverter.GetTempInPreferredUnits(apparentTemp, mMesonetModel, tempUnits)
                ?: return null

        return result.toDouble()
    }


    override fun GetWindSpd(): Double? {
        if (mMesonetModel == null)
            return null

        if (mMesonetModel!!.WSPD == null)
            return null

        if (mMesonetModel!!.WSPD!!.toDouble() < -900.0)
            return null

        var speedUnits = UnitConverter.SpeedUnits.kMps

        if (mPreferences == null || mPreferences!!.GetUnitPreference() == Preferences.UnitPreference.kImperial)
            speedUnits = UnitConverter.SpeedUnits.kMph

        val result = mUnitConverter.GetSpeedInPreferredUnits(mMesonetModel!!.WSPD, mMesonetModel, speedUnits)
                ?: return null

        return result.toDouble()
    }


    override fun GetWindDirection(): UnitConverter.CompassDirections? {
        if (mMesonetModel == null)
            return null

        return if (mMesonetModel!!.WDIR!!.toDouble() < -900.0) null else mUnitConverter.GetCompassDirection(mMesonetModel!!.WDIR)

    }


    override fun GetMaxWind(): Double? {
        if (mMesonetModel == null)
            return null

        if (mMesonetModel!!.WMAX!!.toDouble() < -900.0)
            return null

        var speedUnits = UnitConverter.SpeedUnits.kMps

        if (mPreferences == null || mPreferences!!.GetUnitPreference() == Preferences.UnitPreference.kImperial)
            speedUnits = UnitConverter.SpeedUnits.kMph

        val result = mUnitConverter!!.GetSpeedInPreferredUnits(mMesonetModel!!.WMAX, mMesonetModel, speedUnits)
                ?: return null

        return result.toDouble()
    }


    override fun Get24HrRain(): Double? {
        if (mMesonetModel == null || mMesonetModel!!.RAIN_24 == null)
            return null

        if (mMesonetModel!!.RAIN_24H!!.toDouble() < -900.0)
            return null

        var lengthUnits = UnitConverter.LengthUnits.kMm

        if (mPreferences != null && mPreferences!!.GetUnitPreference() == Preferences.UnitPreference.kImperial)
            lengthUnits = UnitConverter.LengthUnits.kIn

        val result = mUnitConverter.GetLengthInPreferredUnits(mMesonetModel!!.RAIN_24H, mMesonetModel, lengthUnits)
                ?: return null

        return result.toDouble()
    }


    override fun GetHumidity(): Int? {
        return if (mMesonetModel == null || mMesonetModel!!.RELH == null) null else mMesonetModel!!.RELH?.toInt()

    }


    override fun GetPressure(): Double? {
        if (mMesonetModel == null)
            return null

        if (mMesonetModel!!.PRES!!.toDouble() < -900.0)
            return null

        var pressureUnits = UnitConverter.PressureUnits.kMb

        if (mPreferences != null && mPreferences!!.GetUnitPreference() == Preferences.UnitPreference.kImperial)
            pressureUnits = UnitConverter.PressureUnits.kInHg

        val result = mUnitConverter!!.GetPressureInPreferredUnits(mDerivedValues!!.GetMSLPressure(mMesonetModel!!.TAIR!!, mMesonetModel!!.PRES, mSiteDataController.CurrentStationElevation()), mMesonetModel, pressureUnits)
                ?: return null

        return result.toDouble()
    }


    /*TODO begin add tests*/
    override fun GetTime(): Date? {
        return if (mMesonetModel == null || mMesonetModel!!.TIME == null) null else Date(mMesonetModel!!.TIME!! * 1000)

    }


    protected fun GetStid(): String? {
        return if (mMesonetModel == null) null else mMesonetModel!!.STID

    }
    /*TODO end add tests*/


    fun GetUnitPreference(): Preferences.UnitPreference? {
        return if (mPreferences == null) null else mPreferences!!.GetUnitPreference()

    }


    override fun update(observable: Observable, o: Any?) {
        if (mMesonetModel != null && GetStid()!!.toLowerCase() != mPreferences!!.GetSelectedStid())
            StopUpdating()

        if (!IsUpdating())
            StartUpdating()

        setChanged()
        notifyObservers()
    }
}
