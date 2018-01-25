package org.mesonet.app.site.mesonetdata.dependencyinjection;

import org.mesonet.app.dependencyinjection.PerChildFragment;
import org.mesonet.app.site.mesonetdata.MesonetFragment;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;



@PerChildFragment
@Subcomponent(modules = MesonetFragmentModule.class)
public interface MesonetFragmentSubcomponent extends AndroidInjector<MesonetFragment> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MesonetFragment> {
    }
}