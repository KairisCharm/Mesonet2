package org.mesonet.app.baseclasses

import android.databinding.ViewDataBinding
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import java.util.ArrayList
import android.content.ContextWrapper
import android.app.Activity




abstract class RecyclerViewAdapter<TData, TRecyclerViewHolder : RecyclerViewHolder<TData, ViewDataBinding>> : RecyclerView.Adapter<TRecyclerViewHolder>() {
    protected var mUsePagination = false
    protected var mDataItems: MutableList<TData>? = ArrayList()
    protected var mRecyclerView: RecyclerView? = null
    protected var mRecyclerViewHolders: MutableList<TRecyclerViewHolder> = ArrayList()

    protected var mScrolledToEndListeners: MutableList<ScrolledToEndListener>? = ArrayList()

    private var kOnScrollListener: RecyclerView.OnScrollListener? = null

    init {
        setHasStableIds(true)

        kOnScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(inRecyclerView: RecyclerView, inDx: Int, inDy: Int) {
                super.onScrolled(inRecyclerView, inDx, inDy)

                val layoutManager = mRecyclerView?.layoutManager

                if (mDataItems != null) {
                    if ((layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() == mDataItems?.size) {
                        for (scrolledToEndListener in mScrolledToEndListeners?: ArrayList())
                            scrolledToEndListener.OnScrolledToEnd()
                    }
                }
            }
        }
    }


    override fun getItemCount(): Int {
        var result = 0

        if (mDataItems != null)
            result = mDataItems?.size ?: 0

        if (mUsePagination)
            result++

        return result
    }


    override fun onAttachedToRecyclerView(inRecyclerView: RecyclerView) {
        mRecyclerView = inRecyclerView
        if(kOnScrollListener != null)
            mRecyclerView?.addOnScrollListener(kOnScrollListener ?: object: RecyclerView.OnScrollListener(){})
    }


    override fun onBindViewHolder(inHolder: TRecyclerViewHolder, inPosition: Int) {
        if (inPosition < mDataItems?.size ?: 0) {
            val data = mDataItems?.get(inPosition)

            inHolder.SetData(data)
        }
    }


    override fun getItemId(inPostition: Int): Long
    {
        return inPostition.toLong()
    }


    internal fun SetItems(inDataItems: MutableList<TData>?, inTotalItems: Int = 0)
    {
        if (mDataItems === inDataItems)
            return

        mDataItems = inDataItems
        mUsePagination = (mDataItems?.size ?: 0) < inTotalItems

        dataSetChanged()
    }


    internal fun AddItems(inDataItems: List<TData>, inTotalItems: Int = 0) {
        if (mDataItems == null)
            mDataItems = ArrayList()

        mDataItems?.addAll(inDataItems)
        mUsePagination = mDataItems?.size ?: 0 < inTotalItems

        dataSetChanged()
    }


    internal fun ClearItems() {
        mDataItems?.clear()
        mUsePagination = false

        dataSetChanged()
    }

    private fun dataSetChanged() {
        GetActivity()?.runOnUiThread {
            notifyDataSetChanged()
        }
    }


    private fun GetActivity(): Activity? {
        var context = mRecyclerView?.context
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context
            }
            context = context.baseContext
        }
        return null
    }


    internal fun GetItemCount(): Int {
        var count = itemCount

        if (mUsePagination)
            count--

        return count
    }


    internal fun AddScrolledToEndListener(inScrolledToEndListener: ScrolledToEndListener) {
        mScrolledToEndListeners?.add(inScrolledToEndListener)
    }


    internal fun RemoveScrolledToEndListener(inScrolledToEndListener: ScrolledToEndListener) {
        mScrolledToEndListeners?.remove(inScrolledToEndListener)
    }


    internal fun AddViewHolder(inTRecyclerViewHolder: TRecyclerViewHolder) {
        mRecyclerViewHolders.add(inTRecyclerViewHolder)
    }

    internal fun GetViewHolders(): List<TRecyclerViewHolder> {
        return mRecyclerViewHolders
    }


    fun finalize() {
        mRecyclerView?.removeOnScrollListener(kOnScrollListener ?: object: RecyclerView.OnScrollListener(){})

        kOnScrollListener = null
        mScrolledToEndListeners = null
        mRecyclerView = null
    }


    interface ScrolledToEndListener {
        fun OnScrolledToEnd()
    }
}