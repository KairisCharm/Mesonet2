package org.mesonet.app.advisories

import android.view.ViewGroup

import org.mesonet.app.baseclasses.RecyclerViewAdapter
import org.mesonet.app.baseclasses.RecyclerViewHolder
import org.mesonet.dataprocessiFilenamevisories.AdvisoryDisplayListBuilder


class AdvisoriesRecyclerViewAdapter : RecyclerViewAdapter<Pair<AdvisoryDisplayListBuilder.AdvisoryDataType, AdvisoryDisplayListBuilder.AdvisoryData>, RecyclerViewHolder<Any, *>>() {


    override fun getItemViewType(inPosition: Int): Int {
        if(mDataItems != null && inPosition >= 0 && inPosition < mDataItems!!.size && mDataItems?.get(inPosition) != null && mDataItems?.get(inPosition)?.first != null)
        {
            return mDataItems?.get(inPosition)?.first!!.ordinal
        }

        return -1
    }


    override fun onCreateViewHolder(inParent: ViewGroup, inViewType: Int): RecyclerViewHolder<Any, *> {
        return when (AdvisoryDisplayListBuilder.AdvisoryDataType.values()[inViewType]) {
            AdvisoryDisplayListBuilder.AdvisoryDataType.kHeader -> AdvisoryHeaderViewHolder.NewInstance(inParent) as RecyclerViewHolder<Any, *>
            AdvisoryDisplayListBuilder.AdvisoryDataType.kContent -> AdvisoryViewHolder.NewInstance(inParent) as RecyclerViewHolder<Any, *>
        }
    }
}
