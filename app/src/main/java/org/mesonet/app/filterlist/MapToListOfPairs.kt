@file:JvmName("MapToListOfPairs")

package org.mesonet.app.filterlist

class MapToListOfPairs
{
    fun Create(inMaps : Map<String, String>) : List<Pair<String,String>>
    {
        return inMaps.toList()
    }
}