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
import org.mesonet.app.databinding.MapsSectionViewHolderBinding;

public class MapsSectionViewHolder extends RecyclerViewHolder<Pair<Integer, MapsModel.SectionModel>, MapsSectionViewHolderBinding>
{    private BaseActivity mBaseActivity;

    public static MapsSectionViewHolder NewInstance(BaseActivity inBaseActivity, ViewGroup inParent)
    {
        MapsSectionViewHolder viewHolder = new MapsSectionViewHolder((MapsSectionViewHolderBinding) DataBindingUtil.inflate(LayoutInflater.from(inParent.getContext()), R.layout.maps_section_view_holder, inParent, false));
        viewHolder.mBaseActivity = inBaseActivity;

        return viewHolder;
    }


    public MapsSectionViewHolder(MapsSectionViewHolderBinding inBinding) {
        super(inBinding);
    }

    @Override
    public void SetData(final Pair<Integer, MapsModel.SectionModel> inData) {
        if(inData != null)
        {
            GetBinding().sectionTitle.setText(inData.second.mTitle);
            GetBinding().layout.setOnClickListener(new View.OnClickListener() {
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
