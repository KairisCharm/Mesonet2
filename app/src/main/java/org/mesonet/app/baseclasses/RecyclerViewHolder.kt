package org.mesonet.app.baseclasses

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView


abstract class RecyclerViewHolder<in TData : Any?, out TViewDataBinding : ViewDataBinding>(protected var mBinding: ViewDataBinding) : RecyclerView.ViewHolder(mBinding.root) {


    abstract fun SetData(inData: Any?)


    fun GetBinding(): TViewDataBinding? {
        try {
            return mBinding as TViewDataBinding
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }

        return null
    }
}
