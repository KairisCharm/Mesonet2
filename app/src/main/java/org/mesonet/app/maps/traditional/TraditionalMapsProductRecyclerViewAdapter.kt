package org.mesonet.app.maps.traditional

import android.databinding.ViewDataBinding
import android.view.ViewGroup
import org.mesonet.app.baseclasses.RecyclerViewAdapter
import org.mesonet.app.baseclasses.RecyclerViewHolder
import org.mesonet.models.maps.MapsList

class TraditionalMapsProductRecyclerViewAdapter : RecyclerViewAdapter<MapsList.Product, RecyclerViewHolder<MapsList.Product, ViewDataBinding>>() {
    override fun onCreateViewHolder(inParent: ViewGroup, inViewGroup: Int): RecyclerViewHolder<MapsList.Product, ViewDataBinding> {
        return TraditionalMapsProductViewHolder.NewInstance(inParent)
    }
}