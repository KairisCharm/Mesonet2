package org.mesonet.app.dependencyinjection;

import android.app.Fragment;

import org.mesonet.app.filterlist.FilterListFragment;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;

@Module
abstract class FilterListFragmentModule {

    @Binds
    @Named("FilterListFragment")
    @PerChildFragment
    abstract Fragment fragment(FilterListFragment inFilterListFragment);
}