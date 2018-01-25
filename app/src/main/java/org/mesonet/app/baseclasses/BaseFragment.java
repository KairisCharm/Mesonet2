package org.mesonet.app.baseclasses;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasFragmentInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;


public abstract class BaseFragment extends Fragment implements HasSupportFragmentInjector{
    @Inject
    DispatchingAndroidInjector<Fragment> mChildFragmentInjector;

    @Override
    public void onAttach(Context inContext)
    {
        AndroidSupportInjection.inject(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            super.onAttach(inContext);
    }



    @Override
    public void onAttach(Activity inActivity)
    {
        AndroidSupportInjection.inject(this);
        super.onAttach(inActivity);
    }


    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return mChildFragmentInjector;
    }
}
