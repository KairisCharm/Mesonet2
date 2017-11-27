package org.mesonet.app.mesonetdata;


import java.util.Map;

public class SiteSelectionInterfaces {
    public interface SelectSiteListener
    {
        void SetResult(String inResult);
    }



    public interface SelectSiteController
    {
        void StartSelection(SelectSiteListener inListener, Map<String, String> inKeysToDisplayText);
    }
}
