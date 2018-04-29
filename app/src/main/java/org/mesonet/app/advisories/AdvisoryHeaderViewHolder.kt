package org.mesonet.app.advisories

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.LayoutInflater
import android.view.ViewGroup

import org.mesonet.app.R
import org.mesonet.app.baseclasses.RecyclerViewHolder
import org.mesonet.app.databinding.HeaderViewHolderBinding
import org.mesonet.dataprocessiFilenamevisories.AdvisoryListBuilder


class AdvisoryHeaderViewHolder(inBinding: HeaderViewHolderBinding) : RecyclerViewHolder<Pair<AdvisoryListBuilder.AdvisoryDataType, AdvisoryListBuilder.AdvisoryData>, HeaderViewHolderBinding>(inBinding) {

    override fun SetData(inData: Any?) {

        if (inData != null && inData is Pair<*,*> && inData.first is AdvisoryListBuilder.AdvisoryDataType && inData.second is AdvisoryListBuilder.AdvisoryData) {
            val binding = GetBinding()

            if(binding != null)
                binding.headerText.text = (inData.second as AdvisoryListBuilder.AdvisoryData).AdvisoryType()
        }
    }

    companion object {
        fun NewInstance(inParent: ViewGroup): AdvisoryHeaderViewHolder {
            return AdvisoryHeaderViewHolder(DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(inParent.context), R.layout.header_view_holder, inParent, false) as HeaderViewHolderBinding)
        }
    }
}
