package org.mesonet.app.site;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

import org.mesonet.app.ControllerCreator;
import org.mesonet.app.R;
import org.mesonet.app.databinding.SiteOverviewFragmentBinding;
import org.mesonet.app.filterlist.FilterListFragment;
import org.mesonet.app.mesonetdata.MesonetDataController;
import org.mesonet.app.mesonetdata.MesonetFragment;
import org.mesonet.app.mesonetdata.MesonetSiteDataController;
import org.mesonet.app.mesonetdata.SiteSelectionInterfaces;
import org.mesonet.app.userdata.Preferences;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;


public class SiteOverviewFragment extends Fragment implements SiteSelectionInterfaces.SelectSiteController, FilterListFragment.FilterListCloser, Observer
{
    private SiteOverviewFragmentBinding mBinding;
    private Preferences mPreferences;
    private MesonetSiteDataController mSiteDataController;



    public static SiteOverviewFragment NewInstance(MesonetSiteDataController inSiteDataController, Preferences inPreferences)
    {
        SiteOverviewFragment fragment = new SiteOverviewFragment();
        fragment.mPreferences = inPreferences;
        fragment.mSiteDataController = inSiteDataController;

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inInflater, ViewGroup inParent, Bundle inSavedInstanceState)
    {
        mBinding = DataBindingUtil.inflate(inInflater, R.layout.site_overview_fragment, inParent, false);

        MesonetDataController mesonetDataController = ControllerCreator.GetInstance().GetMesonetDataController("NRMN", mPreferences, this);

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.mesonetDetailContainer, MesonetFragment.NewInstance(mesonetDataController));
        transaction.add(R.id.siteSelection, FilterListFragment.NewInstance(mesonetDataController, mSiteDataController.GetSiteList(), this));
        transaction.commit();

        if(mSiteDataController != null)
            mSiteDataController.addObserver(this);

        mBinding.siteListFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RevealView(mBinding.siteListFab);
            }
        });

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBinding.getRoot().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    v.removeOnLayoutChangeListener(this);
                    RevealView(mBinding.siteListFab);
                }
            });
        }

        return mBinding.getRoot();
    }



    private void RevealView(View view)
    {
        final int cx = (view.getLeft() + view.getRight()) / 2;
        final int cy = (view.getTop() + view.getBottom()) / 2;
        final float radius = Math.max(mBinding.siteSelection.getWidth(), mBinding.siteSelection.getHeight()) * 2.0f;

        if (mBinding.siteSelection.getVisibility() == View.INVISIBLE) {

            mBinding.siteListFab.hide(new FloatingActionButton.OnVisibilityChangedListener() {
                @Override
                public void onHidden(FloatingActionButton fab) {
                    super.onHidden(fab);
                    mBinding.siteSelection.setVisibility(View.VISIBLE);
                    final Animator hide = ViewAnimationUtils.createCircularReveal(mBinding.siteSelection, cx, cy, 0, radius);
                    hide.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mBinding.siteSelection.setVisibility(View.VISIBLE);
                            hide.removeListener(this);
                        }
                    });
                    hide.start();
                }
            });
        } else {
            final Animator reveal = ViewAnimationUtils.createCircularReveal(
                    mBinding.siteSelection, cx, cy, radius, 0);
            reveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mBinding.siteSelection.setVisibility(View.INVISIBLE);
                    reveal.removeListener(this);
                    mBinding.siteListFab.show();
                }
            });
            reveal.start();
        }
    }



    @Override
    public void StartSelection(SiteSelectionInterfaces.SelectSiteListener inListener, Map<String, String> inKeysToNames) {

    }

    @Override
    public void update(Observable observable, Object o)
    {

    }



    private void PopulateSiteList()
    {

    }

    @Override
    public void Close() {
        RevealView(mBinding.siteListFab);
    }
}
