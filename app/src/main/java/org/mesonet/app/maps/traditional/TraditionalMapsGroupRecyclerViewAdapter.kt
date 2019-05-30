package org.mesonet.app.maps.traditional

import android.databinding.ViewDataBinding
import android.view.ViewGroup
import org.mesonet.app.baseclasses.BaseActivity
import org.mesonet.app.baseclasses.RecyclerViewAdapter
import org.mesonet.app.baseclasses.RecyclerViewHolder
import org.mesonet.dataprocessing.maps.MapsDataProvider

class TraditionalMapsGroupRecyclerViewAdapter(private val mBaseActivity: BaseActivity) : RecyclerViewAdapter<MapsDataProvider.MapAbbreviatedGroupDisplayData, RecyclerViewHolder<MapsDataProvider.MapAbbreviatedGroupDisplayData, ViewDataBinding>>() {
    override fun onCreateViewHolder(inParent: ViewGroup, p1: Int): RecyclerViewHolder<MapsDataProvider.MapAbbreviatedGroupDisplayData, ViewDataBinding> {
        return TraditionalMapsGroupViewHolder.NewInstance(mBaseActivity, inParent)
    }
}