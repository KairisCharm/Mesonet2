package org.mesonet.app.baseclasses;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;





public abstract class RecyclerViewHolder<TData extends Object, TViewDataBinding extends ViewDataBinding> extends RecyclerView.ViewHolder
{
    private ViewDataBinding mBinding;



    public RecyclerViewHolder(ViewDataBinding inBinding)
    {
        super(inBinding.getRoot());
        mBinding = inBinding;
    }



    public abstract void SetData(TData inData);



    public TViewDataBinding GetBinding()
    {
        try {
            return (TViewDataBinding)mBinding;
        }
        catch (ClassCastException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
