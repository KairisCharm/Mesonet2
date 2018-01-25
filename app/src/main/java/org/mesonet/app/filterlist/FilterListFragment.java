package org.mesonet.app.filterlist;


import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.mesonet.app.BasicViewHolder;
import org.mesonet.app.R;
import org.mesonet.app.baseclasses.RecyclerViewAdapter;
import org.mesonet.app.databinding.FilterListFragmentBinding;
import org.mesonet.app.baseclasses.BaseFragment;
import org.mesonet.app.site.SiteSelectionInterfaces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.inject.Inject;

import kotlin.Pair;



public class FilterListFragment extends BaseFragment implements SiteSelectionInterfaces.SelectSiteListener, Observer
{
    TextWatcher mTextChangedListener;

    FilterListFragmentBinding mBinding;

    @Inject
    FilterListDataProvider mFilterListData;

    @Inject
    FilterListCloser mFilterListCloser;

    @Inject
    SiteSelectionInterfaces.SelectSiteListener mSelectedListener;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.filter_list_fragment, container, false);

        mBinding.searchList.setAdapter(new FilterListAdapter());

        Drawable closeDrawable = getResources().getDrawable(R.drawable.ic_close_white_36dp);

        mBinding.siteSelectionToolbar.setNavigationIcon(closeDrawable);
        mBinding.siteSelectionToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFilterListCloser.Close();
            }
        });

        mBinding.siteSelectionToolbar.inflateMenu(R.menu.search_list_menu);

        mBinding.siteSelectionToolbar.getMenu().findItem(R.id.nearestLocation).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return false;
            }
        });



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            closeDrawable.setTint(getResources().getColor(R.color.blueTextColor, getActivity().getTheme()));
            mBinding.searchText.setTextColor(getResources().getColor(R.color.blueTextColor, getActivity().getTheme()));
        }
        else
        {
            closeDrawable.setTint(getResources().getColor(R.color.blueTextColor));
            mBinding.searchText.setTextColor(getResources().getColor(R.color.blueTextColor));
        }

        mTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                FillList();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        Map<String, BasicViewHolder.BasicViewHolderData> data = mFilterListData.AllData();

        if(data != null && data.containsKey(mFilterListData.CurrentSelection()))
            mBinding.searchText.setText(data.get(mFilterListData.CurrentSelection()).GetName());
        mBinding.searchText.addTextChangedListener(mTextChangedListener);

        mFilterListData.GetDataObservable().addObserver(this);

        FillList();

        return mBinding.getRoot();
    }



    @Override
    public void onDestroyView()
    {
        mFilterListData.GetDataObservable().deleteObserver(this);
        mBinding.searchText.removeTextChangedListener(mTextChangedListener);
        mTextChangedListener = null;
        super.onDestroyView();
    }



    @Override
    public void SetResult(String inResult) {
        mFilterListCloser.Close();
        mSelectedListener.SetResult(inResult);
    }



    private void FillList()
    {
        mBinding.searchList.SetItems(new ArrayList());

        Map<String, BasicViewHolder.BasicViewHolderData> data = mFilterListData.AllData();

        if(data != null) {
            mBinding.searchList.SetItems(SortList(mBinding.searchText.getText().toString(), new MapToListOfPairs().Create(data)));
        }
    }



    private List<Pair<String, BasicViewHolder.BasicViewHolderData>> SortList(final String inCurrentValue, List<Pair<String, BasicViewHolder.BasicViewHolderData>> inSearchFields)
    {
        Collections.sort(inSearchFields, new Comparator<Pair<String, BasicViewHolder.BasicViewHolderData>>() {
            @Override
            public int compare(Pair<String, BasicViewHolder.BasicViewHolderData> stringPairPair, Pair<String, BasicViewHolder.BasicViewHolderData> t1) {
                String name1 = stringPairPair.getSecond().GetName();
                String name2 = t1.getSecond().GetName();

                int result = 0;

                if(stringPairPair.getSecond().IsFavorite() && !t1.getSecond().IsFavorite())
                    result = -1;

                if(!stringPairPair.getSecond().IsFavorite() && t1.getSecond().IsFavorite())
                    result = 1;

                if(result == 0)
                    result = HasSearchValue(inCurrentValue, name1, name2);

                if(result == 0)
                    result = HasSearchValue(inCurrentValue, name2, name1);

                if(result == 0)
                    result = name1.compareTo(name2);

                return result;
            }



            private int HasSearchValue(String inSearchValue, String inName1, String inName2)
            {
                String lowerSearchValue = inSearchValue.toLowerCase();
                String lowerName1 = inName1.toLowerCase();
                String lowerName2 = inName2.toLowerCase();

                if(lowerName1.equals(lowerSearchValue) && !lowerName2.equals(lowerSearchValue))
                    return -1;

                if(lowerName2.equals(lowerSearchValue) && !lowerName1.equals(lowerSearchValue))
                    return 1;

                if(lowerName1.contains(lowerSearchValue) && !lowerName2.contains(lowerSearchValue))
                    return -1;

                if(lowerName2.contains(lowerSearchValue) && !lowerName1.contains(lowerSearchValue))
                    return 1;

                return 0;
            }
        });

        return inSearchFields;
    }

    @Override
    public void update(Observable observable, Object o) {
        FillList();
    }


    public interface FilterListCloser
    {
        void Close();
    }



    public interface FilterListDataProvider
    {
        Map<String, BasicViewHolder.BasicViewHolderData> AllData();
        String CurrentSelection();
        Observable GetDataObservable();
    }



    private class FilterListAdapter extends RecyclerViewAdapter<Pair<String, String>, BasicViewHolder>
    {
        @Override
        public BasicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new BasicViewHolder(parent, FilterListFragment.this);
        }
    }
}
