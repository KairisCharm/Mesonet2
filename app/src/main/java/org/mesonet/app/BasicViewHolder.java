package org.mesonet.app;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import kotlin.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mesonet.app.baseclasses.RecyclerViewHolder;
import org.mesonet.app.databinding.BasicViewHolderBinding;
import org.mesonet.app.site.SiteSelectionInterfaces;

public class BasicViewHolder extends RecyclerViewHolder<Pair<String, Pair<String, Location>>, BasicViewHolderBinding>
{
    SiteSelectionInterfaces.SelectSiteListener mSelectedListener;



    public BasicViewHolder(ViewGroup inParent, @NonNull SiteSelectionInterfaces.SelectSiteListener inSelectedListener) {
        super(DataBindingUtil.inflate(LayoutInflater.from(inParent.getContext()), R.layout.basic_view_holder, inParent, false));
        mSelectedListener = inSelectedListener;
    }



    @Override
    public void SetData(final Pair<String, Pair<String, Location>> inData)
    {
        GetBinding().setText(inData.getSecond().getFirst());
        GetBinding().getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectedListener.SetResult(inData.getFirst());
            }
        });
    }
}
