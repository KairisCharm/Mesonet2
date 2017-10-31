package org.mesonet.app.userdata;



public interface Preferences
{
    enum UnitPreference{ kMetric, kImperial }



    UnitPreference GetUnitPreference();
}