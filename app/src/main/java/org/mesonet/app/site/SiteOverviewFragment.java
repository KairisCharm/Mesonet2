package org.mesonet.app.site;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;

import org.mesonet.app.R;
import org.mesonet.app.forecast.FiveDayForecastDataController;
import org.mesonet.app.forecast.ForecastListView;
import org.mesonet.app.webview.WebViewFragment;
import org.mesonet.app.databinding.SiteOverviewFragmentBinding;
import org.mesonet.app.baseclasses.BaseFragment;
import org.mesonet.app.filterlist.FilterListFragment;
//import org.mesonet.app.filterlist.dependencyinjection.DaggerFilterListComponent;
import org.mesonet.app.site.mesonetdata.MesonetFragment;
import org.mesonet.app.site.mesonetdata.MesonetSiteDataController;
//import org.mesonet.app.site.mesonetdata.dependencyinjection.DaggerMesonetDataComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.inject.Inject;


public class SiteOverviewFragment extends BaseFragment implements FilterListFragment.FilterListCloser, WebViewFragment.WebViewInformation, Toolbar.OnMenuItemClickListener, Observer
{
    private SiteOverviewFragmentBinding mBinding;

    @Inject
    MesonetSiteDataController mMesonetSiteDataController;

    @Inject
    FiveDayForecastDataController mFiveDayForecastDataController;



    @Override
    public View onCreateView(LayoutInflater inInflater, ViewGroup inParent, Bundle inSavedInstanceState)
    {
        mBinding = DataBindingUtil.inflate(inInflater, R.layout.site_overview_fragment, inParent, false);

        mBinding.setMesonetSiteDataController(mMesonetSiteDataController);

        mMesonetSiteDataController.GetDataObservable().addObserver(this);
        mFiveDayForecastDataController.GetObservable().addObserver(this);

        mBinding.siteToolbar.inflateMenu(R.menu.mesonet_site_menu);
        mBinding.siteToolbar.setOnMenuItemClickListener(this);
        mBinding.siteToolbar.setNavigationIcon(R.drawable.ic_multiline_chart_white_36dp);
        mBinding.siteToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebViewFragment webViewFragment = new WebViewFragment();
                webViewFragment.SetInfo(SiteOverviewFragment.this);
                webViewFragment.show(getChildFragmentManager(), "MeteogramWebview");
            }
        });

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.mesonetDetailContainer, new MesonetFragment());
        transaction.commit();

        Update();

        mBinding.previousFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentSelected = mBinding.forecastViewPager.getCurrentItem();

                if(currentSelected > 0)
                    mBinding.forecastViewPager.setCurrentItem(currentSelected - 1);
            }
        });

        mBinding.nextFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentSelected = mBinding.forecastViewPager.getCurrentItem();

                if(currentSelected < (mBinding.forecastViewPager.getChildCount() - 1))
                    mBinding.forecastViewPager.setCurrentItem(currentSelected + 1);
            }
        });

        mBinding.forecastViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position > 0)
                    mBinding.previousFab.show();
                else
                    mBinding.previousFab.hide();

                if(position < (mBinding.forecastViewPager.getChildCount() - 1))
                    mBinding.nextFab.show();
                else
                    mBinding.nextFab.hide();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return mBinding.getRoot();
    }



    private void RevealView(View view)
    {
        final int cx = (view.getLeft() + view.getRight()) - 24;
        final int cy = (view.getTop() + view.getBottom()) / 2;
        final float radius = Math.max(mBinding.childFragmentContainer.getWidth(), mBinding.childFragmentContainer.getHeight()) * 2.0f;

        if (mBinding.childFragmentContainer.getVisibility() == View.INVISIBLE) {


            mBinding.childFragmentContainer.setVisibility(View.VISIBLE);
            final Animator hide = ViewAnimationUtils.createCircularReveal(mBinding.childFragmentContainer, cx, cy, 0, radius);
            hide.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mBinding.childFragmentContainer.setVisibility(View.VISIBLE);
                    hide.removeListener(this);
                }
            });
            hide.start();

        } else {
            final Animator reveal = ViewAnimationUtils.createCircularReveal(
                    mBinding.childFragmentContainer, cx, cy, radius, 0);
            reveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mBinding.childFragmentContainer.setVisibility(View.INVISIBLE);
                    reveal.removeListener(this);
                }
            });
            reveal.start();
        }
    }

    @Override
    public void Close() {
        RevealView(mBinding.siteToolbar);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId())
        {
            case R.id.openList:
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.childFragmentContainer, new FilterListFragment());
                transaction.commit();
                RevealView(mBinding.siteToolbar);
                break;
            case R.id.favorite:
                ToggleFavorite();
                break;
        }
        return false;
    }


    private void ToggleFavorite()
    {
        if(mMesonetSiteDataController.IsFavorite(mMesonetSiteDataController.CurrentSelection()))
        {
            mMesonetSiteDataController.RemoveFavorite(mMesonetSiteDataController.CurrentSelection());
        }
        else
        {
            mMesonetSiteDataController.AddFavorite(mMesonetSiteDataController.CurrentSelection());
        }
    }

    @Override
    public String Url() {
        return "http://www.mesonet.org/data/public/mesonet/meteograms/" + mMesonetSiteDataController.CurrentSelection().toUpperCase() + ".met.gif";
    }

    @Override
    public String Title() {
        return mMesonetSiteDataController.CurrentStationName() + " Meteogram";
    }



    @Override
    public void update(Observable observable, Object o) {
        Update();
    }



    private void Update()
    {
        mBinding.setMesonetSiteDataController(mMesonetSiteDataController);

        if(mMesonetSiteDataController.IsFavorite(mMesonetSiteDataController.CurrentSelection())) {
            mBinding.siteToolbar.getMenu().findItem(R.id.favorite).setIcon(R.drawable.ic_favorite_white_36dp);
        }
        else
        {
            mBinding.siteToolbar.getMenu().findItem(R.id.favorite).setIcon(R.drawable.ic_favorite_border_white_36dp);
        }

        mBinding.forecastViewPager.removeAllViews();

        int forecastsPerPage = getResources().getInteger(R.integer.forecastsPerPage);
        final int pageCount = mFiveDayForecastDataController.GetCount() / forecastsPerPage;

        final List<ForecastListView> forecastPages = new ArrayList<>();
        for(int i = 0; i < mFiveDayForecastDataController.GetCount(); i += forecastsPerPage)
        {
            ForecastListView forecastListView = new ForecastListView(getContext());

            for(int j = 0; j < forecastsPerPage; j++)
            {
                forecastListView.SetSemiDayForecast(j, mFiveDayForecastDataController.GetForecast(i + j));
            }

            forecastPages.add(forecastListView);
        }

        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return pageCount;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view.equals(object);
            }

            @Override
            public View instantiateItem(ViewGroup inViewGroup, int inPosition) {
                if(forecastPages.get(inPosition).getParent() != null)
                    ((ViewGroup)forecastPages.get(inPosition).getParent()).removeView(forecastPages.get(inPosition));
                inViewGroup.addView(forecastPages.get(inPosition));
                return forecastPages.get(inPosition);
            }

            @Override
            public void destroyItem(ViewGroup inParent, int inPosition, Object inObject){

            }
        };

        mBinding.forecastViewPager.setAdapter(pagerAdapter);
        pagerAdapter.notifyDataSetChanged();

        mBinding.invalidateAll();
    }
}