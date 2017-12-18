package org.mesonet.app.filterlist;


import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.mesonet.app.BasicViewHolder;
import org.mesonet.app.R;
import org.mesonet.app.baseclasses.RecyclerViewAdapter;
import org.mesonet.app.databinding.FilterListFragmentBinding;
import org.mesonet.app.dependencyinjection.BaseFragment;
import org.mesonet.app.site.SiteSelectionInterfaces;

import java.util.Map;

import javax.inject.Inject;

import kotlin.Pair;



public class FilterListFragment extends BaseFragment implements SiteSelectionInterfaces.SelectSiteListener
{
    FilterListFragmentBinding mBinding;

    @Inject
    Map<String, Pair<String, Location>> mKeysToDisplayText;

    @Inject
    FilterListCloser mFilterListCloser;

    @Inject
    SiteSelectionInterfaces.SelectSiteListener mSelectedListener;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.filter_list_fragment, container, false);

        mBinding.searchList.setAdapter(new FilterListAdapter());
        mBinding.searchList.SetItems(new MapToListOfPairs().Create(mKeysToDisplayText));

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

        return mBinding.getRoot();
    }



    @Override
    public void SetResult(String inResult) {
        mFilterListCloser.Close();
        mSelectedListener.SetResult(inResult);
    }



    public interface FilterListCloser
    {
        void Close();
    }



    private class FilterListAdapter extends RecyclerViewAdapter<Pair<String, String>, BasicViewHolder>
    {
        @Override
        public BasicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new BasicViewHolder(parent, FilterListFragment.this);
        }
    }
}
