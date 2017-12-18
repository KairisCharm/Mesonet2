package org.mesonet.app.dependencyinjection;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasFragmentInjector;


public abstract class BaseFragment extends Fragment implements HasFragmentInjector
{
    @Inject
    DispatchingAndroidInjector<Fragment> mChildFragmentInjector;

    @Override
    public void onAttach(Context inContext)
    {
        AndroidInjection.inject(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            super.onAttach(inContext);
    }



    @Override
    public void onAttach(Activity inActivity)
    {
        AndroidInjection.inject(this);
        super.onAttach(inActivity);
    }



    @Override
    public AndroidInjector<Fragment> fragmentInjector()
    {
        return mChildFragmentInjector;
    }
}
