package org.mesonet.app.dependencyinjection;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toolbar;

import org.mesonet.app.userdata.Preferences;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasFragmentInjector;



public abstract class BaseActivity extends AppCompatActivity  implements HasFragmentInjector
{
    @Inject
    DispatchingAndroidInjector<Fragment> mFragmentInjector;



    @Override
    public AndroidInjector<Fragment> fragmentInjector() {
        return mFragmentInjector;
    }
}
