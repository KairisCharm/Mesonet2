package org.mesonet.app.filterlist.dependencyinjection;

import android.location.Location;
import android.support.v4.app.Fragment;
import android.util.Pair;

import org.mesonet.app.dependencyinjection.PerChildFragment;
import org.mesonet.app.filterlist.FilterListFragment;

import java.util.Map;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;

@Module
abstract class FilterListFragmentModule
{
    @Binds
    @PerChildFragment
    abstract Fragment fragment(FilterListFragment inFilterListFragment);
}