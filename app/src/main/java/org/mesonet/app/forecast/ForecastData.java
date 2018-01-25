package org.mesonet.app.forecast;


import org.mesonet.app.formulas.UnitConverter;

import java.util.Observable;

public interface ForecastData
{
    String GetTime();
    String GetIconUrl();
    String GetStatus();
    String GetHighOrLow();
    String GetTemp();
    String GetWindDirection();
    String GetWindGustsDirection();
    String GetWindSpeed();
    String GetWindGusts();
    Observable GetObservable();
}
