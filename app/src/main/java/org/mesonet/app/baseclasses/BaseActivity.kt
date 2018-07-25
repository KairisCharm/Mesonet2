package org.mesonet.app.baseclasses

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.annotation.AnimRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import dagger.android.AndroidInjection

import javax.inject.Inject

import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import org.mesonet.androidsystem.Permissions
import org.mesonet.dataprocessing.LocationProvider


abstract class BaseActivity : AppCompatActivity(), HasSupportFragmentInjector {


    @Inject
    lateinit var mFragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var mPermissions: Permissions



    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return mFragmentInjector
    }


    override fun onRequestPermissionsResult(inRequestCode: Int,
                                            inPermissions: Array<String>,
                                            inGrantResults: IntArray) {
        mPermissions.ProcessPermissionResponse(inPermissions, inGrantResults)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)
    }


    abstract fun NavigateToPage(inFragment: Fragment, inPushToBackStack: Boolean, @AnimRes inAnimationIn: Int, @AnimRes inAnimationOut: Int)
}