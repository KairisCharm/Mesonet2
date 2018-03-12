package org.mesonet.app.androidsystem;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;


public class UserSettings
{
    private static final String kMesonetSettings = "MesonetSettings";
    public static final String kUnitPreference = "UnitPreference";
    public static final String kSelectedStid = "SelectedStid";
    public static final String kSelectedRadar = "SelectedRadar";

    @Inject
    UserSettings(){}




    public void SetPreference(Context inContext, String inName, String inValue)
    {
        SharedPreferences preferences = inContext.getSharedPreferences(kMesonetSettings, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(inName, inValue);
        editor.apply();
    }



    public String GetStringPreference(Context inContext, String inName, String inDefault)
    {
        SharedPreferences preferences = inContext.getSharedPreferences(kMesonetSettings, Context.MODE_PRIVATE);

        String result = preferences.getString(inName, inDefault);
        return result;
    }
}
