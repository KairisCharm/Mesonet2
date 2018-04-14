package org.mesonet.app.filterlist.dependencyinjection;

import android.support.v4.app.Fragment;

import org.mesonet.app.dependencyinjection.PerChildFragment;
import org.mesonet.app.filterlist.FilterListFragment;

import dagger.Binds;
import dagger.Module;

@Module
abstract class FilterListFragmentModule
{
    @Binds
    @PerChildFragment
    abstract Fragment fragment(FilterListFragment inFilterListFragment);
}