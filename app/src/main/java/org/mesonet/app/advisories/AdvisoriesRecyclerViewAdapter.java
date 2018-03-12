package org.mesonet.app.advisories;

import android.support.annotation.NonNull;
import android.util.Pair;
import android.view.ViewGroup;

import org.mesonet.app.baseclasses.RecyclerViewAdapter;
import org.mesonet.app.baseclasses.RecyclerViewHolder;
import org.mesonet.app.maps.MapsProductViewHolder;
import org.mesonet.app.maps.MapsRecyclerViewAdapter;


public class AdvisoriesRecyclerViewAdapter extends RecyclerViewAdapter<Object, RecyclerViewHolder>
{
    enum AdvisoryViewHolderType { kHeader, kContent }

    @Override
    public int getItemViewType(int inPosition)
    {
        if(inPosition >= 0 && inPosition < mDataItems.size())
        {
            if(mDataItems.get(inPosition) instanceof AdvisoryModel.AdvisoryTypeModel)
                return AdvisoryViewHolderType.kHeader.ordinal();

            if(mDataItems.get(inPosition) instanceof AdvisoryModel)
                return AdvisoryViewHolderType.kContent.ordinal();
        }

        return -1;
    }



    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup inParent, int inViewType)
    {
        switch (AdvisoryViewHolderType.values()[inViewType])
        {
            case kHeader:
                return AdvisoryHeaderViewHolder.NewInstance(inParent);
            case kContent:
                return AdvisoryViewHolder.NewInstance(inParent);
        }
        return null;
    }
}
