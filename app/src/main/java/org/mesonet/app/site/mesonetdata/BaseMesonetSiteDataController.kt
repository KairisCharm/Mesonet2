package org.mesonet.app.site.mesonetdata

import android.location.Location

import com.google.gson.Gson

import org.mesonet.app.BasicViewHolder
import org.mesonet.app.androidsystem.DeviceLocation
import org.mesonet.app.androidsystem.Permissions
import org.mesonet.app.dependencyinjection.PerActivity
import org.mesonet.app.filterlist.FilterListFragment
import org.mesonet.app.site.SiteSelectionInterfaces
import org.mesonet.app.site.caching.SiteCache
import org.mesonet.app.userdata.Preferences

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.Observable
import java.util.Observer

import javax.inject.Inject


@PerActivity
open class BaseMesonetSiteDataController @Inject constructor(
                                             var mDeviceLocation: DeviceLocation, var mCache: SiteCache) : Observable(), FilterListFragment.FilterListDataProvider, SiteSelectionInterfaces.SelectSiteListener, Observer {
    @Inject
    lateinit var mPreferences: Preferences

    @Inject
    lateinit var mPermissions: Permissions

    internal var mMesonetSiteModel: MesonetSiteListModel? = null

    internal var mFavorites: MutableList<String>? = ArrayList()


    init {
        mCache.GetSites(object : SiteCache.SitesCacheListener {
            override fun SitesLoaded(inSiteResults: MesonetSiteListModel) {

                mCache.GetFavorites(object : SiteCache.FavoritesCacheListener {
                    override fun FavoritesLoaded(inResults: MutableList<String>) {
                        if (mMesonetSiteModel == null) {
                            mMesonetSiteModel = inSiteResults
                        }

                        mFavorites = inResults

                        if (mFavorites == null)
                            mFavorites = ArrayList()

                        setChanged()
                        notifyObservers()
                    }
                })

            }
        })
    }


    override fun update(observable: Observable, o: Any?) {
        setChanged()
        notifyObservers()
    }


    fun SetData(inMesonetDataString: String?) {
        SetData(Gson().fromJson(inMesonetDataString, MesonetSiteListModel::class.java))
    }


    fun SetData(inMesonetSiteModel: MesonetSiteListModel) {
        mMesonetSiteModel = inMesonetSiteModel

        val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss z")

        val keys = ArrayList(mMesonetSiteModel!!.keys)
        var i = 0
        while (i < keys.size) {
            var date: Date? = null

            try {
                date = dateFormat.parse(mMesonetSiteModel!![keys[i]]?.mDatd)
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

        mDeviceLocation.GetLocation(object : DeviceLocation.LocationListener {
            override fun LastLocationFound(inLocation: Location?) {
                mPreferences!!.SetSelectedStid(GetNearestSite(inLocation))
                setChanged()
                notifyObservers()
            }

            override fun LocationUnavailable() {
                setChanged()
                notifyObservers()
            }
        })
    }


    private fun GetNearestSite(inLocation: Location?): String {
        val keys = ArrayList(mMesonetSiteModel!!.keys)

        var shortestDistanceIndex = -1
        var shortestDistance = java.lang.Float.MAX_VALUE

        for (i in keys.indices) {
            val site = mMesonetSiteModel!![keys[i]]

            val siteLocation = Location("none")

            siteLocation.latitude = java.lang.Double.parseDouble(site?.mLat)
            siteLocation.longitude = java.lang.Double.parseDouble(site?.mLon)

            val distance = siteLocation.distanceTo(inLocation)

            if (distance < shortestDistance) {
                shortestDistance = distance
                shortestDistanceIndex = i
            }
        }

        return if (shortestDistanceIndex == -1) "" else keys[shortestDistanceIndex]

    }


    override fun AllViewHolderData(): Map<String, BasicViewHolder.BasicViewHolderData>? {
        return if (mMesonetSiteModel == null) null else MesonetStidNamePair().Create(mMesonetSiteModel!!, mFavorites!!)
    }


    override fun CurrentSelection(): String {
        return mPreferences!!.GetSelectedStid()
    }


    fun SiteDataFound(): Boolean {
        return mMesonetSiteModel != null && mMesonetSiteModel!!.size > 0
    }


    fun CurrentStationName(): String? {
        val currentSelection = CurrentSelection()

        return if (currentSelection == null || mMesonetSiteModel == null || !mMesonetSiteModel!!.containsKey(currentSelection)) null else mMesonetSiteModel!![currentSelection]?.mName
    }


    fun CurrentStationElevation(): Number? {
        val currentSelection = CurrentSelection()

        if (currentSelection == null ||
                mMesonetSiteModel == null ||
                !mMesonetSiteModel!!.containsKey(currentSelection) ||
                mMesonetSiteModel!![currentSelection] == null ||
                mMesonetSiteModel!![currentSelection]?.mElev == null)
            return null

        var result: Double? = null

        try {
            result = java.lang.Double.parseDouble(mMesonetSiteModel!![currentSelection]?.mElev)
        } catch (e: NumberFormatException) {
            return null
        }

        return result
    }


    fun IsFavorite(inStid: String): Boolean {
        return mFavorites != null && mFavorites!!.contains(inStid)
    }


    fun AddFavorite(inStid: String) {
        if (mFavorites != null) {
            mFavorites!!.add(inStid)
            mCache.SaveFavorites(mFavorites!!)
            setChanged()
            notifyObservers()
        }
    }


    fun RemoveFavorite(inStid: String) {
        if (mFavorites != null && mFavorites!!.contains(inStid)) {
            mFavorites!!.remove(inStid)
            mCache.SaveFavorites(mFavorites!!)
            setChanged()
            notifyObservers()
        }
    }


    open protected fun LoadData(){}


    override fun GetDataObservable(): Observable {
        return this
    }


    override fun SetResult(inResult: String) {
        mPreferences.SetSelectedStid(inResult)
        setChanged()
        notifyObservers()
    }
}
