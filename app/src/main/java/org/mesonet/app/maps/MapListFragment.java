package org.mesonet.app.maps;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mesonet.app.R;
import org.mesonet.app.baseclasses.BaseActivity;
import org.mesonet.app.baseclasses.BaseFragment;
import org.mesonet.app.databinding.MapListFragmentBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;


public class MapListFragment extends BaseFragment
{
    public static final String kSelectedGroup = "selectedSection";

    MapListFragmentBinding mBinding;

    @Inject
    MapsDataProvider mDataProvider;

    @Inject
    BaseActivity mActivity;


    @Override
    public View onCreateView(LayoutInflater inInflater, ViewGroup inParent, final Bundle inSavedInstanceState)
    {
        mBinding = DataBindingUtil.inflate(inInflater, R.layout.map_list_fragment, inParent, false);

        mBinding.mapList.setAdapter(new MapsRecyclerViewAdapter(mActivity));

        mDataProvider.GetMaps(new BaseMapsDataProvider.MapsListListener() {
            @Override
            public void ListLoaded(MapsModel inMapModel) {

                Integer selectedGroup = null;
                Bundle args = getArguments();

                if(args != null && args.containsKey(kSelectedGroup))
                    selectedGroup = args.getInt(kSelectedGroup);

                List<Object> sortingResult = new ArrayList<>();
                List<MapsModel.MainModel> main = inMapModel.GetMain();
                Map<String, MapsModel.SectionModel> sections = inMapModel.GetSections();
                Map<String, MapsModel.ProductModel> products = inMapModel.GetProducts();

                if(selectedGroup != null && selectedGroup >= 0 && selectedGroup < main.size())
                {
                    List<String> sectionIds = main.get(selectedGroup).GetSections();

                    for(int i = 0; i < sectionIds.size(); i++)
                    {
                        MapsModel.SectionModel section = sections.get(sectionIds.get(i));

                        if(section != null) {
                            if (section.GetTitle() != null && !section.GetTitle().isEmpty())
                                sortingResult.add(section.GetTitle());

                            sortingResult.addAll(SectionMaps(section, products, false));
                        }
                    }

                    mBinding.groupNameToolbar.setTitle(main.get(selectedGroup).GetTitle());
                    mBinding.groupNameToolbar.setVisibility(View.VISIBLE);
                }
                else {
                    for (int i = 0; i < main.size(); i++) {
                        if (main.get(i) != null) {
                            List<String> mainSections = main.get(i).GetSections();

                            if (mainSections != null && mainSections.size() > 0) {
                                sortingResult.add(main.get(i).GetTitle());

                                List<Object> sectionProducts = new ArrayList<>();
                                for (int j = 0; j < mainSections.size(); j++) {
                                    if (sections.containsKey(mainSections.get(j))) {
                                        MapsModel.SectionModel section = sections.get(mainSections.get(j));

                                        sectionProducts.addAll(SectionMaps(section, products, true));
                                    }
                                }

                                sortingResult.add(new Pair<>(i, sectionProducts));
                            }
                        }
                    }
                }

                mBinding.mapList.SetItems(sortingResult);
            }



            private List<Object> SectionMaps(final MapsModel.SectionModel inSection, final Map<String, MapsModel.ProductModel> inProducts, final boolean inShowSection)
            {
                List<Object> result = new ArrayList<>();
                for(int k = 0; k < inSection.GetProducts().size(); k++)
                {
                    final int index = k;
                    result.add(new MapsProductViewHolder.MapsProductViewHolderData() {
                        @Override
                        public String Product() {
                            return inProducts.get(inSection.GetProducts().get(index)).mTitle;
                        }

                        @Override
                        public String Section() {
                            if(!inShowSection)
                                return null;

                            return inSection.GetTitle();
                        }

                        @Override
                        public String Url() {
                            return inProducts.get(inSection.GetProducts().get(index)).mUrl;
                        }
                    });
                }

                return result;
            }
        });

        return mBinding.getRoot();
    }
}
