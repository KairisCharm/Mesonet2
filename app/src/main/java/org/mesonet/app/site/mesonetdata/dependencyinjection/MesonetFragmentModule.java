package org.mesonet.app.site.mesonetdata.dependencyinjection;

import android.support.v4.app.Fragment;

import org.mesonet.app.dependencyinjection.PerChildFragment;
import org.mesonet.app.site.mesonetdata.MesonetFragment;

import dagger.Binds;
import dagger.Module;



@Module
public abstract class MesonetFragmentModule
{
    @Binds
    @PerChildFragment
    abstract Fragment fragment(MesonetFragment inMesonetFragment);
}
