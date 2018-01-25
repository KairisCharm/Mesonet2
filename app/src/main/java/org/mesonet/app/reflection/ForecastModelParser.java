package org.mesonet.app.reflection;


import org.mesonet.app.forecast.ForecastModel;
import org.mesonet.app.formulas.UnitConverter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ForecastModelParser
{
    private static ForecastModelParser sModelParser;



    public static ForecastModelParser GetInstance()
    {
        if(sModelParser == null)
            sModelParser = new ForecastModelParser();

        return sModelParser;
    }



    private ForecastModelParser(){}



    public List<ForecastModel> Parse(String inStrValues)
    {
        if(inStrValues == null || inStrValues.isEmpty())
            return null;

        List<ForecastModel> result = new ArrayList<>();

        String[] strForecast = inStrValues.split(",");

        for(int i = 0, fieldOffset = 0; (fieldOffset + 8) < strForecast.length; i++, fieldOffset += 8) {

            try {
                ForecastModel forecastModel = new ForecastModel();

                if( Character.isDigit(strForecast[fieldOffset].charAt(0))) {
                    boolean found = false;

                    for(int j = 0; j < strForecast[fieldOffset].length() && !found; j++) {
                        if(!Character.isDigit(strForecast[fieldOffset].charAt(j)) && strForecast[fieldOffset].charAt(j) != ' ') {
                            found = true;
                            forecastModel.mTime = strForecast[fieldOffset].substring(j);
                        }
                    }
                }
                else
                    forecastModel.mTime = strForecast[fieldOffset];
                forecastModel.mIconUrl = strForecast[fieldOffset + 1];
                forecastModel.mStatus = strForecast[fieldOffset + 2];
                forecastModel.mHighOrLow = ForecastModel.HighOrLow.valueOf(strForecast[fieldOffset + 3]);
                forecastModel.mTemp = Double.parseDouble(strForecast[fieldOffset + 4]);
//                forecastModel.mWindDirection = UnitConverter.CompassDirections.valueOf(strForecast[fieldOffset + 5]);
//                forecastModel.mWindGustsDirection = UnitConverter.CompassDirections.valueOf(strForecast[fieldOffset + 6]);
//                forecastModel.mWindSpd = Double.parseDouble(strForecast[fieldOffset + 7]);
//
//                if( Character.isDigit(strForecast[fieldOffset + 8].charAt(0))) {
//                    boolean found = false;
//
//                    for(int j = 0; j < strForecast[fieldOffset + 8].length() && !found; j++) {
//                        if(!Character.isDigit(strForecast[fieldOffset + 8].charAt(j)) && strForecast[fieldOffset + 8].charAt(j) != ' ') {
//                            found = true;
//                            forecastModel.mWindGusts = Double.parseDouble(strForecast[fieldOffset + 8].substring(0, j));
//                        }
//                    }
//                }
//                else
//                    forecastModel.mWindGusts = Double.parseDouble(strForecast[fieldOffset + 8]);

                result.add(forecastModel);
            }
            catch (ArrayIndexOutOfBoundsException e)
            {
                e.printStackTrace();
            }
        }

        return result;
    }
}
