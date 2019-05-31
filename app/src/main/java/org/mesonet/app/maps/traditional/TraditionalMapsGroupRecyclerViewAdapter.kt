package org.mesonet.app.maps.traditional

import android.view.ViewGroup
import org.mesonet.app.baseclasses.BaseActivity
import org.mesonet.app.baseclasses.RecyclerViewAdapter
import org.mesonet.app.baseclasses.RecyclerViewHolder
import org.mesonet.app.databinding.TraditionalMapsViewHolderBinding
import org.mesonet.dataprocessing.maps.MapsDataProvider



class TraditionalMapsGroupRecyclerViewAdapter(private val mBaseActivity: BaseActivity) : RecyclerViewAdapter<MapsDataProvider.MapAbbreviatedGroupDisplayData, RecyclerViewHolder<MapsDataProvider.MapAbbreviatedGroupDisplayData, TraditionalMapsViewHolderBinding>>() {
    override fun onCreateViewHolder(inParent: ViewGroup, inViewType: Int): RecyclerViewHolder<MapsDataProvider.MapAbbreviatedGroupDisplayData, TraditionalMapsViewHolderBinding> {
        return TraditionalMapsGroupViewHolder.NewInstance(mBaseActivity, inParent)
    }
}