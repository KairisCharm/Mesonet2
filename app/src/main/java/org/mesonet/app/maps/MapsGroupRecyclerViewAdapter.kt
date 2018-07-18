package org.mesonet.app.maps

import android.databinding.ViewDataBinding
import android.os.Parcelable
import android.support.v7.widget.RecyclerView
import android.util.Pair
import android.view.ViewGroup

import org.mesonet.app.baseclasses.BaseActivity
import org.mesonet.app.baseclasses.RecyclerViewAdapter
import org.mesonet.app.baseclasses.RecyclerViewHolder
import org.mesonet.dataprocessing.maps.MapsDataProvider



class MapsGroupRecyclerViewAdapter(private val mBaseActivity: BaseActivity?) : RecyclerViewAdapter<MapsDataProvider.MapAbbreviatedGroupDisplayData, RecyclerViewHolder<MapsDataProvider.MapAbbreviatedGroupDisplayData, ViewDataBinding>>() {
    override fun onCreateViewHolder(inParent: ViewGroup, inViewType: Int): RecyclerViewHolder<MapsDataProvider.MapAbbreviatedGroupDisplayData, ViewDataBinding> {
        return MapsGroupViewHolder.NewInstance(mBaseActivity, inParent)
    }
}
