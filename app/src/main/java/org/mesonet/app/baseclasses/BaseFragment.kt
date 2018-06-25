package org.mesonet.app.baseclasses

import android.app.Activity
import android.content.Context
import android.os.Build
import android.support.v4.app.Fragment

import javax.inject.Inject

import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector


abstract class BaseFragment : Fragment(), HasSupportFragmentInjector {
    @Inject
    internal lateinit var mChildFragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onAttach(inContext: Context?) {
        AndroidSupportInjection.inject(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            super.onAttach(inContext)
    }


    override fun onAttach(inActivity: Activity?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(inActivity)
    }


    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return mChildFragmentInjector
    }
}