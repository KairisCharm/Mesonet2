package org.mesonet.app.forecast;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mesonet.app.R;
import org.mesonet.app.baseclasses.BaseFragment;
import org.mesonet.app.databinding.ForecastFragmentBinding;

import javax.inject.Inject;


public class ForecastFragment extends BaseFragment
{
    ForecastFragmentBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inInflater, ViewGroup inParent, Bundle inSavedInstanceState)
    {
        mBinding = DataBindingUtil.inflate(inInflater, R.layout.forecast_fragment, inParent, true);

        int forecastsPerPage = getResources().getInteger(R.integer.forecastsPerPage);

        for(int i = 0; i < forecastsPerPage; i++)
        {
            ForecastLayout forecastLayout = new ForecastLayout(getContext());
            mBinding.layout.addView(forecastLayout);
        }

        return mBinding.getRoot();
    }



    public void SetSemiDayForecast(int inForecastIndex, SemiDayForecastDataController inController)
    {
        if(mBinding != null)
        {
            if(inForecastIndex < mBinding.layout.getChildCount())
            {
                ((ForecastLayout)mBinding.layout.getChildAt(inForecastIndex)).SetData(inController);
            }
        }
    }
}
