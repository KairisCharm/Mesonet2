package org.mesonet.app.filterlist.dependencyinjection;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

import org.mesonet.app.dependencyinjection.PerChildFragment;
import org.mesonet.app.filterlist.FilterListFragment;


@PerChildFragment
@Subcomponent(modules = FilterListFragmentModule.class)
public interface FilterListFragmentSubcomponent extends AndroidInjector<FilterListFragment> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<FilterListFragment> {
    }
}