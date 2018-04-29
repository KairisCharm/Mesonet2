package org.mesonet.app.advisories

import android.view.ViewGroup

import org.mesonet.app.baseclasses.RecyclerViewAdapter
import org.mesonet.app.baseclasses.RecyclerViewHolder
import org.mesonet.dataprocessiFilenamevisories.AdvisoryListBuilder


class AdvisoriesRecyclerViewAdapter : RecyclerViewAdapter<Pair<AdvisoryListBuilder.AdvisoryDataType, AdvisoryListBuilder.AdvisoryData>, RecyclerViewHolder<Any, *>>() {


    override fun getItemViewType(inPosition: Int): Int {
        if(mDataItems != null && inPosition >= 0 && inPosition < mDataItems!!.size && mDataItems?.get(inPosition) != null && mDataItems?.get(inPosition)?.first != null)
        {
            return mDataItems?.get(inPosition)?.first!!.ordinal
        }

        return -1
    }


    override fun onCreateViewHolder(inParent: ViewGroup, inViewType: Int): RecyclerViewHolder<Any, *> {
        return when (AdvisoryListBuilder.AdvisoryDataType.values()[inViewType]) {
            AdvisoryListBuilder.AdvisoryDataType.kHeader -> AdvisoryHeaderViewHolder.NewInstance(inParent) as RecyclerViewHolder<Any, *>
            AdvisoryListBuilder.AdvisoryDataType.kContent -> AdvisoryViewHolder.NewInstance(inParent) as RecyclerViewHolder<Any, *>
        }
    }
}
