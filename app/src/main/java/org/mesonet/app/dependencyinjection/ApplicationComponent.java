package org.mesonet.app.dependencyinjection;

import android.content.Context;

import org.mesonet.app.Application;

import javax.inject.Singleton;

import dagger.Component;



@Singleton
@Component(modules = {ApplicationModule.class,
                      ApplicationContextModule.class})
public interface ApplicationComponent
{
    Context Context();

    void Inject(Application inApplication);
}