package org.mesonet.app.baseclasses;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;



public abstract class RecyclerViewAdapter<TData, TRecyclerViewHolder extends RecyclerViewHolder> extends RecyclerView.Adapter<TRecyclerViewHolder>
{
    protected boolean mUsePagination = false;
    protected List<TData> mDataItems = new ArrayList<>();
    protected RecyclerView mRecyclerView;
    protected List<TRecyclerViewHolder> mRecyclerViewHolders = new ArrayList<>();

    protected List<ScrolledToEndListener> mScrolledToEndListeners = new ArrayList<>();

    private RecyclerView.OnScrollListener kOnScrollListener;



    public RecyclerViewAdapter()
    {
        setHasStableIds(true);

        kOnScrollListener = new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView inRecyclerView, int inDx, int inDy)
            {
                super.onScrolled(inRecyclerView, inDx, inDy);

                RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();

                if (mDataItems != null)
                {
                    if (((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition() == mDataItems.size())
                    {
                        for (ScrolledToEndListener scrolledToEndListener : mScrolledToEndListeners)
                            scrolledToEndListener.OnScrolledToEnd();
                    }
                }
            }
        };
    }



    @Override
    public int getItemCount()
    {
        int result = 0;

        if (mDataItems != null)
            result = mDataItems.size();

        if (mUsePagination)
            result++;

        return result;
    }



    @Override
    public void onAttachedToRecyclerView(RecyclerView inRecyclerView)
    {
        mRecyclerView = inRecyclerView;
        mRecyclerView.addOnScrollListener(kOnScrollListener);
    }



    @Override
    public void onBindViewHolder(TRecyclerViewHolder inHolder, int inPosition)
    {
        if (inPosition < mDataItems.size())
        {
            TData data = mDataItems.get(inPosition);

            inHolder.SetData(data);
        }
    }



    @Override
    public long getItemId(int inPostition)
    {
        return inPostition;
    }



    public void SetItems(final List<TData> inDataItems)
    {
        SetItems(inDataItems, 0);
    }



    public void SetItems(List<TData> inDataItems, int inTotalItems)
    {
        if (mDataItems == inDataItems)
        {
            return;
        }

        mDataItems = inDataItems;
        mUsePagination = (mDataItems == null ? 0 : mDataItems.size()) < inTotalItems;
        notifyDataSetChanged();
    }



    public void AddItems(List<TData> inDataItems)
    {
        AddItems(inDataItems, 0);
    }

    public void AddItems(List<TData> inDataItems, int inTotalItems)
    {
        if (mDataItems == null)
            mDataItems = new ArrayList<>();

        mDataItems.addAll(inDataItems);
        mUsePagination = mDataItems.size() < inTotalItems;
        notifyDataSetChanged();
    }

    public void ClearItems()
    {
        if (mDataItems != null)
        {
            mDataItems.clear();
            mUsePagination = false;
            notifyDataSetChanged();
        }
    }



    public int GetItemCount()
    {
        int count = getItemCount();

        if (mUsePagination)
            count--;

        return count;
    }



    public void AddScrolledToEndListener(final ScrolledToEndListener inScrolledToEndListener)
    {
        mScrolledToEndListeners.add(inScrolledToEndListener);
    }


    public void RemoveScrolledToEndListener(final ScrolledToEndListener inScrolledToEndListener){
        mScrolledToEndListeners.remove(inScrolledToEndListener);
    }


    public void AddViewHolder(TRecyclerViewHolder inTRecyclerViewHolder)
    {
        mRecyclerViewHolders.add(inTRecyclerViewHolder);
    }

    public List<TRecyclerViewHolder> GetViewHolders()
    {
        return mRecyclerViewHolders;
    }



    @Override
    public void finalize()
    {
        mRecyclerView.removeOnScrollListener(kOnScrollListener);

        kOnScrollListener = null;
        mScrolledToEndListeners = null;
        mRecyclerView = null;
    }



    public interface ScrolledToEndListener
    {
        void OnScrolledToEnd();
    }
}