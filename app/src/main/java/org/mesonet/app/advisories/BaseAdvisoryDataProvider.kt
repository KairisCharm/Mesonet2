package org.mesonet.app.advisories

import java.util.Observable

import javax.inject.Inject


abstract class BaseAdvisoryDataProvider : Observable() {
    var mCurrentAdvisories: List<AdvisoryModel>? = null

    @Inject
    lateinit var mAdvisoryParser: AdvisoryParser


    fun SetData(inData: String) {
        mCurrentAdvisories = mAdvisoryParser.ParseAdvisoryFile(inData)
        setChanged()
        notifyObservers()
    }


    fun GetAdvisories(): List<AdvisoryModel>? {
        return mCurrentAdvisories
    }


    fun GetCount(): Int? {
        return if (mCurrentAdvisories == null) 0 else mCurrentAdvisories?.size

    }


    protected abstract fun Update()
}
