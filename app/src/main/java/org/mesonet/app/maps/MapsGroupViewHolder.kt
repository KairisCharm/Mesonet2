package org.mesonet.app.maps


import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import org.mesonet.app.R
import org.mesonet.app.baseclasses.BaseActivity
import org.mesonet.app.baseclasses.RecyclerViewHolder
import org.mesonet.app.databinding.MapsGroupViewHolderBinding
import org.mesonet.dataprocessing.maps.MapsDataProvider

class MapsGroupViewHolder(private val mBaseActivity: BaseActivity, inBinding: MapsGroupViewHolderBinding) : RecyclerViewHolder<MapsDataProvider.MapAbbreviatedGroupDisplayData, MapsGroupViewHolderBinding>(inBinding) {
    init {
        inBinding.productRecyclerView.setAdapter(MapsProductRecyclerViewAdapter())
    }

    override fun SetData(inData: MapsDataProvider.MapAbbreviatedGroupDisplayData) {
        val binding = GetBinding()

        binding?.header?.headerText?.text = inData.GetTitle()
        binding?.productRecyclerView?.GetAdapter()!!.SetItems(inData.GetProducts())

        if (inData.GetFullListSize() > inData.GetGroupDisplayLimit())
            binding.viewAllLayout.visibility = View.VISIBLE
        else
            binding.viewAllLayout.visibility = View.GONE

        binding.viewAllLayout.setOnClickListener {
            val args = Bundle()
            args.putSerializable(MapListFragment.kMapGroupFullList, inData.GetMapFullGroupDisplayData())

            val fragment = MapListFragment()
            fragment.arguments = args

            mBaseActivity.NavigateToPage(fragment, true, R.anim.slide_from_right_animation, R.anim.slide_to_left_animation)
        }
    }

    companion object {
        fun NewInstance(inActivity: BaseActivity, inParent: ViewGroup): MapsGroupViewHolder {
            return MapsGroupViewHolder(inActivity, DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(inParent.context), R.layout.maps_group_view_holder, inParent, false) as MapsGroupViewHolderBinding)
        }
    }
}
