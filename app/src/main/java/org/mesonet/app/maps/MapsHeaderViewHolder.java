package org.mesonet.app.maps;


import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.mesonet.app.R;
import org.mesonet.app.baseclasses.RecyclerViewHolder;
import org.mesonet.app.databinding.HeaderViewHolderBinding;

public class MapsHeaderViewHolder extends RecyclerViewHolder<String, HeaderViewHolderBinding>
{
    public static MapsHeaderViewHolder NewInstance(ViewGroup inParent)
    {
        return new MapsHeaderViewHolder((HeaderViewHolderBinding) DataBindingUtil.inflate(LayoutInflater.from(inParent.getContext()), R.layout.header_view_holder, inParent, false));
    }
    public MapsHeaderViewHolder(HeaderViewHolderBinding inBinding) {
        super(inBinding);
    }

    @Override
    public void SetData(String inData) {
        if(inData != null)
        {
            HeaderViewHolderBinding binding = GetBinding();

            binding.headerText.setText(inData);
        }
    }
}
