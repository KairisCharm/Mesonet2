package org.mesonet.app.maps.traditional

import android.databinding.ViewDataBinding
import android.view.ViewGroup
import org.mesonet.app.baseclasses.BaseActivity
import org.mesonet.app.baseclasses.RecyclerViewAdapter
import org.mesonet.app.baseclasses.RecyclerViewHolder
import org.mesonet.dataprocessing.maps.MapsDataProvider

class TraditionalMapsProductRecyclerViewAdapter(private val mBaseActivity: BaseActivity) : RecyclerViewAdapter<MapsDataProvider.MapFullGroupDisplayData.MapGroupSection.MapsProduct, RecyclerViewHolder<MapsDataProvider.MapFullGroupDisplayData.MapGroupSection.MapsProduct, ViewDataBinding>>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerViewHolder<MapsDataProvider.MapFullGroupDisplayData.MapGroupSection.MapsProduct, ViewDataBinding> {
        return TraditionalMapsProductViewHolder.NewInstance(mBaseActivity, p0)
    }
}