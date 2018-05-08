package org.mesonet.cache.site


import org.mesonet.core.PerActivity
import org.mesonet.models.MesonetSiteListModel

import java.util.ArrayList

import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SiteListConverter @Inject
constructor() {


    fun FavoriteToRealmModels(inSiteModels: MesonetSiteListModel): List<SiteRealmModel> {
        val result = ArrayList<SiteRealmModel>()

        val keySet = ArrayList(inSiteModels.keys)

        for (i in 0 until inSiteModels.size) {
            result.add(inSiteModels[keySet[i]]?.let { SiteRealmModel(keySet[i], it) }!!)
        }

        return result
    }


    fun SiteFromRealmModels(inRealmModels: List<SiteRealmModel>): MesonetSiteListModel {
        val result = MesonetSiteListModel()

        for (i in inRealmModels.indices) {
            result[inRealmModels[i].GetStid().toString()] = inRealmModels[i].ToSiteModel()
        }

        return result
    }


    fun FavoriteToRealmModels(inFavorites: List<String>): List<FavoriteSiteRealmModel> {
        val result = ArrayList<FavoriteSiteRealmModel>()

        for (i in inFavorites.indices) {
            result.add(FavoriteSiteRealmModel(inFavorites[i]))
        }

        return result
    }


    fun FavoriteFromRealmModels(inRealmModels: List<FavoriteSiteRealmModel>): MutableList<String> {
        val result = ArrayList<String>()

        for (i in inRealmModels.indices) {
            result.add(inRealmModels[i].GetName().toString())
        }

        return result
    }
}
