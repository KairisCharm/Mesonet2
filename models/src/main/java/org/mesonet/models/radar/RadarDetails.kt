package org.mesonet.models.radar

interface RadarDetails
{
    fun GetLatitude(): Float?
    fun GetLongitude(): Float?
    fun GetRange(): Float?
    fun GetName(): String?
}