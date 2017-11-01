package org.mesonet.app;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toolbar;

import org.mesonet.app.databinding.MainActivityBinding;
import org.mesonet.app.mesonetdata.MesonetFragment;
import org.mesonet.app.userdata.PreferenceObservable;
import org.mesonet.app.userdata.Preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;



public class MainActivity extends Activity implements Toolbar.OnMenuItemClickListener
{
    private MainActivityBinding mBinding;
    private Preferences mPreferences;
    private PreferenceObservable mPreferenceObservable;
    private Preferences.UnitPreference mUnitPreference = Preferences.UnitPreference.kMetric;


    @Override
    public void onCreate(Bundle inSavedInstanceState)
    {
        super.onCreate(inSavedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.main_activity);

        mBinding.toolBar.inflateMenu(R.menu.main_menu);
        mBinding.toolBar.setOnMenuItemClickListener(this);

        mPreferenceObservable = new PreferenceObservable();

        mPreferences = new Preferences() {
            @Override
            public UnitPreference GetUnitPreference() {
                return mUnitPreference;
            }



            @Override
            public java.util.Observable GetObservable() {
                return mPreferenceObservable;
            }
        };

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragmentLayout,
                MesonetFragment.NewInstance(ControllerCreator.GetInstance().GetMesonetDataController("NRMN", mPreferences)));
        fragmentTransaction.commit();
    }



    @Override
    public void onDestroy()
    {
        mPreferences = null;
        mPreferenceObservable = null;

        super.onDestroy();
    }



    @Override
    public boolean onMenuItemClick(MenuItem inMenuItem)
    {
        switch(inMenuItem.getItemId())
        {
            case R.id.metricUnits:
                mUnitPreference = Preferences.UnitPreference.kMetric;
                mPreferenceObservable.notifyObservers();
                break;
            case R.id.imperialUnits:
                mUnitPreference = Preferences.UnitPreference.kImperial;
                mPreferenceObservable.notifyObservers();
                break;
        }

        return false;
    }
}
