package org.mesonet.app.maps

import android.databinding.ViewDataBinding
import android.view.ViewGroup
import org.mesonet.app.baseclasses.RecyclerViewAdapter
import org.mesonet.app.baseclasses.RecyclerViewHolder
import org.mesonet.dataprocessing.maps.MapsDataProvider
import org.mesonet.models.maps.MapsList

class MapsSectionRecyclerViewAdapter(private val mMapsDataProvider: MapsDataProvider) : RecyclerViewAdapter<MapsList.GroupSection, RecyclerViewHolder<MapsList.GroupSection, ViewDataBinding>>() {
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder<MapsList.GroupSection, ViewDataBinding> {
        return MapsSectionViewHolder.NewInstance(parent, mMapsDataProvider, viewType == 0)
    }
}