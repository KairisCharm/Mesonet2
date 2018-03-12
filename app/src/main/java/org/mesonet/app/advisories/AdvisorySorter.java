package org.mesonet.app.advisories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;



public class AdvisorySorter
{
    @Inject
    public AdvisorySorter(){}



    public List<AdvisoryModel> Sort(List<AdvisoryModel> inOriginal)
    {
        List<AdvisoryModel> result = new ArrayList<>(inOriginal);

        Collections.sort(result, new Comparator<AdvisoryModel>() {
            @Override
            public int compare(AdvisoryModel o1, AdvisoryModel o2) {
                int result = o1.mAdvisoryType.mAdvisoryLevel.compareTo(o2.mAdvisoryType.mAdvisoryLevel);

                if(result == 0)
                    result = o1.mAdvisoryType.mAdvisoryType.compareTo(o2.mAdvisoryType.mAdvisoryType);

                return result;
            }
        });

        return result;
    }
}
