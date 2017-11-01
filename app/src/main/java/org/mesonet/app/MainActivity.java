package org.mesonet.app;


import android.app.Activity;
import android.app.FragmentTransaction;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import org.mesonet.app.mesonetdata.MesonetFragment;
import org.mesonet.app.userdata.Preferences;



public class MainActivity extends Activity implements Preferences
{
    @Override
    public void onCreate(Bundle inSavedInstanceState)
    {
        super.onCreate(inSavedInstanceState);

        DataBindingUtil.setContentView(this, R.layout.main_activity_binding);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragmentLayout,
                                MesonetFragment.NewInstance(ControllerCreator.GetInstance().GetMesonetDataController("NRMN", this)));
        fragmentTransaction.commit();
    }



    @Override
    public UnitPreference GetUnitPreference ()
    {
        return UnitPreference.kMetric;
    }
}
