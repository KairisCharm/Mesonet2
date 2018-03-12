package org.mesonet.app.radar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import org.mesonet.app.R;
import org.mesonet.app.baseclasses.BaseFragment;
import org.mesonet.app.databinding.RadarFragmentBinding;
import org.mesonet.app.filterlist.FilterListFragment;

import java.util.Observable;
import java.util.Observer;

import javax.inject.Inject;


public class RadarFragment extends BaseFragment implements GoogleMapController.GoogleMapProvider, FilterListFragment.FilterListCloser
{
    @Inject
    GoogleMapController mMapController;


    RadarFragmentBinding mBinding;

    private Snackbar mSnackbar;



    private BindingReadyListener mBindingReadyListener;



    @Override
    public void onCreate(Bundle inSavedInstanceState)
    {
        super.onCreate(inSavedInstanceState);

        mMapController.SetProvider(this);
    }



    @Override
    public void onActivityCreated(Bundle inSavedInstanceState)
    {
        super.onActivityCreated(inSavedInstanceState);

        mBinding.map.onCreate(inSavedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inInflater, ViewGroup inContainer, Bundle inSavedInstanceState)
    {
        mBinding = DataBindingUtil.inflate(inInflater, R.layout.radar_fragment, inContainer, false);

        if(mBindingReadyListener != null)
            mBindingReadyListener.BindingReady(mBinding);

        mBindingReadyListener = null;

        mSnackbar = Snackbar.make(mBinding.radarLayout, "", Snackbar.LENGTH_INDEFINITE).setAction("Change", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.childFragmentContainer, new FilterListFragment());
                transaction.commit();
                RevealView(mBinding.radarLayout);
                mBinding.playPauseButton.hide();
            }
        });

        mBinding.playPauseButton.SetPlayPauseState(mMapController.GetPlayPauseState());
        mBinding.playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMapController.TogglePlay();
            }
        });

        mBinding.transparencySeekBar.setMax(255);
        mBinding.transparencySeekBar.setProgress(Math.round(255.0f * (1 - mMapController.GetTransparency())));
        mBinding.transparencySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar inSeekBar, int inProgress, boolean inFromUser) {
                mMapController.SetTransparency((1 - (inProgress / 255.0f)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSnackbar.show();

        return mBinding.getRoot();
    }



    @Override
    public void onResume()
    {
        super.onResume();

        mBinding.map.onResume();
    }



    @Override
    public void onPause()
    {
        mBinding.map.onPause();

        super.onPause();
    }



    @Override
    public void onDestroy()
    {
        mMapController.SetProvider(null);
        mBinding.map.onDestroy();

        super.onDestroy();
    }



    @Override
    public void onSaveInstanceState(Bundle inSaveInstanceState)
    {
        super.onSaveInstanceState(inSaveInstanceState);

        mBinding.map.onSaveInstanceState(inSaveInstanceState);
    }



    @Override
    public void onLowMemory()
    {
        super.onLowMemory();

        mBinding.map.onLowMemory();
    }



    @Override
    public void GetMap(final GoogleMapListener inListener)
    {
        BindingReadyListener bindingReadyListener = new BindingReadyListener() {
            @Override
            public void BindingReady(RadarFragmentBinding inBinding) {
                if(inListener != null && inBinding != null) {
                    inBinding.map.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap inMap) {
                            inListener.MapFound(getContext(), inMap);
                        }
                    });
                }
            }
        };

        if(mBinding == null)
            mBindingReadyListener = bindingReadyListener;

        else
            bindingReadyListener.BindingReady(mBinding);
    }



    @Override
    public void SetTime(String inTimeString)
    {
        if(mSnackbar != null) {
            String timeString = "";

            if(inTimeString != null && !inTimeString.isEmpty()) {
                timeString = "\n" + inTimeString;
                mBinding.playPauseButton.show();
            }
            else
                mBinding.playPauseButton.hide();

            mSnackbar.setText(mMapController.GetRadarName() + timeString);
        }
    }

    @Override
    public void Close() {
        RevealView(mBinding.radarLayout);
        mSnackbar.show();
        mBinding.playPauseButton.show();
    }



    private void RevealView(View view)
    {
        final int cx = (view.getLeft() + view.getRight()) - 24;
        final int cy = (view.getTop() + view.getBottom()) / 2;
        final float radius = Math.max(mBinding.childFragmentContainer.getWidth(), mBinding.childFragmentContainer.getHeight()) * 2.0f;

        if (mBinding.childFragmentContainer.getVisibility() != View.VISIBLE) {
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


    private interface BindingReadyListener
    {
        void BindingReady(RadarFragmentBinding inBinding);
    }
}
