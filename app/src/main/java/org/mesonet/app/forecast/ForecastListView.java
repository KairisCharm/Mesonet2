package org.mesonet.app.forecast;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import org.mesonet.app.R;
import org.mesonet.app.databinding.ForecastFragmentBinding;



public class ForecastListView extends LinearLayout {
    ForecastFragmentBinding mBinding;

    public ForecastListView(Context context) {
        super(context);
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.forecast_fragment, this, true );

        int totalForecastsCount = getResources().getInteger(R.integer.forecastsPerPage);

        for(int i = 0; i < totalForecastsCount; i++)
        {
            ForecastLayout forecastLayout = new ForecastLayout(getContext());
            mBinding.layout.addView(forecastLayout);
            forecastLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
        }
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
