package org.mesonet.app.maps;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mesonet.app.R;
import org.mesonet.app.baseclasses.BaseActivity;
import org.mesonet.app.baseclasses.RecyclerViewHolder;
import org.mesonet.app.databinding.MapsGroupViewHolderBinding;

import java.util.List;

public class MapsGroupViewHolder extends RecyclerViewHolder<Pair<Integer, List>, MapsGroupViewHolderBinding>
{
    private BaseActivity mBaseActivity;

    public static MapsGroupViewHolder NewInstance(BaseActivity inActivity, ViewGroup inParent)
    {
        MapsGroupViewHolder viewHolder = new MapsGroupViewHolder(inActivity, (MapsGroupViewHolderBinding) DataBindingUtil.inflate(LayoutInflater.from(inParent.getContext()), R.layout.maps_group_view_holder, inParent, false));
        return viewHolder;
    }


    public MapsGroupViewHolder(BaseActivity inActivity, MapsGroupViewHolderBinding inBinding) {
        super(inBinding);

        mBaseActivity = inActivity;

        inBinding.groupRecyclerView.setAdapter(new MapGroupRecyclerViewAdapter(inActivity));
    }

    @Override
    public void SetData(final Pair<Integer, List> inData) {
        if(inData != null)
        {
            MapsGroupViewHolderBinding binding = GetBinding();

            binding.groupRecyclerView.GetAdapter().SetItems(inData.second);

            if(inData.second.size() > 4)
                binding.viewAllLayout.setVisibility(View.VISIBLE);
            else
                binding.viewAllLayout.setVisibility(View.GONE);

            binding.viewAllLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle args = new Bundle();
                    args.putInt(MapListFragment.kSelectedGroup, inData.first);

                    MapListFragment fragment = new MapListFragment();
                    fragment.setArguments(args);

                    mBaseActivity.NavigateToPage(fragment, true, R.anim.slide_from_right_animation, R.anim.slide_to_left_animation);
                }
            });
        }
    }
}
