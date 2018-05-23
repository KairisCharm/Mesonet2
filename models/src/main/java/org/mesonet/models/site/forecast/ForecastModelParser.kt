package org.mesonet.models.site.forecast


import org.mesonet.core.DefaultUnits
import java.util.ArrayList

import javax.inject.Inject

class ForecastModelParser @Inject
internal constructor() : ForecastParser {
    override fun Parse(inStrValues: String): List<Forecast>? {
        if (inStrValues.isEmpty())
            return ArrayList()

        val result = ArrayList<ForecastModel>()

        val strForecastList = inStrValues.split("\n")

        for (i in strForecastList.indices) {

            val strForecast = strForecastList[i].split(",")

            if(strForecast.size != 9)
                continue

            val forecastModel = ForecastModel()

            forecastModel.mTime = strForecast[0]
            forecastModel.mIconUrl = strForecast[1]
            forecastModel.mStatus = strForecast[2]

            try {
                forecastModel.mHighOrLow = Forecast.HighOrLow.valueOf(strForecast[3])
            }
            catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }

            try {
                forecastModel.mTemp = java.lang.Double.parseDouble(strForecast[4])
            }
            catch (e: NumberFormatException)
            {
                e.printStackTrace()
            }

            try {
                forecastModel.mWindDirectionStart = DefaultUnits.CompassDirections.valueOf(strForecast[5])
            }
            catch (e: IllegalArgumentException)
            {
                e.printStackTrace()
            }

            try {
                forecastModel.mWindDirectionEnd = DefaultUnits.CompassDirections.valueOf(strForecast[6])
            }
            catch (e: IllegalArgumentException)
            {
                e.printStackTrace()
            }

            try {
                forecastModel.mWindMin = java.lang.Double.parseDouble(strForecast[7])
            }
            catch (e: NumberFormatException)
            {
                e.printStackTrace()
            }

            try {
                forecastModel.mWindMax = java.lang.Double.parseDouble(strForecast[8])
            }
            catch (e: NumberFormatException)
            {
                e.printStackTrace()
            }

            if(forecastModel.GetIconUrl() != null ||
                    forecastModel.GetTemp() != null ||
                    forecastModel.GetHighOrLow() != null ||
                    forecastModel.GetStatus() != null ||
                    forecastModel.GetWindDirectionStart() != null ||
                    forecastModel.GetWindDirectionEnd() != null ||
                    forecastModel.GetWindMin() != null ||
                    forecastModel.GetWindMax() != null)
            {
                result.add(forecastModel)
            }
        }

        return result
    }
}
