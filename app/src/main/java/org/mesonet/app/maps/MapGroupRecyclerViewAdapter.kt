package org.mesonet.app.maps

import org.mesonet.app.baseclasses.BaseActivity


class MapGroupRecyclerViewAdapter(inBaseActivity: BaseActivity) : MapsRecyclerViewAdapter(inBaseActivity) {
    override fun getItemCount() : Int
    {
        return if (mDataItems != null && mDataItems!!.size > 4) 3 else super.getItemCount()
    }
}
