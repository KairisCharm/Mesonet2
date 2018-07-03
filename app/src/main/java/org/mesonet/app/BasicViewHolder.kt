package org.mesonet.app


import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.mesonet.app.baseclasses.RecyclerViewHolder

import org.mesonet.app.databinding.BasicViewHolderBinding
import org.mesonet.dataprocessing.BasicListData
import org.mesonet.dataprocessing.SelectSiteListener

class BasicViewHolder(inParent: ViewGroup, private var mSelectedListener: SelectSiteListener) : RecyclerViewHolder<Pair<String, BasicListData>, BasicViewHolderBinding>(DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(inParent.context), R.layout.basic_view_holder, inParent, false)) {
    override fun SetData(inData: Pair<String, BasicListData>) {
        GetBinding()?.favorite = inData.second.IsFavorite()
        GetBinding()?.text = inData.second.GetName()
        GetBinding()?.root?.setOnClickListener { mSelectedListener.SetResult(inData.first as String) }
    }
}