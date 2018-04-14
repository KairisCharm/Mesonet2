package org.mesonet.app.userdata

import java.util.Observable


class PreferencesObservable : Observable() {


    override fun notifyObservers() {
        setChanged()
        super.notifyObservers()
    }
}
