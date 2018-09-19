package org.mesonet.app.maps

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.mesonet.app.R

import org.mesonet.app.baseclasses.RecyclerViewHolder
import org.mesonet.app.databinding.MapsSectionViewHolderBinding
import org.mesonet.dataprocessing.maps.MapsDataProvider


class MapsSectionViewHolder(inBinding: MapsSectionViewHolderBinding,
                            val mIsFirst: Boolean) : RecyclerViewHolder<MapsDataProvider.MapFullGroupDisplayData.MapGroupSection, MapsSectionViewHolderBinding>(inBinding) {

    override fun SetData(inData: MapsDataProvider.MapFullGroupDisplayData.MapGroupSection?) {
        val binding = GetBinding()

        binding?.productRecyclerView?.setAdapter(MapsProductRecyclerViewAdapter())

        binding?.header?.headerText?.text = inData?.GetTitle()

        if(mIsFirst && binding?.header?.headerText?.text.isNullOrBlank())
            binding?.header?.headerText?.visibility = View.GONE

        binding?.productRecyclerView?.SetItems(ArrayList(inData?.GetProducts()?.values))
    }

    companion object {
        fun NewInstance(inParent: ViewGroup, inIsFirst: Boolean): MapsSectionViewHolder {
            return MapsSectionViewHolder(DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(inParent.context), R.layout.maps_section_view_holder, inParent, false) as MapsSectionViewHolderBinding, inIsFirst)
        }
    }
}
