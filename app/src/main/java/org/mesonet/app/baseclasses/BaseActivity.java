package org.mesonet.app.baseclasses;

import android.support.annotation.AnimRes;
import android.support.annotation.AnimatorRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toolbar;

import org.mesonet.app.R;
import org.mesonet.app.androidsystem.Permissions;
import org.mesonet.app.androidsystem.UserSettings;
import org.mesonet.app.userdata.Preferences;
import org.mesonet.app.userdata.PreferencesObservable;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasFragmentInjector;
import dagger.android.support.HasSupportFragmentInjector;


public abstract class BaseActivity extends AppCompatActivity implements HasSupportFragmentInjector, Preferences
{
    @Inject
    DispatchingAndroidInjector<Fragment> mFragmentInjector;

    @Inject
    Permissions mPermissions;


    @Inject
    protected UserSettings mUserSettings;

    protected PreferencesObservable mPreferencesObservable = new PreferencesObservable();


        @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return mFragmentInjector;
    }



    @Override
    public UnitPreference GetUnitPreference() {
        return UnitPreference.valueOf(mUserSettings.GetStringPreference(this, UserSettings.kUnitPreference, UnitPreference.kImperial.name()));
    }

    @Override
    public void SetUnitPreference(UnitPreference inPreference)
    {
        mUserSettings.SetPreference(this, UserSettings.kUnitPreference, inPreference.toString());
    }

    @Override
    public String GetSelectedStid() {
        return mUserSettings.GetStringPreference(this, UserSettings.kSelectedStid, "nrmn");
    }

    @Override
    public void SetSelectedStid(String inStid)
    {
        mUserSettings.SetPreference(this, UserSettings.kSelectedStid, inStid);
    }

    @Override
    public String GetSelectedRadar()
    {
        return mUserSettings.GetStringPreference(this, UserSettings.kSelectedRadar, "KTLX");
    }

    @Override
    public void SetSelectedRadar(String inStid)
    {
        mUserSettings.SetPreference(this, UserSettings.kSelectedRadar, inStid);
    }


    @Override
    public PreferencesObservable GetPreferencesObservable()
    {
        return mPreferencesObservable;
    }



    @Override
    public void onRequestPermissionsResult(int inRequestCode,
                                           @NonNull String[] inPermissions,
                                           @NonNull int[] inGrantResults)
    {
        mPermissions.ProcessPermissionResponse(inPermissions, inGrantResults);
    }


    public abstract void NavigateToPage(Fragment inFragment, boolean inPushToBackStack, @AnimRes int inAnimationIn, @AnimRes int inAnimationOut);
}
