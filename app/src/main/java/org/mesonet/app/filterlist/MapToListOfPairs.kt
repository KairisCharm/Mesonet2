@file:JvmName("MapToListOfPairs")

package org.mesonet.app.filterlist

import android.location.Location
import org.mesonet.app.BasicViewHolder

class MapToListOfPairs
{
    fun Create(inMaps : Map<String, BasicViewHolder.BasicViewHolderData>) : List<Pair<String,BasicViewHolder.BasicViewHolderData>>
    {
        return inMaps.toList()
    }
}