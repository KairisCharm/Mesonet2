package org.mesonet.dataprocessing.site.mesonetdata

import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.mesonet.core.DefaultUnits
import org.mesonet.dataprocessing.formulas.UnitConverter
import org.mesonet.dataprocessing.site.MesonetSiteDataController
import org.mesonet.dataprocessing.userdata.Preferences
import org.mesonet.models.site.mesonetdata.MesonetData
import org.mesonet.network.DataDownloader
import java.util.*

import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MesonetDataController @Inject constructor(private var mSiteDataController: MesonetSiteDataController,
                                                private var mDerivedValues: DerivedValues,
                                                private var mUnitConverter: UnitConverter,
                                                private var mDataDownloader: DataDownloader) : Observer<String>


{
    private var mMesonetData: MesonetData? = null

    private var mDataObservable = Observable.create(ObservableOnSubscribe<MesonetData> {subscriber ->
        mDataDownloader.GetMesonetData(mSiteDataController.CurrentSelection()).observeOn(Schedulers.computation()).subscribe (object: Observer<MesonetData>
        {
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(it: MesonetData) {
                SetData(it)
                if (mMesonetData != null)
                    subscriber.onNext(mMesonetData!!)
            }
        })
    }).subscribeOn(Schedulers.computation())

    init {
        mSiteDataController.GetCurrentSelectionObservable().observeOn(Schedulers.computation()).subscribe(this)
    }


    internal fun SetData(inMesonetData: MesonetData?) {
        if(inMesonetData == null)
            return

        if (inMesonetData.GetStID()?.toLowerCase() != mSiteDataController.CurrentSelection().toLowerCase())
            return

        mMesonetData = inMesonetData
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


    fun GetDataObservable(): Observable<MesonetData>
    {
        return mDataObservable
    }


    internal fun GetStationName(): String
    {
        return mSiteDataController.CurrentStationName()
    }



    internal fun ProcessTime(): Date? {
        return if (mMesonetData == null || mMesonetData!!.GetTime() == null) null else Date(mMesonetData!!.GetTime()!! * 1000)
    }


    override fun onNext(t: String) {
        if (mMesonetData == null || !mMesonetData?.GetStID()?.toLowerCase().equals(t)) {
            mMesonetData = null
            mDataObservable.subscribe()
        }
    }


    override fun onComplete() {}
    override fun onSubscribe(d: Disposable) {}
    override fun onError(e: Throwable)
    {
        e.printStackTrace()
    }
}
