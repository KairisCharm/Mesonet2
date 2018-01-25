package org.mesonet.app.forecast;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.OnRebindCallback;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import org.mesonet.app.R;
import org.mesonet.app.databinding.ForecastLayoutBinding;

import java.util.Observable;
import java.util.Observer;


public class ForecastLayout extends RelativeLayout implements Observer
{
    ForecastLayoutBinding mBinding;


    public ForecastLayout(Context context) {
        super(context);
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.forecast_layout, this, true );
        mBinding.setForecastLayout(this);
    }


    public int GetBackgroundColor()
    {
        int result = R.color.colorPrimary;
        if(mBinding != null)
        {
            ForecastData forecastData = mBinding.getForecastData();

            if(forecastData != null)
            {
                ForecastModel.HighOrLow highOrLow = ForecastModel.HighOrLow.valueOf(forecastData.GetHighOrLow());

                if(highOrLow != null)
                {
                    switch (highOrLow)
                    {
                        case High:
                            result = R.color.forecastDayBackground;
                            break;
                        case Low:
                            result = R.color.forecastNightBackground;
                            break;
                    }
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return getResources().getColor(result, getContext().getTheme());

        return getResources().getColor(result);
    }



    public void SetData(ForecastData inForecastData)
    {
        mBinding.setForecastData(inForecastData);
        inForecastData.GetObservable().addObserver(this);

        String url = inForecastData.GetIconUrl();

        if(url != null && !url.isEmpty())
            Picasso.with(getContext()).load(inForecastData.GetIconUrl()).into(mBinding.image);
    }

    @Override
    public void update(Observable observable, Object o) {
        mBinding.invalidateAll();
    }
}
