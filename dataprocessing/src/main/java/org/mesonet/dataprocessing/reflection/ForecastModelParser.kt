package org.mesonet.dataprocessing.reflection


import org.mesonet.dataprocessing.site.forecast.ForecastModel
import java.util.ArrayList

import javax.inject.Inject

class ForecastModelParser @Inject
internal constructor() {


    internal fun Parse(inStrValues: String?): List<ForecastModel>? {
        if (inStrValues == null || inStrValues.isEmpty())
            return null

        val result = ArrayList<ForecastModel>()

        val strForecast = inStrValues.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        var i = 0
        var fieldOffset = 0
        while (fieldOffset + 8 < strForecast.size) {

            try {
                val forecastModel = ForecastModel()

                if (Character.isDigit(strForecast[fieldOffset][0])) {
                    var found = false

                    var j = 0
                    while (j < strForecast[fieldOffset].length && !found) {
                        if (!Character.isDigit(strForecast[fieldOffset][j]) && strForecast[fieldOffset][j] != ' ') {
                            found = true
                            forecastModel.mTime = strForecast[fieldOffset].substring(j)
                        }
                        j++
                    }
                } else
                    forecastModel.mTime = strForecast[fieldOffset]
                    forecastModel.mIconUrl = strForecast[fieldOffset + 1]
                    forecastModel.mStatus = strForecast[fieldOffset + 2]
                    forecastModel.mHighOrLow = ForecastModel.HighOrLow.valueOf(strForecast[fieldOffset + 3])
                    forecastModel.mTemp = java.lang.Double.parseDouble(strForecast[fieldOffset + 4])

                result.add(forecastModel)
            } catch (e: ArrayIndexOutOfBoundsException) {
                e.printStackTrace()
            }
            catch (e: NumberFormatException)
            {
                e.printStackTrace()
            }
            catch (e: IllegalArgumentException)
            {
                e.printStackTrace()
            }

            i++
            fieldOffset += 8
        }

        return result
    }
}
