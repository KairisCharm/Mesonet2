package org.mesonet.app;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import kotlin.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mesonet.app.baseclasses.RecyclerViewHolder;
import org.mesonet.app.databinding.BasicViewHolderBinding;

public class BasicViewHolder extends RecyclerViewHolder<Pair<String, String>, BasicViewHolderBinding>
{
    public BasicViewHolder(ViewGroup inParent) {
        super(DataBindingUtil.inflate(LayoutInflater.from(inParent.getContext()), R.layout.basic_view_holder, inParent, false));
    }



    @Override
    public void SetData(Pair<String, String> inData)
    {
        GetBinding().setText(inData.getSecond());
    }
}
