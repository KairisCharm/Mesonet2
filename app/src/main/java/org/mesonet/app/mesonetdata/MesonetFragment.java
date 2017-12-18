package org.mesonet.app.mesonetdata;

import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mesonet.app.R;
import org.mesonet.app.databinding.MesonetFragmentBinding;
import org.mesonet.app.dependencyinjection.BaseFragment;

import java.util.Observable;
import java.util.Observer;

import javax.inject.Inject;


public class MesonetFragment extends BaseFragment
{
    @Inject
    MesonetDataController mDataController;

    @Inject
    MesonetUIController mUIController;



    private MesonetFragmentBinding mBinding;
    Observer mObserver;



    @Override
    public View onCreateView(LayoutInflater inInflater, ViewGroup inParent, Bundle inSavedInstanceState)
    {
        mBinding = DataBindingUtil.inflate(inInflater, R.layout.mesonet_fragment, inParent, false);

        mObserver = new Observer() {
            @Override
            public void update(Observable observable, Object o) {
                mBinding.invalidateAll();
            }
        };

        mDataController.addObserver(mObserver);
        mBinding.setUiController(mUIController);

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
