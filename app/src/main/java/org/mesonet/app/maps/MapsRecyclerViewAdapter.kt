package org.mesonet.app.maps

import android.databinding.ViewDataBinding
import android.util.Pair
import android.view.ViewGroup

import org.mesonet.app.baseclasses.BaseActivity
import org.mesonet.app.baseclasses.RecyclerViewAdapter
import org.mesonet.app.baseclasses.RecyclerViewHolder


open class MapsRecyclerViewAdapter(private val mBaseActivity: BaseActivity?) : RecyclerViewAdapter<Any, RecyclerViewHolder<Any, ViewDataBinding>>() {
    internal enum class MapViewHolderTypes {
        kHeader, kProduct, kGroup
    }

    override fun getItemViewType(inPosition: Int): Int {
        if (inPosition >= 0 && inPosition < mDataItems?.size!!) {
            if (mDataItems?.get(inPosition) is String)
                return MapViewHolderTypes.kHeader.ordinal

            if (mDataItems?.get(inPosition) is MapsProductViewHolder.MapsProductViewHolderData)
                return MapViewHolderTypes.kProduct.ordinal

            if (mDataItems?.get(inPosition) is Pair<*, *>)
                return MapViewHolderTypes.kGroup.ordinal
        }

        return -1
    }


    override fun onCreateViewHolder(inParent: ViewGroup, inViewType: Int): RecyclerViewHolder<Any, ViewDataBinding> {

        try {
            return when (MapViewHolderTypes.values()[inViewType]) {
                MapsRecyclerViewAdapter.MapViewHolderTypes.kHeader -> MapsHeaderViewHolder.NewInstance(inParent) as RecyclerViewHolder<Any, ViewDataBinding>
                MapsRecyclerViewAdapter.MapViewHolderTypes.kProduct -> MapsProductViewHolder.NewInstance(mBaseActivity?.supportFragmentManager!!, inParent) as RecyclerViewHolder<Any, ViewDataBinding>
                MapsRecyclerViewAdapter.MapViewHolderTypes.kGroup -> mBaseActivity?.let { MapsGroupViewHolder.NewInstance(it, inParent) } as RecyclerViewHolder<Any, ViewDataBinding>
            }
        } catch (e: ArrayIndexOutOfBoundsException) {
            e.printStackTrace()
        }

        return MapsHeaderViewHolder.NewInstance(inParent) as RecyclerViewHolder<Any, ViewDataBinding>
    }
}
