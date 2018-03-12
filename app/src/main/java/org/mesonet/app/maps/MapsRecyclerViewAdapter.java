package org.mesonet.app.maps;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.ViewGroup;

import org.mesonet.app.baseclasses.BaseActivity;
import org.mesonet.app.baseclasses.RecyclerViewAdapter;
import org.mesonet.app.baseclasses.RecyclerViewHolder;

import java.util.List;



public class MapsRecyclerViewAdapter extends RecyclerViewAdapter<Object, RecyclerViewHolder>
{
    enum MapViewHolderTypes { kHeader, kProduct, kGroup }

    private BaseActivity mBaseActivity;



    public MapsRecyclerViewAdapter(BaseActivity inBaseActivity)
    {
        mBaseActivity = inBaseActivity;
    }

    @Override
    public int getItemViewType(int inPosition)
    {
        if(inPosition >= 0 && inPosition < mDataItems.size())
        {
            if(mDataItems.get(inPosition) instanceof String)
                return MapViewHolderTypes.kHeader.ordinal();

            if(mDataItems.get(inPosition) instanceof MapsProductViewHolder.MapsProductViewHolderData)
                return MapViewHolderTypes.kProduct.ordinal();

            if(mDataItems.get(inPosition) instanceof Pair)
                return MapViewHolderTypes.kGroup.ordinal();
        }

        return -1;
    }


    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup inParent, int inViewType) {

        try {
            switch (MapViewHolderTypes.values()[inViewType]) {
                case kHeader:
                    return MapsHeaderViewHolder.NewInstance(inParent);
                case kProduct:
                    return MapsProductViewHolder.NewInstance(mBaseActivity.getSupportFragmentManager(), inParent);
                case kGroup:
                    return MapsGroupViewHolder.NewInstance(mBaseActivity, inParent);
            }
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
