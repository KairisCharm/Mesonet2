package org.mesonet.app.maps


import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.LayoutInflater
import android.view.ViewGroup

import org.mesonet.app.R
import org.mesonet.app.baseclasses.RecyclerViewHolder
import org.mesonet.app.databinding.HeaderViewHolderBinding

class MapsHeaderViewHolder(inBinding: HeaderViewHolderBinding) : RecyclerViewHolder<String, HeaderViewHolderBinding>(inBinding) {

    override fun SetData(inData: Any?) {
        if(inData != null && inData is String) {
            val binding = GetBinding()

            binding?.headerText?.setText(inData)
        }
    }

    companion object {
        fun NewInstance(inParent: ViewGroup): MapsHeaderViewHolder {
            return MapsHeaderViewHolder(DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(inParent.context), R.layout.header_view_holder, inParent, false) as HeaderViewHolderBinding)
        }
    }
}
