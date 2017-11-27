package org.mesonet.app;

import android.app.FragmentTransaction;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;

import org.mesonet.app.databinding.MainActivityBinding;
import org.mesonet.app.site.SiteOverviewFragment;
import org.mesonet.app.userdata.PreferenceObservable;
import org.mesonet.app.userdata.Preferences;


public class MainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener
{
    private MainActivityBinding mBinding;
    private Preferences mPreferences;
    private PreferenceObservable mPreferenceObservable;
    private Preferences.UnitPreference mUnitPreference = Preferences.UnitPreference.kMetric;

    private ActionBarDrawerToggle mActionBarDrawerToggle;


    @Override
    public void onCreate(Bundle inSavedInstanceState)
    {
        super.onCreate(inSavedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.main_activity);

        setSupportActionBar(mBinding.toolBar);

        ActionBar actionBar = getSupportActionBar();

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

        mPreferenceObservable = new PreferenceObservable();

        mPreferences = new Preferences() {
            @Override
            public UnitPreference GetUnitPreference() {
                return mUnitPreference;
            }



            @Override
            public java.util.Observable GetObservable() {
                return mPreferenceObservable;
            }
        };

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragmentLayout,
                SiteOverviewFragment.NewInstance(ControllerCreator.GetInstance().GetMesonetSiteDataController(), mPreferences));
        fragmentTransaction.commit();
    }



    @Override
    public void onDestroy()
    {
        mBinding.drawer.removeDrawerListener(mActionBarDrawerToggle);

        mPreferences = null;
        mPreferenceObservable = null;
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
                mPreferenceObservable.notifyObservers();
                break;
            case R.id.imperialUnits:
                mUnitPreference = Preferences.UnitPreference.kImperial;
                mPreferenceObservable.notifyObservers();
                break;
        }

        return false;
    }
}
