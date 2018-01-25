package org.mesonet.app.userdata;

import java.util.Observable;



public interface Preferences
{
    enum UnitPreference{ kMetric, kImperial }



    UnitPreference GetUnitPreference();
    void SetUnitPreference(UnitPreference inPreference);
    String GetSelectedStid();
    void SetSelectedStid(String inStid);
    PreferencesObservable GetPreferencesObservable();
}