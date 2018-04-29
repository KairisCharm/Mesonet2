package org.mesonet.dataprocessing.userdata

import java.util.Observable


open class PreferencesObservable : Observable() {


    override fun notifyObservers() {
        setChanged()
        super.notifyObservers()
    }
}
