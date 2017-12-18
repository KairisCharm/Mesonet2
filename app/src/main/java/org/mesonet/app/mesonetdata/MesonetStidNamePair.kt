@file:JvmName("MesonetStidNamePair")

package org.mesonet.app.mesonetdata

import android.location.Location


class MesonetStidNamePair
{
    fun Create(inSiteModel : MesonetSiteModel) : Map<String, Pair<String, Location>>
    {
        val result = HashMap<String, Pair<String, Location>>()

        inSiteModel.forEach(
        {
            val location = Location("none")
            location.latitude = it.value.mLat.toDouble()
            location.longitude = it.value.mLon.toDouble()
            result.put(it.key, Pair(it.value.mName, location))
        })

        return result
    }
}