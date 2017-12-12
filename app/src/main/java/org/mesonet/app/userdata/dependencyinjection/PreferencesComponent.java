package org.mesonet.app.userdata.dependencyinjection;

import org.mesonet.app.MainActivity;
import org.mesonet.app.mesonetdata.MesonetDataController;

import dagger.Subcomponent;



@Subcomponent(modules = MainActivity.class)
public interface PreferencesComponent
{
    void Inject(MainActivity inActivity);
    void Inject(MesonetDataController inMesonetDataController);
}
