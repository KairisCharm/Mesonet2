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
import org.mesonet.app.databinding.MapsSectionViewHolderBinding
import org.mesonet.dataprocessing.maps.MapsModel

class MapsSectionViewHolder(inBinding: MapsSectionViewHolderBinding) : RecyclerViewHolder<Pair<Int, MapsModel.SectionModel>, MapsSectionViewHolderBinding>(inBinding) {
    private var mBaseActivity: BaseActivity? = null

    override fun SetData(inData: Any?) {
        if (inData != null && inData is Pair<*, *> && inData.first is Int && inData.second is MapsModel.SectionModel) {
            GetBinding()?.sectionTitle?.text = (inData.second as MapsModel.SectionModel).GetTitle()
            GetBinding()?.layout?.setOnClickListener(View.OnClickListener {
                val args = Bundle()
                args.putInt(MapListFragment.kSelectedGroup, inData.first as Int)

                val fragment = MapListFragment()
                fragment.arguments = args

                mBaseActivity!!.NavigateToPage(fragment, true, R.anim.slide_from_right_animation, R.anim.slide_to_left_animation)
            })
        }
    }

    companion object {

        fun NewInstance(inBaseActivity: BaseActivity, inParent: ViewGroup): MapsSectionViewHolder {
            val viewHolder = MapsSectionViewHolder(DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(inParent.context), R.layout.maps_section_view_holder, inParent, false) as MapsSectionViewHolderBinding)
            viewHolder.mBaseActivity = inBaseActivity

            return viewHolder
        }
    }
}
