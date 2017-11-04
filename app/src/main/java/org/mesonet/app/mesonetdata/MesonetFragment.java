package org.mesonet.app.mesonetdata;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mesonet.app.R;
import org.mesonet.app.databinding.MesonetFragmentBinding;

import java.util.Observable;
import java.util.Observer;


public class MesonetFragment extends Fragment
{
    private MesonetDataController mDataController;
    private MesonetFragmentBinding mBinding;
    private Observer mObserver;



    public static MesonetFragment NewInstance(MesonetDataController inController)
    {
        MesonetFragment fragment = new MesonetFragment();
        fragment.mDataController = inController;

        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inInflater, ViewGroup inParent, Bundle inSavedInstanceState)
    {
        mBinding = DataBindingUtil.inflate(inInflater, R.layout.mesonet_fragment, inParent, false);

        mObserver = new Observer() {
            @Override
            public void update(Observable observable, Object o) {
                // not using databinding here because AutoSizing TextViews don't work with databound text
//                mBinding.currentTair.setText(mBinding.getUiController().GetAirTempString());
//                mBinding.readingTime.setText(mBinding.getUiController().GetTimeString());
                mBinding.invalidateAll();
            }
        };

        mDataController.addObserver(mObserver);
        mBinding.setUiController(new MesonetUIController(mDataController));

        return mBinding.getRoot();
    }



    @Override
    public void onResume()
    {
        super.onResume();

        mDataController.StartUpdating();
    }



    @Override
    public void onPause()
    {
        mDataController.StopUpdating();

        super.onPause();
    }



    @Override
    public void onDestroyView()
    {
        mObserver = null;
        super.onDestroyView();
    }
}
