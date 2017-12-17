package org.mesonet.app;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;

import org.mesonet.app.databinding.MainActivityBinding;
//import org.mesonet.app.dependencyinjection.DaggerMainActivityComponent;
import org.mesonet.app.site.SiteOverviewFragment;
import org.mesonet.app.userdata.Preferences;
import org.mesonet.app.userdata.PreferencesObservable;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasFragmentInjector;


public class MainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, Preferences, HasFragmentInjector
{
    private MainActivityBinding mBinding;
    private Preferences.UnitPreference mUnitPreference = Preferences.UnitPreference.kMetric;

    private ActionBarDrawerToggle mActionBarDrawerToggle;

    PreferencesObservable mPreferencesObservable;

    @Inject
    DispatchingAndroidInjector<Fragment> mFragmentInjector;



    @Override
    public void onCreate(Bundle inSavedInstanceState)
    {
        AndroidInjection.inject(this);
        super.onCreate(inSavedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.main_activity);

        setSupportActionBar(mBinding.toolBar);

        ActionBar actionBar = getActionBar();

        if(actionBar != null)
        {
            mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mBinding.drawer, mBinding.toolBar, R.string.app_name, R.string.app_name)
            {
                @Override
                public void onDrawerClosed(View drawerView) {
                    syncState();
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    syncState();
                }
            };

            mActionBarDrawerToggle.setDrawerIndicatorEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);

            mBinding.drawer.addDrawerListener(mActionBarDrawerToggle);
            mActionBarDrawerToggle.syncState();
        }

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragmentLayout, new SiteOverviewFragment());
        fragmentTransaction.commit();
    }



    @Override
    public void onDestroy()
    {
        mBinding.drawer.removeDrawerListener(mActionBarDrawerToggle);

        mActionBarDrawerToggle = null;

        super.onDestroy();
    }



    @Override
    public boolean onMenuItemClick(MenuItem inMenuItem)
    {
        switch(inMenuItem.getItemId())
        {
            case R.id.metricUnits:
                mUnitPreference = Preferences.UnitPreference.kMetric;
                mPreferencesObservable.notifyObservers();
                break;
            case R.id.imperialUnits:
                mUnitPreference = Preferences.UnitPreference.kImperial;
                mPreferencesObservable.notifyObservers();
                break;
        }

        return false;
    }



    @Override
    public UnitPreference GetUnitPreference() {
        return mUnitPreference;
    }



    @Override
    public AndroidInjector<Fragment> fragmentInjector() {
        return mFragmentInjector;
    }
}
