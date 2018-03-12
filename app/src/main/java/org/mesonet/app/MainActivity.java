package org.mesonet.app;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.mesonet.app.advisories.AdvisoriesFragment;
import org.mesonet.app.advisories.AdvisoryDataProvider;
import org.mesonet.app.databinding.MainActivityBinding;
import org.mesonet.app.baseclasses.BaseActivity;
import org.mesonet.app.maps.MapListFragment;
import org.mesonet.app.radar.RadarFragment;
import org.mesonet.app.site.SiteOverviewFragment;
import org.mesonet.app.webview.WebViewActivity;

import java.util.Observer;

import javax.inject.Inject;

import dagger.android.AndroidInjection;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, Toolbar.OnMenuItemClickListener, Observer
{
    private MainActivityBinding mBinding;

    private ActionBarDrawerToggle mActionBarDrawerToggle;


    @Inject
    AdvisoryDataProvider mAdvisoryDataProvider;



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

        mBinding.bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem inItem) {

                Fragment fragment = null;

                switch (inItem.getItemId())
                {
                    case R.id.mesonetOption:
                        fragment = new SiteOverviewFragment();
                        break;

                    case R.id.mapsOption:
                        fragment = new MapListFragment();
                        break;

                    case R.id.radarOption:
                        fragment = new RadarFragment();
                        break;

                    case R.id.advisoriesOption:
                        fragment = new AdvisoriesFragment();
                        break;
                }

                if(fragment != null) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentLayout, fragment);
                    fragmentTransaction.commit();
                }

                return true;
            }
        });

        mBinding.bottomNav.setItemIconTintList(null);

        BottomNavigationMenuView menuView = (BottomNavigationMenuView) mBinding.bottomNav.getChildAt(0);


        for(int i = 0; i < mBinding.bottomNav.getMenu().size(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentLayout, new SiteOverviewFragment());
        fragmentTransaction.commit();

        mAdvisoryDataProvider.addObserver(this);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ticker_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent = new Intent(getBaseContext(), WebViewActivity.class);
        intent.putExtra(WebViewActivity.kTitle, "Ticker");
        intent.putExtra(WebViewActivity.kUrl, "http://www.mesonet.org/index.php/app/ticker_article/latest.ticker.txt");
        startActivity(intent);

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
        Intent intent = new Intent(getBaseContext(), WebViewActivity.class);
        intent.putExtra(WebViewActivity.kTitle, "Ticker");
        intent.putExtra(WebViewActivity.kUrl, "http://www.mesonet.org/index.php/app/ticker_article/latest.ticker.txt");
        startActivity(intent);
        return false;
    }



    public void NavigateToPage(Fragment inFragment, boolean inPushBackStack, @AnimRes int inAnimationIn, @AnimRes int inAnimationOut)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if(inAnimationIn != 0 || inAnimationOut != 0)
            fragmentTransaction.setCustomAnimations(inAnimationIn, inAnimationOut);

        fragmentTransaction.replace(R.id.fragmentLayout, inFragment);

        if(inPushBackStack)
            fragmentTransaction.addToBackStack(inFragment.getClass().getName());
        fragmentTransaction.commit();
    }



    @Override
    public void update(java.util.Observable o, Object arg) {
        if (mAdvisoryDataProvider.GetCount() > 0)
            mBinding.bottomNav.getMenu().getItem(3).setIcon(getResources().getDrawable(R.drawable.advisory_badge, getTheme()));
        else
            mBinding.bottomNav.getMenu().getItem(3).setIcon(R.drawable.advisories_image);
    }
}
