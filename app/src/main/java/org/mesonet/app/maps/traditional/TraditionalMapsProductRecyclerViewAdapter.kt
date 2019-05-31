package org.mesonet.app.maps.traditional

import android.databinding.ViewDataBinding
import android.view.ViewGroup
import org.mesonet.app.baseclasses.RecyclerViewAdapter
import org.mesonet.app.baseclasses.RecyclerViewHolder
import org.mesonet.dataprocessing.maps.MapsDataProvider

class TraditionalMapsProductRecyclerViewAdapter : RecyclerViewAdapter<MapsDataProvider.MapFullGroupDisplayData.MapGroupSection.MapsProduct, RecyclerViewHolder<MapsDataProvider.MapFullGroupDisplayData.MapGroupSection.MapsProduct, ViewDataBinding>>() {
    override fun onCreateViewHolder(inParent: ViewGroup, inViewGroup: Int): RecyclerViewHolder<MapsDataProvider.MapFullGroupDisplayData.MapGroupSection.MapsProduct, ViewDataBinding> {
        return TraditionalMapsProductViewHolder.NewInstance(inParent)
    }
}