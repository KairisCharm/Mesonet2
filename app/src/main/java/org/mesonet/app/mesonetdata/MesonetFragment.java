package org.mesonet.app.mesonetdata;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mesonet.app.R;
import org.mesonet.app.databinding.MesonetFragmentBinding;



public class MesonetFragment extends Fragment
{
    private MesonetDataController mDataController;
    private MesonetFragmentBinding mBinding;



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

        mBinding.setUiController(new MesonetUIController(mDataController));

        return mBinding.getRoot();
    }
}
