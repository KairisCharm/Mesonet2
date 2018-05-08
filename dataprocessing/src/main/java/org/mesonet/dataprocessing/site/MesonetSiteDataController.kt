package org.mesonet.dataprocessing.site


import android.location.Location
import android.util.Log
import com.google.gson.Gson
import org.mesonet.cache.site.SiteCache
import org.mesonet.core.PerActivity
import org.mesonet.core.ThreadHandler
import org.mesonet.dataprocessing.BasicListData
import org.mesonet.dataprocessing.LocationProvider
import org.mesonet.dataprocessing.SelectSiteListener
import org.mesonet.dataprocessing.filterlist.FilterListDataProvider
import org.mesonet.dataprocessing.userdata.Preferences
import org.mesonet.models.MesonetSiteListModel
import org.mesonet.network.DataDownloader

import java.net.HttpURLConnection
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MesonetSiteDataController @Inject
constructor(private var mLocationProvider: LocationProvider, private var mCache: SiteCache, private var mPreferences: Preferences, private var mThreadHandler: ThreadHandler) : Observable(), FilterListDataProvider, SelectSiteListener{


    private val mDataDownloader: DataDownloader

    private var mMesonetSiteModel: MesonetSiteListModel? = null

    private var mFavorites: MutableList<String>? = ArrayList()

    private var mCurrentSelection = "nrmn"

    private var mCurrentStationName = ""
    private var mCurrentIsFavorite = false


    init {
        mDataDownloader = DataDownloader(mThreadHandler)
        mThreadHandler.Run("MesonetData", Runnable {
            mCache.GetSites(object : SiteCache.SitesCacheListener {
                override fun SitesLoaded(inSiteResults: MesonetSiteListModel) {

                    if (mMesonetSiteModel == null) {
                        synchronized(this@MesonetSiteDataController)
                        {
                            mMesonetSiteModel = inSiteResults
                        }
                    }

                    mCache.GetFavorites(object : SiteCache.FavoritesCacheListener {
                        override fun FavoritesLoaded(inResults: MutableList<String>) {
                            Log.e("Site", "FavoritesLoaded")

                            mFavorites = inResults

                            if (mFavorites == null)
                                mFavorites = ArrayList()

                            LoadData()

                            setChanged()
                            notifyObservers()
                        }
                    })

                }
            })

            LoadData()
        })
    }


    internal fun SetData(inMesonetDataString: String?) {
        SetData(Gson().fromJson(inMesonetDataString, MesonetSiteListModel::class.java))
    }


    internal fun SetData(inMesonetSiteModel: MesonetSiteListModel) {
        synchronized(this@MesonetSiteDataController)
        {
            mMesonetSiteModel = inMesonetSiteModel

            val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss z")

            val keys = ArrayList(mMesonetSiteModel!!.keys)
            var i = 0
            while (i < keys.size) {
                var date: Date? = null

                try {
                    date = dateFormat.parse(mMesonetSiteModel!![keys[i]]?.GetDatd())
                } catch (e: ParseException) {
                    e.printStackTrace()
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }

                if (date == null || date.before(Date())) {
                    mMesonetSiteModel!!.remove(keys[i])
                    keys.removeAt(i)
                    i--
                }
                i++
            }

            mCache.SaveSites(mMesonetSiteModel!!)
        }
    }


    private fun FinalizeSelection()
    {
        if(mMesonetSiteModel != null && mMesonetSiteModel!![mCurrentSelection] != null && mMesonetSiteModel!![mCurrentSelection]?.GetName() != null) {
            mCurrentStationName = mMesonetSiteModel!![mCurrentSelection]!!.GetName()!!
            Log.e("StationName", mCurrentStationName)
        }

        mCurrentIsFavorite = IsFavorite(mCurrentSelection)

        Log.e("Site", "FinalizeSelection " + mCurrentStationName)
        setChanged()
        notifyObservers()
    }


    private fun GetNearestSite(inLocation: Location?): String {
        val keys = ArrayList(mMesonetSiteModel!!.keys)

        var shortestDistanceIndex = -1
        var shortestDistance = java.lang.Float.MAX_VALUE

        for (i in keys.indices) {
            val site = mMesonetSiteModel!![keys[i]]

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


    override fun AllViewHolderData(inListener: FilterListDataProvider.FilterListDataListener) {
        var data: Map<String, BasicListData>? = null

        mThreadHandler.Run("MesonetData", Runnable {
            data = if (mMesonetSiteModel == null) null else MakeMesonetStidNamePairs(mMesonetSiteModel!!, mFavorites!!)
        }, Runnable {
            inListener.ListDataBuilt(data)
        })
    }


    override fun CurrentSelection(): String {
        return mCurrentSelection
    }


    internal fun SiteDataFound(): Boolean {
        return mMesonetSiteModel != null && mMesonetSiteModel!!.size > 0
    }


    fun CurrentStationName(): String {
        return mCurrentStationName
    }


    internal fun CurrentStationElevation(): Number? {
        val currentSelection = CurrentSelection()

        if (mMesonetSiteModel == null || !mMesonetSiteModel!!.containsKey(currentSelection) || mMesonetSiteModel!![currentSelection] == null || mMesonetSiteModel!![currentSelection]?.GetElev() == null)
            return null

        val result: Double?

        try {
            result = java.lang.Double.parseDouble(mMesonetSiteModel!![currentSelection]?.GetElev())
        } catch (e: NumberFormatException) {
            return null
        }

        return result
    }


    override fun addObserver(o: Observer?) {
        Log.e("Site", "ObserverAdded")
        super.addObserver(o)
        setChanged()
        notifyObservers()
    }


    fun ToggleFavorite(inStid: String)
    {
        mThreadHandler.Run("MesonetData", Runnable {
            if (IsFavorite(inStid)) {
                RemoveFavorite(inStid)
            } else {
                AddFavorite(inStid)
            }
        })
    }


    fun CurrentIsFavorite(): Boolean
    {
        return mCurrentIsFavorite
    }


    internal fun IsFavorite(inStid: String): Boolean {
        return mFavorites != null && mFavorites!!.contains(inStid)
    }


    internal fun AddFavorite(inStid: String) {
        Log.e("Site", "FavoriteAdded")
        if (mFavorites != null) {
            mFavorites!!.add(inStid)
            if(inStid.equals(mCurrentSelection))
                mCurrentIsFavorite = IsFavorite(mCurrentSelection)
            mCache.SaveFavorites(mFavorites!!)
            setChanged()
            notifyObservers()
        }
    }


    internal fun RemoveFavorite(inStid: String) {
        Log.e("Site", "FavoriteRemoved")
        if (mFavorites != null && mFavorites!!.contains(inStid)) {
            mFavorites!!.remove(inStid)
            mCache.SaveFavorites(mFavorites!!)
            if(inStid.equals(mCurrentSelection))
                mCurrentIsFavorite = IsFavorite(mCurrentSelection)
            setChanged()
            notifyObservers()
        }
    }


    override fun GetDataObservable(): Observable {
        return this
    }


    override fun SetResult(inResult: String) {
        mThreadHandler.Run("MesonetData", Runnable {
            mCurrentSelection = inResult
            mPreferences.SetSelectedStid(inResult)

            FinalizeSelection()
        })
    }


    internal fun LoadData() {
        Log.e("Site", "LoadData")
        mPreferences.GetSelectedStid(object: Preferences.StidListener{
            override fun StidFound(inStidPreference: String) {
                Log.e("Site", "StidFound " + inStidPreference)
                mCurrentSelection = inStidPreference
                mCache.GetSites(object : SiteCache.SitesCacheListener {
                    override fun SitesLoaded(inSiteResults: MesonetSiteListModel) {
                        if (mMesonetSiteModel == null || mMesonetSiteModel!!.size == 0)
                            SetData(inSiteResults)

                        if(mCurrentSelection.isEmpty()) {
                            mLocationProvider.GetLocation(object : LocationProvider.LocationListener {
                                override fun LastLocationFound(inLocation: Location?) {
                                    mCurrentSelection = GetNearestSite(inLocation)
                                    mPreferences.SetSelectedStid(mCurrentSelection)

                                    FinalizeSelection()
                                }

                                override fun LocationUnavailable() {
                                    mCurrentSelection = "nrmn"
                                    FinalizeSelection()
                                }
                            })
                        }
                        else
                            FinalizeSelection()
                    }
                })
                mDataDownloader.SingleUpdate("http://www.mesonet.org/find/siteinfo-json", object : DataDownloader.DownloadCallback {
                    override fun DownloadComplete(inResponseCode: Int, inResult: String?) {
                        if (inResponseCode == HttpURLConnection.HTTP_OK)
                            SetData(inResult)
                    }

                    override fun DownloadFailed() {

                    }
                })
            }

        })
    }



    internal fun MakeMesonetStidNamePairs(inSiteModel : MesonetSiteListModel, inFavorites : List<String>) : Map<String, BasicListData>
    {
        val result = HashMap<String, BasicListData>()

        inSiteModel.forEach(
                {
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
                })

        return result
    }
}
