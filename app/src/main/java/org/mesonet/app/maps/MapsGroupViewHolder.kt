package org.mesonet.app.maps


import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import org.mesonet.app.R
import org.mesonet.app.baseclasses.BaseActivity
import org.mesonet.app.baseclasses.RecyclerViewHolder
import org.mesonet.app.databinding.MapsGroupViewHolderBinding

class MapsGroupViewHolder(private val mBaseActivity: BaseActivity, inBinding: MapsGroupViewHolderBinding) : RecyclerViewHolder<Pair<Int, List<*>>, MapsGroupViewHolderBinding>(inBinding) {


    init {
        inBinding.groupRecyclerView.setAdapter(MapGroupRecyclerViewAdapter(mBaseActivity))
    }

    override fun SetData(inData: Any?) {
        if (inData != null && inData is Pair<*, *> && inData.first is Int && inData.second is List<*>) {
            val binding = GetBinding()

            binding?.groupRecyclerView?.GetAdapter()!!.SetItems(inData.second as MutableList<*>)

            if ((inData.second as List<*>).size > 4)
                binding?.viewAllLayout.setVisibility(View.VISIBLE)
            else
                binding?.viewAllLayout.setVisibility(View.GONE)

            binding?.viewAllLayout.setOnClickListener(View.OnClickListener {
                val args = Bundle()
                args.putInt(MapListFragment.kSelectedGroup, inData.first as Int)

                val fragment = MapListFragment()
                fragment.setArguments(args)

                mBaseActivity.NavigateToPage(fragment, true, R.anim.slide_from_right_animation, R.anim.slide_to_left_animation)
            })
        }
    }

    companion object {
        fun NewInstance(inActivity: BaseActivity, inParent: ViewGroup): MapsGroupViewHolder {
            return MapsGroupViewHolder(inActivity, DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(inParent.context), R.layout.maps_group_view_holder, inParent, false) as MapsGroupViewHolderBinding)
        }
    }
}
