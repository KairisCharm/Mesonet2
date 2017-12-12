package org.mesonet.app.filterlist;

import android.app.Fragment;
import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.mesonet.app.BasicViewHolder;
import org.mesonet.app.R;
import org.mesonet.app.baseclasses.RecyclerViewAdapter;
import org.mesonet.app.databinding.FilterListFragmentBinding;
import org.mesonet.app.mesonetdata.MesonetData;
import org.mesonet.app.mesonetdata.MesonetDataController;
import org.mesonet.app.mesonetdata.SiteSelectionInterfaces;

import java.util.Map;

import javax.inject.Inject;

import kotlin.Pair;


public class FilterListFragment extends Fragment
{
    FilterListFragmentBinding mBinding;

    @Inject
    MesonetData mMesonetDataController;

    @Inject
    Map<String, String> mKeysToDisplayText;

    @Inject
    FilterListCloser mCloser;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.filter_list_fragment, container, false);

        mBinding.searchList.setAdapter(new FilterListAdapter());
        mBinding.searchList.SetItems(new MapToListOfPairs().Create(mKeysToDisplayText));

        Drawable closeDrawable = getResources().getDrawable(R.drawable.ic_close_white_36dp);
        closeDrawable.setTint(getResources().getColor(R.color.blueTextColor, getActivity().getTheme()));
        mBinding.siteSelectionToolbar.setNavigationIcon(closeDrawable);
        mBinding.siteSelectionToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCloser.Close();
            }
        });

        mBinding.siteSelectionToolbar.getMenu().findItem(R.id.nearestLocation).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return false;
            }
        });

        mBinding.siteSelectionToolbar.inflateMenu(R.menu.search_list_menu);

        mBinding.searchText.setTextColor(getResources().getColor(R.color.blueTextColor, getActivity().getTheme()));

        return mBinding.getRoot();
    }



//    private void revealView(View view) {
//
//        int cx = (view.getLeft() + view.getRight()) / 2;
//        int cy = (view.getTop() + view.getBottom()) / 2;
//        float radius = Math.max(infoContainer.getWidth(), infoContainer.getHeight()) * 2.0f;
//
//        if (infoContainer.getVisibility() == View.INVISIBLE) {
//            infoContainer.setVisibility(View.VISIBLE);
//            ViewAnimationUtils.createCircularReveal(infoContainer, cx, cy, 0, radius).start();
//        } else {
//            Animator reveal = ViewAnimationUtils.createCircularReveal(
//                    infoContainer, cx, cy, radius, 0);
//            reveal.addListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                }
//            });
//            reveal.start();
//        }
//    }



//    @Override
//    public void setupDialog(final Dialog inDialog, int inStyle)
//    {
//        super.setupDialog(inDialog, inStyle);
//
//        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.filter_list_fragment, null, false);
//        mBinding.searchList.setAdapter(new FilterListAdapter());
//
//        List list = new MapToListOfPairs().Create(mKeysToDisplayText);
//        mBinding.searchList.SetItems(list);
//
//        inDialog.setContentView(mBinding.getRoot());
//    }


//
//    public void Show(FragmentManager inSupportFragmentManager, SiteSelectionInterfaces.SelectSiteListener inListener, Map<String, String> inKeysToDisplayText)
//    {
//        mMesonetDataController = inListener;
//        mKeysToDisplayText = inKeysToDisplayText;
//        show(inSupportFragmentManager, getTag());
//    }

    public interface FilterListCloser
    {
        void Close();
    }



    private class FilterListAdapter extends RecyclerViewAdapter<Pair<String, String>, BasicViewHolder>
    {
        @Override
        public BasicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new BasicViewHolder(parent);
        }
    }
}
