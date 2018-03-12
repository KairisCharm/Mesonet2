package org.mesonet.app.advisories;

import java.util.List;
import java.util.Observable;

import javax.inject.Inject;


public abstract class BaseAdvisoryDataProvider extends Observable
{
    protected List<AdvisoryModel> mCurrentAdvisories;

    @Inject
    AdvisoryParser mAdvisoryParser;



    public void SetData(String inData)
    {
        mCurrentAdvisories = mAdvisoryParser.ParseAdvisoryFile(inData);
        setChanged();
        notifyObservers();
    }



    public List<AdvisoryModel> GetAdvisories()
    {
        return mCurrentAdvisories;
    }



    public int GetCount()
    {
        if(mCurrentAdvisories == null)
            return 0;

        return mCurrentAdvisories.size();
    }


    protected abstract void Update();
}
