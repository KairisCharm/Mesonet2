package org.mesonet.app.maps.traditional

import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import org.mesonet.app.R
import org.mesonet.app.baseclasses.BaseActivity
import org.mesonet.app.baseclasses.RecyclerViewHolder
import org.mesonet.app.databinding.TraditionalMapsSectionViewHolderBinding
import org.mesonet.dataprocessing.maps.MapsDataProvider

class TraditionalMapsSectionViewHolder(mBaseActivity: BaseActivity, inBinding: TraditionalMapsSectionViewHolderBinding) : RecyclerViewHolder<MapsDataProvider.MapFullGroupDisplayData.MapGroupSection, TraditionalMapsSectionViewHolderBinding>(inBinding) {
    companion object {
        fun NewInstance(inBaseActivity: BaseActivity, inParent: ViewGroup): TraditionalMapsSectionViewHolder {
            return TraditionalMapsSectionViewHolder(inBaseActivity, DataBindingUtil.inflate(LayoutInflater.from(inParent.context), R.layout.traditional_maps_section_view_holder, inParent, false))
        }
    }

    init {
        GetBinding()?.productRecyclerView?.setAdapter(TraditionalMapsProductRecyclerViewAdapter(mBaseActivity))
    }

    override fun SetData(inData: MapsDataProvider.MapFullGroupDisplayData.MapGroupSection?) {
        val list = mutableListOf<MapsDataProvider.MapFullGroupDisplayData.MapGroupSection.MapsProduct>()

        list.addAll(inData?.GetProducts()?.values?: mutableListOf())
        GetBinding()?.productRecyclerView?.SetItems(list)
    }
}