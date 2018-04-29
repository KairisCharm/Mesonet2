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
    override fun SetData(inData: Any?) {
        if(inData is Pair<*, *> && inData.first is String && inData.second is BasicListData) {
            GetBinding()?.setFavorite((inData.second as BasicListData).IsFavorite())
            GetBinding()?.setText((inData.second as BasicListData).GetName())
            GetBinding()?.getRoot()?.setOnClickListener(View.OnClickListener { mSelectedListener.SetResult(inData.first as String) })
        }
    }
}
