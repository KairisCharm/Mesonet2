package org.mesonet.app.maps

import android.databinding.ViewDataBinding
import android.view.ViewGroup
import org.mesonet.app.baseclasses.RecyclerViewAdapter
import org.mesonet.app.baseclasses.RecyclerViewHolder
import org.mesonet.dataprocessing.maps.MapsDataProvider

class MapsSectionRecyclerViewAdapter : RecyclerViewAdapter<MapsDataProvider.MapFullGroupDisplayData.MapGroupSection, RecyclerViewHolder<MapsDataProvider.MapFullGroupDisplayData.MapGroupSection, ViewDataBinding>>() {
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder<MapsDataProvider.MapFullGroupDisplayData.MapGroupSection, ViewDataBinding> {
        return MapsSectionViewHolder.NewInstance(parent, viewType == 0)
    }

}