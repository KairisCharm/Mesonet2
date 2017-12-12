package org.mesonet.app.userdata;

import java.util.Observable;

import javax.inject.Inject;



public class PreferencesObservable extends Observable
{
    @Inject
    public PreferencesObservable(){}



    @Override
    public void notifyObservers()
    {
        setChanged();
        super.notifyObservers();
    }
}
