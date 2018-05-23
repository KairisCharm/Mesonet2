package org.mesonet.models.site.forecast

interface ForecastParser
{
    fun Parse(inStrValues: String): List<Forecast>?
}