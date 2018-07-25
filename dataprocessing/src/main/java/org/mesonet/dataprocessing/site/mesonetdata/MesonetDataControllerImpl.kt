package org.mesonet.dataprocessing.site.mesonetdata

import android.content.Context
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.mesonet.core.DefaultUnits
import org.mesonet.core.PerActivity
import org.mesonet.dataprocessing.formulas.UnitConverter
import org.mesonet.dataprocessing.site.MesonetSiteDataController
import org.mesonet.dataprocessing.userdata.Preferences
import org.mesonet.models.site.mesonetdata.MesonetData
import org.mesonet.network.DataProvider
import java.util.*
import java.util.concurrent.TimeUnit

import javax.inject.Inject
import javax.inject.Singleton


@PerActivity
class MesonetDataControllerImpl @Inject constructor(private var mSiteDataController: MesonetSiteDataController,
                                                private var mDerivedValues: DerivedValues,
                                                private var mUnitConverter: UnitConverter,
                                                private var mDataProvider: DataProvider) : MesonetDataController, Observer<String>


{
    private var mMesonetData: MesonetData? = null

    private var mDisposable: Disposable? = null

    private var mCurrentSiteDisposable: Disposable? = null

    private var mDataSubject: BehaviorSubject<MesonetData> = BehaviorSubject.create()

    private var mInit = false

    internal fun Init(inContext: Context) {
        mInit = true
        mSiteDataController.GetCurrentSelectionSubject(inContext).observeOn(Schedulers.computation()).subscribe(this)
    }


    internal fun SetData(inMesonetData: MesonetData?) {
        if(inMesonetData == null)
            return

        if (inMesonetData.GetStID()?.toLowerCase() != mSiteDataController.CurrentSelection().toLowerCase())
            return

        mMesonetData = inMesonetData
    }


    override fun ProcessTemp(inUnitPreference: Preferences.UnitPreference): Double? {
        if (mMesonetData == null)
            return null

        if (mMesonetData?.GetTAir()?.toDouble()?: -900.0 < -900.0)
            return null

        var tempUnits = DefaultUnits.TempUnits.kCelsius

        if (inUnitPreference == Preferences.UnitPreference.kImperial)
            tempUnits = DefaultUnits.TempUnits.kFahrenheit

        val result = mUnitConverter.GetTempInPreferredUnits(mMesonetData?.GetTAir(), mMesonetData, tempUnits)
                ?: return null

        return result.toDouble()
    }


    override fun ProcessApparentTemp(inUnitPreference: Preferences.UnitPreference): Double? {
        if (mMesonetData == null)
            return null

        if (mMesonetData?.GetTAir()?.toDouble()?: -900.0 < -900.0)
            return null

        var tempUnits = DefaultUnits.TempUnits.kCelsius

        if (inUnitPreference == Preferences.UnitPreference.kImperial)
            tempUnits = DefaultUnits.TempUnits.kFahrenheit

        val apparentTemp = mDerivedValues.GetApparentTemperature(mMesonetData?.GetTAir(), mMesonetData?.GetWSpd(), mMesonetData?.GetRelH())

        val result = mUnitConverter.GetTempInPreferredUnits(apparentTemp, mMesonetData, tempUnits)
                ?: return null

        return result.toDouble()
    }



    override fun ProcessDewpoint(inUnitPreference: Preferences.UnitPreference): Double? {
        if (mMesonetData == null)
            return null

        if (mMesonetData?.GetTAir()?.toDouble()?: -900.0 < -900.0 || mMesonetData?.GetRelH()?.toDouble()?: -900.0 < -900.0)
            return null

        var tempUnits = DefaultUnits.TempUnits.kCelsius

        if (inUnitPreference == Preferences.UnitPreference.kImperial)
            tempUnits = DefaultUnits.TempUnits.kFahrenheit

        val apparentTemp = mDerivedValues.GetDewpoint(mMesonetData?.GetTAir(), mMesonetData?.GetRelH())

        val result = mUnitConverter.GetTempInPreferredUnits(apparentTemp, mMesonetData, tempUnits)
                ?: return null

        return result.toDouble()
    }


    override fun ProcessWindSpd(inUnitPreference: Preferences.UnitPreference): Double? {
        if (mMesonetData == null)
            return null

        if (mMesonetData?.GetWSpd() == null)
            return null

        if (mMesonetData?.GetWSpd()?.toDouble()?: -900.0 < -900.0)
            return null

        var speedUnits = DefaultUnits.SpeedUnits.kMps

        if (inUnitPreference == Preferences.UnitPreference.kImperial)
            speedUnits = DefaultUnits.SpeedUnits.kMph

        val result = mUnitConverter.GetSpeedInPreferredUnits(mMesonetData?.GetWSpd(), mMesonetData, speedUnits)
                ?: return null

        return result.toDouble()
    }


    override fun ProcessWindDirection(): DefaultUnits.CompassDirections? {
        if (mMesonetData == null)
            return null

        return if (mMesonetData?.GetWDir()?.toDouble()?: -900.0 < -900.0) null else mUnitConverter.GetCompassDirection(mMesonetData?.GetWDir())

    }


    override fun ProcessMaxWind(inUnitPreference: Preferences.UnitPreference): Double? {
        if (mMesonetData == null)
            return null

        if (mMesonetData?.GetWMax()?.toDouble()?: -900.0 < -900.0)
            return null

        var speedUnits = DefaultUnits.SpeedUnits.kMps

        if (inUnitPreference == Preferences.UnitPreference.kImperial)
            speedUnits = DefaultUnits.SpeedUnits.kMph

        val result = mUnitConverter.GetSpeedInPreferredUnits(mMesonetData?.GetWMax(), mMesonetData, speedUnits)
                ?: return null

        return result.toDouble()
    }


    override fun Process24HrRain(inUnitPreference: Preferences.UnitPreference): Double? {
        if (mMesonetData == null || mMesonetData?.GetRain24H() == null)
            return null

        if (mMesonetData?.GetRain24H()?.toDouble()?: -900.0 < -900.0)
            return null

        var lengthUnits = DefaultUnits.LengthUnits.kMm

        if (inUnitPreference == Preferences.UnitPreference.kImperial)
            lengthUnits = DefaultUnits.LengthUnits.kIn

        val result = mUnitConverter.GetLengthInPreferredUnits(mMesonetData?.GetRain24H(), mMesonetData, lengthUnits)
                ?: return null

        return result.toDouble()
    }


    override fun ProcessHumidity(): Int? {
        return if (mMesonetData == null || mMesonetData?.GetRelH() == null || mMesonetData?.GetRelH()?.toDouble()?: -900.0 < -900.0) null else mMesonetData?.GetRelH()?.toInt()

    }


    override fun ProcessPressure(inUnitPreference: Preferences.UnitPreference): Double? {
        if (mMesonetData == null)
            return null

        if (mMesonetData?.GetPres()?.toDouble()?: -900.0 < -900.0)
            return null

        var pressureUnits = DefaultUnits.PressureUnits.kMb

        if (inUnitPreference == Preferences.UnitPreference.kImperial)
            pressureUnits = DefaultUnits.PressureUnits.kInHg

        val result = mUnitConverter.GetPressureInPreferredUnits(mDerivedValues.GetMSLPressure(mMesonetData?.GetTAir()?: 0.0, mMesonetData?.GetPres(), mSiteDataController.CurrentStationElevation()), mMesonetData, pressureUnits)
                ?: return null

        return result.toDouble()
    }


    override fun GetDataSubject(inContext: Context): BehaviorSubject<MesonetData>
    {
        if(!mInit)
            Init(inContext)

        return mDataSubject
    }


    override fun GetStationName(): String
    {
        return mSiteDataController.CurrentStationName()
    }


    override fun StationIsFavorite(): Boolean {
        return mSiteDataController.CurrentIsFavorite()
    }



    override fun ProcessTime(): Date? {
        return if (mMesonetData == null || mMesonetData?.GetTime() == null) null else Date((mMesonetData?.GetTime()?: 0) * 1000)
    }


    override fun onNext(t: String) {
        if (mMesonetData == null || !mMesonetData?.GetStID()?.toLowerCase().equals(t)) {
            mMesonetData = null

            mDisposable?.dispose()

            Observable.interval(0, 1, TimeUnit.MINUTES).subscribeOn(Schedulers.computation()).subscribe(object : Observer<Long> {
                override fun onComplete() {}

                override fun onSubscribe(d: Disposable) {
                    mDisposable = d
                }

                override fun onNext(t: Long) {
                    mDataProvider.GetMesonetData(mSiteDataController.CurrentSelection()).observeOn(Schedulers.computation()).subscribe(object : Observer<MesonetData> {
                        override fun onComplete() {}
                        override fun onSubscribe(d: Disposable) {}
                        override fun onNext(t: MesonetData) {
                            SetData(t)
                            if (mMesonetData != null && (!mDataSubject.hasValue() || mMesonetData?.compareTo(mDataSubject.value) != 0))
                                mDataSubject.onNext(mMesonetData
                                        ?: object : MesonetData {
                                            override fun GetTime(): Long? {
                                                return null
                                            }

                                            override fun GetRelH(): Number? {
                                                return null
                                            }

                                            override fun GetTAir(): Number? {
                                                return null
                                            }

                                            override fun GetWSpd(): Number? {
                                                return null
                                            }

                                            override fun GetWDir(): Number? {
                                                return null
                                            }

                                            override fun GetWMax(): Number? {
                                                return null
                                            }

                                            override fun GetPres(): Number? {
                                                return null
                                            }

                                            override fun GetRain24H(): Number? {
                                                return null
                                            }

                                            override fun GetStID(): String? {
                                                return null
                                            }

                                            override fun GetDefaultTempUnit(): DefaultUnits.TempUnits? {
                                                return null
                                            }

                                            override fun GetDefaultLengthUnit(): DefaultUnits.LengthUnits? {
                                                return null
                                            }

                                            override fun GetDefaultSpeedUnit(): DefaultUnits.SpeedUnits? {
                                                return null
                                            }

                                            override fun GetDefaultPressureUnit(): DefaultUnits.PressureUnits? {
                                                return null
                                            }

                                            override fun compareTo(other: MesonetData): Int {
                                                return 1
                                            }
                                        })
                        }

                        override fun onError(e: Throwable) {
                            e.printStackTrace()
                            onNext(mMesonetData ?: object : MesonetData {
                                override fun GetTime(): Long? {
                                    return null
                                }

                                override fun GetRelH(): Number? {
                                    return null
                                }

                                override fun GetTAir(): Number? {
                                    return null
                                }

                                override fun GetWSpd(): Number? {
                                    return null
                                }

                                override fun GetWDir(): Number? {
                                    return null
                                }

                                override fun GetWMax(): Number? {
                                    return null
                                }

                                override fun GetPres(): Number? {
                                    return null
                                }

                                override fun GetRain24H(): Number? {
                                    return null
                                }

                                override fun GetStID(): String? {
                                    return null
                                }

                                override fun GetDefaultTempUnit(): DefaultUnits.TempUnits? {
                                    return null
                                }

                                override fun GetDefaultLengthUnit(): DefaultUnits.LengthUnits? {
                                    return null
                                }

                                override fun GetDefaultSpeedUnit(): DefaultUnits.SpeedUnits? {
                                    return null
                                }

                                override fun GetDefaultPressureUnit(): DefaultUnits.PressureUnits? {
                                    return null
                                }

                                override fun compareTo(other: MesonetData): Int {
                                    return 1
                                }
                            })
                        }

                    })
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }
            })
        }
    }


    override fun onComplete() {}
    override fun onSubscribe(d: Disposable)
    {
        mCurrentSiteDisposable = d
    }
    override fun onError(e: Throwable)
    {
        e.printStackTrace()
    }


    override fun Dispose()
    {
        mCurrentSiteDisposable?.dispose()
        mDisposable?.dispose()
        mDataSubject.onComplete()
    }
}
