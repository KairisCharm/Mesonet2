package org.mesonet.dataprocessing.site


import android.content.Context
import android.location.Location
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.mesonet.cache.site.SiteCache
import org.mesonet.dataprocessing.BasicListData
import org.mesonet.dataprocessing.LocationProvider
import org.mesonet.dataprocessing.SelectSiteListener
import org.mesonet.dataprocessing.filterlist.FilterListDataProvider
import org.mesonet.dataprocessing.userdata.Preferences
import org.mesonet.models.site.MesonetSiteList
import org.mesonet.models.site.MesonetSiteListModel
import org.mesonet.network.DataDownloader

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MesonetSiteDataController @Inject
constructor(internal var mLocationProvider: LocationProvider,
            internal var mCache: SiteCache,
            internal var mPreferences: Preferences,
            internal var mContext: Context,
            internal val mDataDownloader: DataDownloader): FilterListDataProvider, SelectSiteListener
{
    private var mMesonetSiteList: MesonetSiteList? = null

    private var mFavorites: MutableList<String>? = ArrayList()

    private var mCurrentStationName = ""
    private var mCurrentIsFavorite = false

    private var mCurrentSelectionSubject: BehaviorSubject<String> = BehaviorSubject.create()


    init {
        mCurrentSelectionSubject.onNext("nrmn")
        Observable.create (ObservableOnSubscribe<Void>{
            mCache.GetSites().subscribe {
                    if (mMesonetSiteList == null) {
                        synchronized(this@MesonetSiteDataController)
                        {
                            mMesonetSiteList = it
                        }
                    }

                    mCache.GetFavorites().subscribe {
                            mFavorites = it

                            if (mFavorites == null)
                                mFavorites = ArrayList()

                            LoadData(mContext)
                    }

            }

            LoadData(mContext)
        }).subscribe()
    }


    fun GetCurrentSelectionSubject(): Observable<String>
    {
        return mCurrentSelectionSubject
    }


    internal fun SetData(inMesonetDataString: String?) {
        SetData(Gson().fromJson(inMesonetDataString, MesonetSiteListModel::class.java))
    }


    internal fun SetData(inMesonetSiteList: MesonetSiteList) {
        synchronized(this@MesonetSiteDataController)
        {
            mMesonetSiteList = inMesonetSiteList

            val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss z", Locale.US)

            val keys = ArrayList(mMesonetSiteList!!.keys)
            var i = 0
            while (i < keys.size) {
                var date: Date? = null

                try {
                    date = dateFormat.parse(mMesonetSiteList!![keys[i]]?.GetDatd())
                } catch (e: ParseException) {
                    e.printStackTrace()
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }

                if (date == null || date.before(Date())) {
                    mMesonetSiteList!!.remove(keys[i])
                    keys.removeAt(i)
                    i--
                }
                i++
            }

            mCache.SaveSites(mMesonetSiteList!!).subscribe()

            SetResult(mCurrentSelectionSubject.value)
        }
    }


    private fun FinalizeSelection(inStid: String)
    {
        if(mMesonetSiteList != null && mMesonetSiteList!![inStid] != null && mMesonetSiteList!![mCurrentSelectionSubject.value]!!.GetName() != null) {
            mCurrentStationName = mMesonetSiteList!![inStid]!!.GetName()!!
        }

        mCurrentIsFavorite = IsFavorite(inStid)

        if(mCurrentSelectionSubject.value != inStid)
            mCurrentSelectionSubject.onNext(inStid)
    }


    private fun GetNearestSite(inLocation: Location?): String {
        val keys = ArrayList(mMesonetSiteList!!.keys)

        var shortestDistanceIndex = -1
        var shortestDistance = java.lang.Float.MAX_VALUE

        for (i in keys.indices) {
            val site = mMesonetSiteList!![keys[i]]

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
            if (mMesonetSiteList != null) it.onNext(Pair(MakeMesonetStidNamePairs(mMesonetSiteList!!, mFavorites!!), mCurrentSelectionSubject.value))
        }).subscribeOn(Schedulers.computation())
    }


    override fun CurrentSelection(): String {
        return mCurrentSelectionSubject.value
    }



    fun CurrentStationName(): String {
        return mCurrentStationName
    }


    internal fun CurrentStationElevation(): Number? {
        val currentSelection = CurrentSelection()

        if (mMesonetSiteList == null || !mMesonetSiteList!!.containsKey(currentSelection) || mMesonetSiteList!![currentSelection] == null || mMesonetSiteList!![currentSelection]?.GetElev() == null)
            return null

        val result: Double?

        try {
            result = java.lang.Double.parseDouble(mMesonetSiteList!![currentSelection]?.GetElev())
        } catch (e: NumberFormatException) {
            return null
        }

        return result
    }



    fun ToggleFavorite(inStid: String): Observable<Boolean>
    {
        return Observable.create{
            if (IsFavorite(inStid)) {
                RemoveFavorite(inStid)
            } else {
                AddFavorite(inStid)
            }

            it.onNext(IsFavorite(inStid))
        }
    }


    fun CurrentIsFavorite(): Boolean
    {
        return mCurrentIsFavorite
    }


    internal fun IsFavorite(inStid: String): Boolean {
        return mFavorites != null && mFavorites!!.contains(inStid)
    }


    internal fun AddFavorite(inStid: String) {
        if (mFavorites != null) {
            mFavorites!!.add(inStid)
            if(inStid == mCurrentSelectionSubject.value)
                mCurrentIsFavorite = IsFavorite(mCurrentSelectionSubject.value)
            mCache.SaveFavorites(mFavorites!!).subscribe()
        }
    }


    internal fun RemoveFavorite(inStid: String) {
        if (mFavorites != null && mFavorites!!.contains(inStid)) {
            mFavorites!!.remove(inStid)
            mCache.SaveFavorites(mFavorites!!).subscribe()
            if(inStid == mCurrentSelectionSubject.value)
                mCurrentIsFavorite = IsFavorite(mCurrentSelectionSubject.value)
        }
    }


    override fun SetResult(inResult: String) {
        Observable.create(ObservableOnSubscribe<Void> {
            mPreferences.SetSelectedStid(inResult)

            FinalizeSelection(inResult)
        }).subscribe()
    }


    internal fun LoadData(inContext: Context) {
        mPreferences.SelectedStidSubject().subscribe { stid ->
            mCache.GetSites().subscribe {
                if (mMesonetSiteList == null || mMesonetSiteList!!.size == 0)
                    SetData(it)

                if(stid.isEmpty()) {
                    mLocationProvider.GetLocation(inContext).observeOn(Schedulers.computation()).subscribe{

                        var result = "nrmn"

                        if(it.LocationResult() != null)
                            result = GetNearestSite(it.LocationResult())

                        mPreferences.SetSelectedStid(result)

                        FinalizeSelection(result)
                    }
                }
                else
                    FinalizeSelection(stid)
            }

            mDataDownloader.GetMesonetSites().observeOn(Schedulers.computation()).subscribe(object: Observer<MesonetSiteList> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: MesonetSiteList) {
                    SetData(t)
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }
            })
        }
    }



    internal fun MakeMesonetStidNamePairs(inSiteList : MesonetSiteList, inFavorites : List<String>) : Map<String, BasicListData>
    {
        val result = HashMap<String, BasicListData>()

        inSiteList.forEach {
            val location = Location("none")
            location.latitude = it.value.GetLat()!!.toDouble()
            location.longitude = it.value.GetLon()!!.toDouble()
            result.put(it.key, object: BasicListData {
                override fun GetName(): String {
                    return it.value.GetName()!!
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
}