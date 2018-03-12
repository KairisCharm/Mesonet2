package org.mesonet.app.advisories;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mesonet.app.R;
import org.mesonet.app.baseclasses.BaseFragment;
import org.mesonet.app.databinding.AdvisoriesFragmentBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.inject.Inject;



public class AdvisoriesFragment extends BaseFragment implements Observer
{
    private AdvisoriesFragmentBinding mBinding;

    @Inject
    AdvisoryDataProvider mAdvisoryDataProvider;

    @Inject
    AdvisorySorter mAdvisorySorter;



    @Override
    public View onCreateView(LayoutInflater inInflater, ViewGroup inParent, final Bundle inSavedInstanceState)
    {
        mBinding = DataBindingUtil.inflate(inInflater, R.layout.advisories_fragment, inParent, false);

        mAdvisoryDataProvider.addObserver(this);
        mBinding.advisoriesRecyclerView.setAdapter(new AdvisoriesRecyclerViewAdapter());

        return mBinding.getRoot();
    }



    @Override
    public void update(Observable o, Object arg)
    {
        List<AdvisoryModel> advisories = mAdvisoryDataProvider.GetAdvisories();

        if(advisories != null)
        {
            List<Object> endResult = new ArrayList<>();

            AdvisoryModel.AdvisoryLevel currentLevel = null;
            AdvisoryModel.AdvisoryType currentType = null;

            advisories = mAdvisorySorter.Sort(advisories);

            for(int i = 0; i < advisories.size(); i++)
            {
                if(advisories.get(i).mAdvisoryType.mAdvisoryLevel != currentLevel ||
                        advisories.get(i).mAdvisoryType.mAdvisoryType != currentType)
                {
                    endResult.add(advisories.get(i).mAdvisoryType);
                    currentLevel = advisories.get(i).mAdvisoryType.mAdvisoryLevel;
                    currentType = advisories.get(i).mAdvisoryType.mAdvisoryType;
                }

                endResult.add(advisories.get(i));
            }

            mBinding.advisoriesRecyclerView.SetItems(endResult);
        }
    }
}
