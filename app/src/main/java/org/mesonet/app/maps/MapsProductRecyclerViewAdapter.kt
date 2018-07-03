package org.mesonet.app.maps

import android.view.ViewGroup
import org.mesonet.app.baseclasses.RecyclerViewAdapter
import org.mesonet.dataprocessing.maps.MapsDataProvider



class MapsProductRecyclerViewAdapter: RecyclerViewAdapter<MapsDataProvider.MapFullGroupDisplayData.MapGroupSection.MapsProduct, MapsProductViewHolder>()
{
    override fun onCreateViewHolder(inParent: ViewGroup, inViewType: Int): MapsProductViewHolder
    {
        return MapsProductViewHolder.NewInstance(inParent)
    }
}