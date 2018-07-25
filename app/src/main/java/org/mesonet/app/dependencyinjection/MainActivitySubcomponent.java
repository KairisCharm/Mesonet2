package org.mesonet.app.dependencyinjection;

import org.mesonet.app.MainActivity;
import org.mesonet.app.baseclasses.BaseActivity;
import org.mesonet.core.PerActivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;



@PerActivity
@Subcomponent(modules = {MainActivityModule.class})
public interface MainActivitySubcomponent extends AndroidInjector<MainActivity> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MainActivity> {
    }
}