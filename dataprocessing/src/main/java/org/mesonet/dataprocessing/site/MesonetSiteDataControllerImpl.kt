package org.mesonet.dataprocessing.site


import android.location.Location
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.mesonet.cache.site.SiteCache
import org.mesonet.core.PerActivity
import org.mesonet.dataprocessing.BasicListData
import org.mesonet.dataprocessing.LocationProvider
import org.mesonet.dataprocessing.userdata.Preferences
import org.mesonet.models.site.MesonetSiteList
import org.mesonet.models.site.MesonetSiteListModel
import org.mesonet.network.DataProvider

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import android.content.Context


@PerActivity
class MesonetSiteDataControllerImpl @Inject
constructor(internal var mLocationProvider: LocationProvider,
            internal var mCache: SiteCache,
            internal var mPreferences: Preferences,
            internal val mDataProvider: DataProvider): MesonetSiteDataController
{
    val kDefaultSelection = "nrmn"

    private var mInit = false

    val mCurrentSelectionSubject: BehaviorSubject<String> = BehaviorSubject.create()
    var mMesonetSiteList: MesonetSiteList? = null

    var mFavorites: MutableList<String> = ArrayList()
    var mLocationDisposable: Disposable? = null
    private var mCurrentStationName = ""

    private var mCurrentIsFavorite = false
    internal fun Init(inContext: Context) {
        mInit = true
        fun FindCachedStid()
        {
            mPreferences.SelectedStidSubject(inContext).observeOn(Schedulers.computation()).subscribe(object : Observer<String> {
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}

                override fun onNext(t: String) {
                    if (mMesonetSiteList?.keys?.contains(t) == true)
                    {
                        if(!mCurrentSelectionSubject.hasValue() || mCurrentSelectionSubject.value != t)
                            FinalizeSelection(inContext, t)
                    }
                    else
                        FinalizeSelection(inContext, kDefaultSelection)
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    onNext("")
                }
            })
        }

        fun FindNearestStid()
        {
            mLocationProvider.GetLocation().observeOn(Schedulers.computation()).subscribe(object: Observer<LocationProvider.LocationResult>{
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {
                    mLocationDisposable = d
                }

                override fun onNext(t: LocationProvider.LocationResult) {
                    val nearestStid = GetNearestSite(t.LocationResult())

                    if(!mCurrentSelectionSubject.hasValue() || mCurrentSelectionSubject.value != nearestStid)
                    {
                        if (mMesonetSiteList?.keys?.contains(nearestStid) == true)
                            FinalizeSelection(inContext, nearestStid)
                        else
                            FindCachedStid()
                    }
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    FindCachedStid()
                }
            })
        }

        fun GetFavorites()
        {
            mCache.GetFavorites(inContext).observeOn(Schedulers.computation()).subscribe(object: Observer<MutableList<String>>{
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: MutableList<String>) {
                    mFavorites = t
                    FindNearestStid()
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    onNext(ArrayList())
                }
            })
        }

        mCache.GetSites(inContext).observeOn(Schedulers.computation()).subscribe(object: Observer<MesonetSiteList>
        {
            override fun onComplete() {}
            override fun onSubscribe(d: Disposable) {}
            override fun onNext(t: MesonetSiteList) {
                mMesonetSiteList = t
                GetFavorites()
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }
        })

        mDataProvider.GetMesonetSites().observeOn(Schedulers.computation()).subscribe(object: Observer<MesonetSiteList> {
            override fun onComplete() {}
            override fun onSubscribe(d: Disposable) {}
            override fun onNext(t: MesonetSiteList) {
                val cacheLoaded = mMesonetSiteList?.isEmpty() == true
                SaveList(inContext, t)

                if (!cacheLoaded) {
                    GetFavorites()
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }
        })
    }

    override fun GetCurrentSelectionSubject(inContext: Context): Observable<String> {
        if(!mInit)
            Init(inContext)

        return mCurrentSelectionSubject
    }


    override fun CurrentStationName(): String {
        return mCurrentStationName
    }

    override fun CurrentIsFavorite(): Boolean {
        return mCurrentIsFavorite
    }

    override fun CurrentSelection(): String {
        return mCurrentSelectionSubject.value
    }


    override fun SetResult(inContext: Context, inResult: String) {
        if(!mInit)
            Init(inContext)

        if(mMesonetSiteList?.containsKey(inResult) == true) {
            mLocationDisposable?.dispose()
            mPreferences.SetSelectedStid(inContext, inResult)
            FinalizeSelection(inContext, inResult)
        }
    }


    internal fun SaveList(inContext: Context, inMesonetDataString: String) {
        if(!mInit)
            Init(inContext)
        SaveList(inContext, Gson().fromJson(inMesonetDataString, MesonetSiteListModel::class.java))
    }

    internal fun SaveList(inContext: Context, inMesonetSiteList: MesonetSiteList) {
        if(!mInit)
            Init(inContext)
        synchronized(this@MesonetSiteDataControllerImpl)
        {
            mMesonetSiteList = inMesonetSiteList

            val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss z", Locale.US)

            val keys = ArrayList(mMesonetSiteList?.keys?: ArrayList())
            var i = 0
            while (i < keys.size) {
                var date: Date? = null

                try {
                    date = dateFormat.parse(mMesonetSiteList?.get(keys[i])?.GetDatd())
                } catch (e: ParseException) {
                    e.printStackTrace()
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }

                if (date == null || date.before(Date())) {
                    mMesonetSiteList?.remove(keys[i])
                    keys.removeAt(i)
                    i--
                }
                i++
            }

            synchronized(this@MesonetSiteDataControllerImpl)
            {
                if (mMesonetSiteList != null)
                    mCache.SaveSites(inContext, mMesonetSiteList!!).subscribe(object: Observer<Void>
                    {
                        override fun onComplete() {}
                        override fun onSubscribe(d: Disposable) {}
                        override fun onNext(t: Void) {}
                        override fun onError(e: Throwable) {
                            e.printStackTrace()
                        }
                    })
            }
        }
    }


    private fun FinalizeSelection(inContext: Context, inStid: String)
    {
        mCurrentStationName = mMesonetSiteList?.get(inStid)?.GetName()?: ""
        mCurrentIsFavorite = IsFavorite(inStid)

        if(mCurrentSelectionSubject.value != inStid)
            mCurrentSelectionSubject.onNext(inStid)

        mPreferences.SetSelectedStid(inContext, inStid)
    }


    override fun GetMeteogramUrl(): String {
        if(!mCurrentSelectionSubject.hasValue())
            return ""
        return mDataProvider.GetMeteogramImageUrl(mCurrentSelectionSubject.value.toUpperCase())
    }


    private fun GetNearestSite(inLocation: Location?): String {
        if(inLocation == null)
            return ""

        val keys = ArrayList(mMesonetSiteList?.keys)

        var shortestDistanceIndex = -1
        var shortestDistance = java.lang.Float.MAX_VALUE

        for (i in keys.indices) {
            val site = mMesonetSiteList?.get(keys[i])

            if(site != null) {
                val siteLocation = Location("none")

                siteLocation.latitude = java.lang.Double.parseDouble(site.GetLat())
                siteLocation.longitude = java.lang.Double.parseDouble(site.GetLon())

                val distance = siteLocation.distanceTo(inLocation)

                if (distance < shortestDistance) {
                    shortestDistance = distance
                    shortestDistanceIndex = i
                }
            }
        }

        return if (shortestDistanceIndex == -1) "" else keys[shortestDistanceIndex]

    }


    override fun AsBasicListData(): Observable<Pair<Map<String, BasicListData>, String>> {
        return Observable.create(ObservableOnSubscribe<Pair<Map<String, BasicListData>, String>> {
            synchronized(this@MesonetSiteDataControllerImpl)
            {
                if(mMesonetSiteList != null) {
                    it.onNext(Pair(MakeMesonetStidNamePairs(mMesonetSiteList!!, mFavorites), mCurrentSelectionSubject.value))
                    it.onComplete()
                }
            }
        }).subscribeOn(Schedulers.computation())
    }


    override fun CurrentStationElevation(): Number? {
        val currentSelection = CurrentSelection()

        if (mMesonetSiteList?.containsKey(currentSelection) != true || mMesonetSiteList?.get(currentSelection) == null || mMesonetSiteList?.get(currentSelection)?.GetElev() == null)
            return null

        val result: Double?

        try {
            result = java.lang.Double.parseDouble(mMesonetSiteList?.get(currentSelection)?.GetElev())
        } catch (e: NumberFormatException) {
            return null
        }

        return result
    }

    override fun ToggleFavorite(inContext: Context, inStid: String): Observable<Boolean>
    {
        return Observable.create{
            if (IsFavorite(inStid)) {
                RemoveFavorite(inContext, inStid)
            } else {
                AddFavorite(inContext, inStid)
            }

            it.onNext(IsFavorite(inStid))
            it.onComplete()
        }
    }


    internal fun IsFavorite(inStid: String): Boolean {
        return mFavorites.contains(inStid)
    }

    internal fun AddFavorite(inContext: Context, inStid: String) {
        mFavorites.add(inStid)
        if(inStid == mCurrentSelectionSubject.value)
            mCurrentIsFavorite = IsFavorite(mCurrentSelectionSubject.value)
        mCache.SaveFavorites(inContext, mFavorites).subscribe(object: Observer<Void>{
            override fun onComplete() {}
            override fun onSubscribe(d: Disposable) {}
            override fun onNext(t: Void) {}
            override fun onError(e: Throwable) {
                e.printStackTrace()
            }
        })
    }

    internal fun RemoveFavorite(inContext: Context, inStid: String) {
        if (mFavorites.contains(inStid)) {
            mFavorites.remove(inStid)
            mCache.SaveFavorites(inContext, mFavorites).subscribe(object: Observer<Void>{
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: Void) {}
                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }

            })
            if(inStid == mCurrentSelectionSubject.value)
                mCurrentIsFavorite = IsFavorite(mCurrentSelectionSubject.value)
        }
    }


    internal fun MakeMesonetStidNamePairs(inSiteList : MesonetSiteList, inFavorites : List<String>) : Map<String, BasicListData>
    {
        val result = HashMap<String, BasicListData>()

        inSiteList.forEach {
            val location = Location("none")
            location.latitude = it.value.GetLat()?.toDouble() ?: 0.0
            location.longitude = it.value.GetLon()?.toDouble() ?: 0.0
            result.put(it.key, object: BasicListData {
                override fun GetName(): String {
                    return it.value.GetName()?: ""
                }

                override fun IsFavorite(): Boolean {
                    return inFavorites.contains(it.key)
                }

                override fun GetLocation(): Location {
                    return location
                }

            })
        }

        return result
    }

    override fun Dispose() {
        mLocationDisposable?.dispose()
        mCurrentSelectionSubject.onComplete()
    }
}