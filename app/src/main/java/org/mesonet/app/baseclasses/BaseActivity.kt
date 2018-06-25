package org.mesonet.app.baseclasses

import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.AnimRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import dagger.android.AndroidInjection

import javax.inject.Inject

import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import org.mesonet.androidsystem.Permissions
import org.mesonet.core.ThreadHandler


abstract class BaseActivity : AppCompatActivity(), HasSupportFragmentInjector {
    @Inject
    internal lateinit var mFragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    internal lateinit var mPermissions: Permissions

    @Inject
    internal lateinit var mThreadHandler: ThreadHandler


    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return mFragmentInjector
    }


    override fun onRequestPermissionsResult(inRequestCode: Int,
                                            inPermissions: Array<String>,
                                            inGrantResults: IntArray) {
        mPermissions.ProcessPermissionResponse(inPermissions, inGrantResults)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AndroidInjection.inject(this)
    }


    override fun onDestroy() {
        mThreadHandler.CloseThreads()
        super.onDestroy()
    }


    abstract fun NavigateToPage(inFragment: Fragment, inPushToBackStack: Boolean, @AnimRes inAnimationIn: Int, @AnimRes inAnimationOut: Int)
}