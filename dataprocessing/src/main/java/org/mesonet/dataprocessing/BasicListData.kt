package org.mesonet.dataprocessing

import android.location.Location

interface BasicListData
{
    fun GetName(): String
    fun IsFavorite(): Boolean
    fun GetLocation(): Location
}