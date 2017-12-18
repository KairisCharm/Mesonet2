package org.mesonet.app.dependencyinjection;

import org.mesonet.app.MesonetApplication;
import org.mesonet.app.mesonetdata.MesonetUIController;

import javax.inject.Singleton;

import dagger.Component;



@Singleton
@Component(modules = {MesonetApplicationModule.class})
public interface MesonetApplicationComponent
{
    void Inject(MesonetApplication inApplication);
}