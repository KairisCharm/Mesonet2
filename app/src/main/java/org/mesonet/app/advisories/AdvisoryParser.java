package org.mesonet.app.advisories;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

public class AdvisoryParser
{
    @Inject
    public AdvisoryParser(){}



    public List<AdvisoryModel> ParseAdvisoryFile(String inAdvisoryFile)
    {
        List<AdvisoryModel> result = new ArrayList<>();
        String[] splitList = inAdvisoryFile.split(";");

        for(int i = 0; (i + 5) < splitList.length; i++)
        {
            AdvisoryModel advisory = new AdvisoryModel();

            String[] splitType = splitList[i].substring(splitList[i].length() - 4).split("\\.");

            try {
                advisory.mAdvisoryType.mAdvisoryType = AdvisoryModel.AdvisoryType.valueOf(splitType[0]);
                advisory.mAdvisoryType.mAdvisoryLevel = AdvisoryModel.AdvisoryLevel.valueOf(splitType[1]);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            i += 4;

            advisory.mFilePath = splitList[i++];

            advisory.mCountyCodes = new ArrayList<>();

            String[] splitCounties = splitList[i].split(",");

            advisory.mCountyCodes.addAll(Arrays.asList(splitCounties));

            result.add(advisory);
        }

        return result;
    }
}
