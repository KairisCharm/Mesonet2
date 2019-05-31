package org.mesonet.app.maps.traditional

import android.view.ViewGroup
import org.mesonet.app.baseclasses.RecyclerViewAdapter
import org.mesonet.dataprocessing.maps.MapsDataProvider

class TraditionalMapsSectionRecyclerViewAdapter : RecyclerViewAdapter<MapsDataProvider.MapFullGroupDisplayData.MapGroupSection, TraditionalMapsSectionViewHolder>() {
    override fun onCreateViewHolder(inParent: ViewGroup, inViewType: Int): TraditionalMapsSectionViewHolder {
        return TraditionalMapsSectionViewHolder.NewInstance(inParent)
    }
}