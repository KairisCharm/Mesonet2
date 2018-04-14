@file:JvmName("MesonetStidNamePair")

package org.mesonet.app.site.mesonetdata

import android.location.Location
import org.mesonet.app.BasicViewHolder


class MesonetStidNamePair
{
    fun Create(inSiteModel : MesonetSiteListModel, inFavorites : List<String>) : Map<String, BasicViewHolder.BasicViewHolderData>
    {
        val result = HashMap<String, BasicViewHolder.BasicViewHolderData>()

        inSiteModel.forEach(
        {
            val location = Location("none")
            location.latitude = it.value.mLat!!.toDouble()
            location.longitude = it.value.mLon!!.toDouble()
            result.put(it.key, BasicViewHolder.BasicViewHolderData(it.value.mName!!, location, inFavorites.contains(it.key)))
        })

        return result
    }
}