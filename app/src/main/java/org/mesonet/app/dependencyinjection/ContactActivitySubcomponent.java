package org.mesonet.app.dependencyinjection;

import org.mesonet.app.about.ContactActivity;
import org.mesonet.app.usersettings.UserSettingsActivity;
import org.mesonet.core.PerActivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;


@PerActivity
@Subcomponent(modules = ContactActivityModule.class)
public interface ContactActivitySubcomponent extends AndroidInjector<ContactActivity> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<ContactActivity> {
    }
}
