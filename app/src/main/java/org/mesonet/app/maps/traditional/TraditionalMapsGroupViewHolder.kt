package org.mesonet.app.maps.traditional

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import org.mesonet.app.R
import org.mesonet.app.baseclasses.BaseActivity
import org.mesonet.app.baseclasses.RecyclerViewHolder
import org.mesonet.app.databinding.TraditionalMapsViewHolderBinding
import org.mesonet.app.maps.MapListFragment
import org.mesonet.dataprocessing.maps.MapsDataProvider

class TraditionalMapsGroupViewHolder(private val mBaseActivity: BaseActivity?, inBinding: TraditionalMapsViewHolderBinding) : RecyclerViewHolder<MapsDataProvider.MapAbbreviatedGroupDisplayData, TraditionalMapsViewHolderBinding>(inBinding) {
    companion object {
        fun NewInstance(mBaseActivity: BaseActivity, inParent: ViewGroup): TraditionalMapsGroupViewHolder {
            return TraditionalMapsGroupViewHolder(mBaseActivity, DataBindingUtil.inflate(LayoutInflater.from(inParent.context), R.layout.traditional_maps_view_holder, inParent, false))
        }
    }

    override fun SetData(inData: MapsDataProvider.MapAbbreviatedGroupDisplayData?) {
        val binding = GetBinding()

        binding?.title = inData?.GetTitle()

        binding?.layout?.setOnClickListener{
            it.isEnabled = false
            val args = Bundle()
            args.putSerializable(MapListFragment.kMapGroupFullList, inData?.GetMapFullGroupDisplayData())

            val fragment = MapListFragment()
            fragment.arguments = args

            mBaseActivity?.NavigateToPage(fragment, true, R.anim.slide_from_right_animation, R.anim.slide_to_left_animation)
            it.isEnabled = true
        }
    }
}