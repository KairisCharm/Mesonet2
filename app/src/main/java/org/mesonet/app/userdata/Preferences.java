package org.mesonet.app.userdata;

import java.util.Observable;



public interface Preferences
{
    enum UnitPreference{ kMetric, kImperial }



    UnitPreference GetUnitPreference();
    Observable GetObservable();
}