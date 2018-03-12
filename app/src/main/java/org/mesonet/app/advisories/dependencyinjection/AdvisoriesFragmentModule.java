package org.mesonet.app.advisories.dependencyinjection;


import android.support.v4.app.Fragment;

import org.mesonet.app.advisories.AdvisoriesFragment;
import org.mesonet.app.dependencyinjection.PerFragment;
import org.mesonet.app.maps.MapListFragment;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class AdvisoriesFragmentModule
{
    @Binds
    @PerFragment
    abstract Fragment Fragment(AdvisoriesFragment inAdvisoriesFragment);
}
