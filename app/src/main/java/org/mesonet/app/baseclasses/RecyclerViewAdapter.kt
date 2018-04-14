package org.mesonet.app.baseclasses

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import java.util.ArrayList


abstract class RecyclerViewAdapter<TData : Any?, TRecyclerViewHolder : RecyclerViewHolder<*, *>> : RecyclerView.Adapter<TRecyclerViewHolder>() {
    protected var mUsePagination = false
    protected var mDataItems: MutableList<TData>? = ArrayList()
    protected var mRecyclerView: RecyclerView? = null
    protected var mRecyclerViewHolders: MutableList<TRecyclerViewHolder> = ArrayList()

    protected var mScrolledToEndListeners: MutableList<ScrolledToEndListener>? = ArrayList()

    private var kOnScrollListener: RecyclerView.OnScrollListener? = null

    init {
        setHasStableIds(true)

        kOnScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(inRecyclerView: RecyclerView?, inDx: Int, inDy: Int) {
                super.onScrolled(inRecyclerView, inDx, inDy)

                val layoutManager = mRecyclerView!!.layoutManager

                if (mDataItems != null) {
                    if ((layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() == mDataItems!!.size) {
                        for (scrolledToEndListener in mScrolledToEndListeners!!)
                            scrolledToEndListener.OnScrolledToEnd()
                    }
                }
            }
        }
    }


    override fun getItemCount(): Int {
        var result = 0

        if (mDataItems != null)
            result = mDataItems!!.size

        if (mUsePagination)
            result++

        return result
    }


    override fun onAttachedToRecyclerView(inRecyclerView: RecyclerView) {
        mRecyclerView = inRecyclerView
        mRecyclerView!!.addOnScrollListener(kOnScrollListener)
    }


    override fun onBindViewHolder(inHolder: TRecyclerViewHolder, inPosition: Int) {
        if (inPosition < mDataItems!!.size) {
            val data = mDataItems!![inPosition]

            inHolder.SetData(data)
        }
    }


    override fun getItemId(inPostition: Int): Long
    {
        return inPostition.toLong()
    }


    fun SetItems(inDataItems: MutableList<TData>?, inTotalItems: Int = 0)
    {
        if (mDataItems === inDataItems)
            return

        mDataItems = inDataItems
        mUsePagination = (if (mDataItems == null) 0 else mDataItems!!.size) < inTotalItems
        notifyDataSetChanged()
    }


    fun AddItems(inDataItems: List<TData>, inTotalItems: Int = 0) {
        if (mDataItems == null)
            mDataItems = ArrayList()

        mDataItems!!.addAll(inDataItems)
        mUsePagination = mDataItems!!.size < inTotalItems
        notifyDataSetChanged()
    }

    fun ClearItems() {
        if (mDataItems != null) {
            mDataItems!!.clear()
            mUsePagination = false
            notifyDataSetChanged()
        }
    }


    fun GetItemCount(): Int {
        var count = itemCount

        if (mUsePagination)
            count--

        return count
    }


    fun AddScrolledToEndListener(inScrolledToEndListener: ScrolledToEndListener) {
        mScrolledToEndListeners!!.add(inScrolledToEndListener)
    }


    fun RemoveScrolledToEndListener(inScrolledToEndListener: ScrolledToEndListener) {
        mScrolledToEndListeners!!.remove(inScrolledToEndListener)
    }


    fun AddViewHolder(inTRecyclerViewHolder: TRecyclerViewHolder) {
        mRecyclerViewHolders.add(inTRecyclerViewHolder)
    }

    fun GetViewHolders(): List<TRecyclerViewHolder> {
        return mRecyclerViewHolders
    }


    fun finalize() {
        mRecyclerView!!.removeOnScrollListener(kOnScrollListener)

        kOnScrollListener = null
        mScrolledToEndListeners = null
        mRecyclerView = null
    }


    interface ScrolledToEndListener {
        fun OnScrolledToEnd()
    }
}