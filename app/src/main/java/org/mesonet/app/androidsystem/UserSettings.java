package org.mesonet.app.androidsystem;

import android.content.Context;
import android.content.SharedPreferences;


public class UserSettings
{
    private static UserSettings sUserSettings;



    public static UserSettings GetInstance()
    {
        if(sUserSettings == null)
            sUserSettings = new UserSettings();

        return sUserSettings;
    }



    private UserSettings(){}




    private void SetPreference(Context inContext, String inName, String inValue)
    {
        SharedPreferences preferences = inContext.getSharedPreferences("MesonetSettings", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(inName, inValue);
        editor.apply();
    }
}
