package org.mesonet.app.dependencyinjection;

import org.mesonet.app.MainActivity;
import org.mesonet.core.PerContext;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;



@PerContext
@Subcomponent(modules = {MainActivityModule.class})
public interface MainActivitySubcomponent extends AndroidInjector<MainActivity> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MainActivity> {
    }
}