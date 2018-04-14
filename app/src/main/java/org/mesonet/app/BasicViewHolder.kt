package org.mesonet.app


import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.mesonet.app.baseclasses.RecyclerViewHolder

import org.mesonet.app.databinding.BasicViewHolderBinding
import org.mesonet.app.site.SiteSelectionInterfaces

class BasicViewHolder(inParent: ViewGroup, internal var mSelectedListener: SiteSelectionInterfaces.SelectSiteListener) : RecyclerViewHolder<Pair<String, BasicViewHolder.BasicViewHolderData>, BasicViewHolderBinding>(DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(inParent.context), R.layout.basic_view_holder, inParent, false)) {


    override fun SetData(inData: Any?) {
        if(inData is Pair<*, *> && inData.first is String && inData.second is BasicViewHolderData) {
            GetBinding()?.setFavorite((inData.second as BasicViewHolderData).mFavorite)
            GetBinding()?.setText((inData.second as BasicViewHolderData).mName)
            GetBinding()?.getRoot()?.setOnClickListener(View.OnClickListener { mSelectedListener.SetResult(inData.first as String) })
        }
    }


    class BasicViewHolderData(internal var mName: String, internal var mLocation: Location, internal var mFavorite: Boolean) {


        fun GetName(): String {
            return mName
        }


        fun IsFavorite(): Boolean {
            return mFavorite
        }


        fun GetLocation(): Location {
            return mLocation
        }
    }
}
