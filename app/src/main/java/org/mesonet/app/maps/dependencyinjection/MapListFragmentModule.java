package org.mesonet.app.maps.dependencyinjection;

import android.support.v4.app.Fragment;

import org.mesonet.app.maps.MapListFragment;
import org.mesonet.core.PerFragment;

import dagger.Binds;
import dagger.Module;


@Module
abstract class MapListFragmentModule
{
    @Binds
    @PerFragment
    abstract Fragment Fragment(MapListFragment inMapsListFragment);
}
