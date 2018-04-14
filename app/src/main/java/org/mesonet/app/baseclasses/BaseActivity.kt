package org.mesonet.app.baseclasses

import android.support.annotation.AnimRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

import org.mesonet.app.userdata.Preferences
import org.mesonet.app.userdata.PreferencesObservable

import javax.inject.Inject

import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import org.mesonet.app.androidsystem.Permissions
import org.mesonet.app.androidsystem.UserSettings


abstract class BaseActivity : AppCompatActivity(), HasSupportFragmentInjector, Preferences {
    @Inject
    lateinit var mFragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var mPermissions: Permissions


    @Inject
    lateinit var mUserSettings: UserSettings

    protected var mPreferencesObservable = PreferencesObservable()


    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return mFragmentInjector
    }


    override fun GetUnitPreference(): Preferences.UnitPreference {
        return Preferences.UnitPreference.valueOf(mUserSettings!!.GetStringPreference(this, UserSettings.kUnitPreference, Preferences.UnitPreference.kImperial.name))
    }

    override fun SetUnitPreference(inPreference: Preferences.UnitPreference) {
        mUserSettings!!.SetPreference(this, UserSettings.kUnitPreference, inPreference.toString())
    }

    override fun GetSelectedStid(): String {
        return mUserSettings!!.GetStringPreference(this, UserSettings.kSelectedStid, "nrmn")
    }

    override fun SetSelectedStid(inStid: String) {
        mUserSettings!!.SetPreference(this, UserSettings.kSelectedStid, inStid)
    }

    override fun GetSelectedRadar(): String {
        return mUserSettings!!.GetStringPreference(this, UserSettings.kSelectedRadar, "KTLX")
    }

    override fun SetSelectedRadar(inStid: String) {
        mUserSettings!!.SetPreference(this, UserSettings.kSelectedRadar, inStid)
    }


    override fun GetPreferencesObservable(): PreferencesObservable {
        return mPreferencesObservable
    }


    override fun onRequestPermissionsResult(inRequestCode: Int,
                                            inPermissions: Array<String>,
                                            inGrantResults: IntArray) {
        mPermissions!!.ProcessPermissionResponse(inPermissions, inGrantResults)
    }


    abstract fun NavigateToPage(inFragment: Fragment, inPushToBackStack: Boolean, @AnimRes inAnimationIn: Int, @AnimRes inAnimationOut: Int)
}
