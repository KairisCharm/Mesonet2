package org.mesonet.app.maps.traditional

import android.view.ViewGroup
import org.mesonet.app.baseclasses.BaseActivity
import org.mesonet.app.baseclasses.RecyclerViewAdapter
import org.mesonet.dataprocessing.maps.MapsDataProvider

class TraditionalMapsSectionRecyclerViewAdapter (private val mBaseActivity: BaseActivity) : RecyclerViewAdapter<MapsDataProvider.MapFullGroupDisplayData.MapGroupSection, TraditionalMapsSectionViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TraditionalMapsSectionViewHolder {
        return TraditionalMapsSectionViewHolder.NewInstance(mBaseActivity, p0)
    }

}