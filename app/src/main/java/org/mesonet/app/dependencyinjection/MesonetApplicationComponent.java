package org.mesonet.app.dependencyinjection;

import org.mesonet.app.MesonetApplication;

import javax.inject.Singleton;

import dagger.Component;



@Singleton
@Component(modules = MesonetApplicationModule.class)
public interface MesonetApplicationComponent
{
    void Inject(MesonetApplication inApplication);
}