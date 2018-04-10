package org.mesonet.app.maps.dependencyinjection;

import android.support.v4.app.Fragment;

import org.mesonet.app.dependencyinjection.PerFragment;
import org.mesonet.app.maps.MapListFragment;

import dagger.Binds;
import dagger.Module;


@Module
abstract class MapListFragmentModule
{
    @Binds
    @PerFragment
    abstract Fragment Fragment(MapListFragment inMapsListFragment);
}
