package org.mesonet.app.baseclasses

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet


class RecyclerView<TRecyclerViewAdapter : RecyclerViewAdapter<Any, RecyclerViewHolder<*,*>>> @JvmOverloads constructor(inContext: Context, inAttrs: AttributeSet? = null, inDefStyle: Int = 0) : android.support.v7.widget.RecyclerView(inContext, inAttrs, inDefStyle) {



    init {

        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(inContext, android.support.v7.widget.RecyclerView.VERTICAL, false)
    }


    fun GetAdapter(): TRecyclerViewAdapter? {
        try {
            return adapter as TRecyclerViewAdapter
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }

        return null
    }


    @Synchronized
    fun SetItems(inData: MutableList<Any>) {
        recycledViewPool.clear()
        GetAdapter()?.SetItems(inData)
    }


    fun finalize() {
        adapter = null
    }
}
