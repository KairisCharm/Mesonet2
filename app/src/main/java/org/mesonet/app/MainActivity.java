package org.mesonet.app;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.mesonet.app.androidsystem.UserSettings;
import org.mesonet.app.databinding.MainActivityBinding;
//import org.mesonet.app.dependencyinjection.DaggerMainActivityComponent;
import org.mesonet.app.baseclasses.BaseActivity;
import org.mesonet.app.site.SiteOverviewFragment;
import org.mesonet.app.userdata.Preferences;
import org.mesonet.app.userdata.PreferencesObservable;
import org.mesonet.app.webview.WebViewFragment;

import javax.inject.Inject;

import dagger.android.AndroidInjection;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, Toolbar.OnMenuItemClickListener
{
    private MainActivityBinding mBinding;

    private ActionBarDrawerToggle mActionBarDrawerToggle;




    @Inject
    public MainActivity(){}



    @Override
    public void onCreate(Bundle inSavedInstanceState)
    {
        AndroidInjection.inject(this);
        super.onCreate(inSavedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.main_activity);

        mBinding.toolBar.inflateMenu(R.menu.ticker_menu);
        mBinding.toolBar.setOnMenuItemClickListener(this);

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
            mBinding.drawerNavView.setNavigationItemSelectedListener(this);
            mActionBarDrawerToggle.syncState();
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragmentLayout, new SiteOverviewFragment());
        fragmentTransaction.commit();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ticker_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        WebViewFragment webViewFragment = new WebViewFragment();
        webViewFragment.SetInfo(new WebViewFragment.WebViewInformation() {
            @Override
            public String Url() {
                return "http://www.mesonet.org/index.php/app/ticker_article/latest.ticker.txt";
            }

            @Override
            public String Title() {
                return "Ticker";
            }
        });
        webViewFragment.show(getSupportFragmentManager(), "TickerWebview");
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onDestroy()
    {
        mBinding.drawer.removeDrawerListener(mActionBarDrawerToggle);

        mActionBarDrawerToggle = null;

        super.onDestroy();
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem inMenuItem) {
        switch(inMenuItem.getItemId())
        {
//            case R.id.metricUnits:
//                mUserSettings.SetPreference(this, UserSettings.kUnitPreference, Preferences.UnitPreference.kMetric.name());
//                mPreferencesObservable.notifyObservers();
//                break;
//            case R.id.imperialUnits:
//                mUserSettings.SetPreference(this, UserSettings.kUnitPreference, Preferences.UnitPreference.kImperial.name());
//                mPreferencesObservable.notifyObservers();
//                break;
        }
        return false;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        WebViewFragment webViewFragment = new WebViewFragment();
        webViewFragment.SetInfo(new WebViewFragment.WebViewInformation() {
            @Override
            public String Url() {
                return "http://www.mesonet.org/index.php/app/ticker_article/latest.ticker.txt";
            }

            @Override
            public String Title() {
                return "Ticker";
            }
        });
        webViewFragment.show(getSupportFragmentManager(), "TickerWebview");
        return false;
    }
}
