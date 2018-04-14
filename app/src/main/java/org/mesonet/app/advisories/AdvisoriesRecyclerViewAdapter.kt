package org.mesonet.app.advisories

import android.view.ViewGroup

import org.mesonet.app.baseclasses.RecyclerViewAdapter
import org.mesonet.app.baseclasses.RecyclerViewHolder


class AdvisoriesRecyclerViewAdapter : RecyclerViewAdapter<Any, RecyclerViewHolder<Any, *>>() {
    internal enum class AdvisoryViewHolderType {
        kHeader, kContent
    }

    override fun getItemViewType(inPosition: Int): Int {
        if (inPosition >= 0 && inPosition < mDataItems!!.size) {
            if (mDataItems?.get(inPosition) is AdvisoryModel.AdvisoryTypeModel)
                return AdvisoryViewHolderType.kHeader.ordinal

            if (mDataItems?.get(inPosition) is AdvisoryModel)
                return AdvisoryViewHolderType.kContent.ordinal
        }

        return -1
    }


    override fun onCreateViewHolder(inParent: ViewGroup, inViewType: Int): RecyclerViewHolder<Any, *> {
        return when (AdvisoryViewHolderType.values()[inViewType]) {
            AdvisoriesRecyclerViewAdapter.AdvisoryViewHolderType.kHeader -> AdvisoryHeaderViewHolder.NewInstance(inParent) as RecyclerViewHolder<Any, *>
            AdvisoriesRecyclerViewAdapter.AdvisoryViewHolderType.kContent -> AdvisoryViewHolder.NewInstance(inParent) as RecyclerViewHolder<Any, *>
        }
    }
}
