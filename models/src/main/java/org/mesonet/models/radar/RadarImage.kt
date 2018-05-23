package org.mesonet.models.radar

interface RadarImage
{
    fun GetFilename(): String
    fun GetTimeString(): String
}