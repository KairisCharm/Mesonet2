package org.mesonet.app.maps;

import org.mesonet.app.baseclasses.BaseActivity;



public class MapGroupRecyclerViewAdapter extends MapsRecyclerViewAdapter {
    public MapGroupRecyclerViewAdapter(BaseActivity inBaseActivity) {
        super(inBaseActivity);
    }


    @Override
    public int getItemCount()
    {
        if(mDataItems != null && mDataItems.size() > 4)
            return 3;

        return super.getItemCount();
    }
}
