package org.mesonet.app.advisories

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.LayoutInflater
import android.view.ViewGroup

import org.mesonet.app.R
import org.mesonet.app.baseclasses.RecyclerViewHolder
import org.mesonet.app.databinding.HeaderViewHolderBinding


class AdvisoryHeaderViewHolder(inBinding: HeaderViewHolderBinding) : RecyclerViewHolder<AdvisoryModel.AdvisoryTypeModel, HeaderViewHolderBinding>(inBinding) {

    override fun SetData(inData: Any?) {

        if (inData != null && inData is AdvisoryModel.AdvisoryTypeModel) {
            val binding = GetBinding()

            val advisoryHeaderText = inData.mAdvisoryType.toString() + " " + inData.mAdvisoryLevel

            binding?.headerText!!.text = advisoryHeaderText
        }
    }

    companion object {
        fun NewInstance(inParent: ViewGroup): AdvisoryHeaderViewHolder {
            return AdvisoryHeaderViewHolder(DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(inParent.context), R.layout.header_view_holder, inParent, false) as HeaderViewHolderBinding)
        }
    }
}
