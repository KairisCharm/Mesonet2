package org.mesonet.app.baseclasses;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

import java.util.List;



public class RecyclerView<TRecyclerViewAdapter extends RecyclerViewAdapter> extends android.support.v7.widget.RecyclerView
{
    public RecyclerView(Context inContext)
    {
        this(inContext, null);
    }



    public RecyclerView(Context inContext, @Nullable AttributeSet inAttrs)
    {
        this(inContext, inAttrs, 0);
    }



    public RecyclerView(Context inContext, @Nullable AttributeSet inAttrs, int inDefStyle)
    {
        super(inContext, inAttrs, inDefStyle);

        setHasFixedSize(true);
        setLayoutManager(new LinearLayoutManager(inContext, VERTICAL, false));
    }



    public TRecyclerViewAdapter GetAdapter()
    {
        try {
            return (TRecyclerViewAdapter) getAdapter();
        }
        catch (ClassCastException e)
        {
            e.printStackTrace();
        }

        return null;
    }



    public synchronized void SetItems(List inData)
    {
        getRecycledViewPool().clear();
        GetAdapter().SetItems(inData);
    }



    @Override
    public void finalize()
    {
        setAdapter(null);
    }
}
