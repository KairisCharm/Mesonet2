package org.mesonet.app.maps

import android.view.ViewGroup
import org.mesonet.app.baseclasses.RecyclerViewAdapter
import org.mesonet.models.maps.MapsList


class MapsProductRecyclerViewAdapter: RecyclerViewAdapter<Pair<MapsList.Product, MapsList.GroupSection>, MapsProductViewHolder>()
{
    override fun onCreateViewHolder(inParent: ViewGroup, inViewType: Int): MapsProductViewHolder
    {
        return MapsProductViewHolder.NewInstance(inParent)
    }
}