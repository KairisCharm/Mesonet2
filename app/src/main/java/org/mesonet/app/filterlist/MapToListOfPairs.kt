@file:JvmName("MapToListOfPairs")

package org.mesonet.app.filterlist

import android.location.Location

class MapToListOfPairs
{
    fun Create(inMaps : Map<String, Pair<String, Location>>) : List<Pair<String,Pair<String, Location>>>
    {
        return inMaps.toList()
    }
}