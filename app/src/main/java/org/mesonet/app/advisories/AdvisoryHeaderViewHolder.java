package org.mesonet.app.advisories;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.mesonet.app.R;
import org.mesonet.app.baseclasses.RecyclerViewHolder;
import org.mesonet.app.databinding.HeaderViewHolderBinding;
import org.mesonet.app.maps.MapsHeaderViewHolder;



public class AdvisoryHeaderViewHolder extends RecyclerViewHolder<AdvisoryModel.AdvisoryTypeModel, HeaderViewHolderBinding>
{
    public static AdvisoryHeaderViewHolder NewInstance(ViewGroup inParent)
    {
        return new AdvisoryHeaderViewHolder((HeaderViewHolderBinding) DataBindingUtil.inflate(LayoutInflater.from(inParent.getContext()), R.layout.header_view_holder, inParent, false));
    }
    public AdvisoryHeaderViewHolder(HeaderViewHolderBinding inBinding) {
        super(inBinding);
    }

    @Override
    public void SetData(AdvisoryModel.AdvisoryTypeModel inData) {
        if(inData != null)
        {
            HeaderViewHolderBinding binding = GetBinding();

            binding.headerText.setText(inData.mAdvisoryType + " " + inData.mAdvisoryLevel);
        }
    }
}
