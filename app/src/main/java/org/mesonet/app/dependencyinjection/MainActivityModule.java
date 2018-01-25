package org.mesonet.app.dependencyinjection;

import android.app.Activity;
import android.support.v4.app.Fragment;

import org.mesonet.app.MainActivity;
import org.mesonet.app.baseclasses.BaseActivity;
import org.mesonet.app.site.SiteOverviewFragment;
import org.mesonet.app.site.dependencyinjection.SiteOverviewFragmentSubcomponent;
import org.mesonet.app.site.mesonetdata.dependencyinjection.MesonetSiteDataControllerSubcomponent;
import org.mesonet.app.userdata.Preferences;
import org.mesonet.app.userdata.PreferencesObservable;
import org.mesonet.app.webview.WebViewFragment;
import org.mesonet.app.webview.dependencyinjection.WebViewFragmentSubcomponent;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.AndroidInjector;
import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;



@Module(subcomponents = {SiteOverviewFragmentSubcomponent.class,
        MesonetSiteDataControllerSubcomponent.class})
public abstract class MainActivityModule
{
    @Binds
    @IntoMap
    @FragmentKey(SiteOverviewFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment>
    SiteOverviewFragmentInjectorFactory(SiteOverviewFragmentSubcomponent.Builder inBuilder);

    @Binds
    @PerActivity
    abstract Activity Activity(MainActivity inMainActivity);


    @Binds
    @PerActivity
    abstract BaseActivity BaseActivity(MainActivity inMainActivity);


    @Provides
    @PerActivity
    static Preferences Preferences(BaseActivity inActivity)
    {
        return inActivity;
    }
}