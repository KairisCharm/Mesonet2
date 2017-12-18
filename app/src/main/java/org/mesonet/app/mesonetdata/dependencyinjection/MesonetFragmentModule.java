package org.mesonet.app.mesonetdata.dependencyinjection;

import android.app.Fragment;

import org.mesonet.app.dependencyinjection.PerChildFragment;
import org.mesonet.app.dependencyinjection.PerFragment;
import org.mesonet.app.filterlist.FilterListFragment;
import org.mesonet.app.mesonetdata.MesonetFragment;

import dagger.Binds;
import dagger.Module;



@Module
public abstract class MesonetFragmentModule
{
    @Binds
    @PerChildFragment
    abstract Fragment fragment(MesonetFragment inMesonetFragment);
}
