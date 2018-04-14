package org.mesonet.app.maps

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import org.mesonet.app.R
import org.mesonet.app.baseclasses.BaseActivity
import org.mesonet.app.baseclasses.BaseFragment
import org.mesonet.app.databinding.MapListFragmentBinding

import java.util.ArrayList

import javax.inject.Inject


class MapListFragment : BaseFragment() {

    internal lateinit var mBinding: MapListFragmentBinding

    @Inject
    lateinit var mDataProvider: MapsDataProvider

    @Inject
    lateinit var mActivity: BaseActivity


    override fun onCreateView(inInflater: LayoutInflater, inParent: ViewGroup?, inSavedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inInflater, R.layout.map_list_fragment, inParent, false)

        mBinding.mapList.setAdapter(MapsRecyclerViewAdapter(mActivity))

        mDataProvider.GetMaps(object : BaseMapsDataProvider.MapsListListener {
            override fun ListLoaded(inMapModel: MapsModel) {

                var selectedGroup: Int? = null
                val args = getArguments()

                if (args != null && args.containsKey(kSelectedGroup))
                    selectedGroup = args.getInt(kSelectedGroup)

                val sortingResult = ArrayList<Any>()
                val main = inMapModel.GetMain()
                val sections = inMapModel.GetSections()
                val products = inMapModel.GetProducts()

                if (main != null) {
                    if (selectedGroup != null && selectedGroup >= 0 && selectedGroup < main.size) {
                        val sectionIds = main.get(selectedGroup).GetSections()

                        for (i in sectionIds?.indices!!) {
                            val section = sections?.get(sectionIds[i])

                            if (section != null) {
                                if (section.GetTitle() != null && !section.GetTitle()!!.isEmpty())
                                    sortingResult.add(section.GetTitle()!!)

                                sortingResult.addAll(SectionMaps(section, products, false))
                            }
                        }

                        mBinding.groupNameToolbar.title = main.get(selectedGroup).GetTitle()
                        mBinding.groupNameToolbar.visibility = View.VISIBLE
                    } else {
                        for (i in main.indices) {
                            if (main[i] != null) {
                                val mainSections = main.get(i).GetSections()

                                if (mainSections != null && mainSections.size > 0) {
                                    main[i].GetTitle()?.let { sortingResult.add(it) }

                                    val sectionProducts = ArrayList<Any>()
                                    for (j in mainSections.indices) {
                                        if (sections?.containsKey(mainSections[j])!!) {
                                            val section = sections.get(mainSections[j])

                                            sectionProducts.addAll(SectionMaps(section, products, true))
                                        }
                                    }

                                    sortingResult.add(Pair<Int, List<Any>>(i, sectionProducts))
                                }
                            }
                        }
                    }
                }

                mBinding.mapList.SetItems(sortingResult)
            }


            private fun SectionMaps(inSection: MapsModel.SectionModel?, inProducts: Map<String, MapsModel.ProductModel>?, inShowSection: Boolean): List<Any> {
                val result = ArrayList<Any>()
                for (k in 0 until inSection?.GetProducts()!!.size) {
                    result.add(object : MapsProductViewHolder.MapsProductViewHolderData {
                        override fun Product(): String {
                            return inProducts?.get(inSection.GetProducts()!![k])?.mTitle!!
                        }

                        override fun Section(): String? {
                            return if (!inShowSection) null else inSection.GetTitle()

                        }

                        override fun Url(): String {
                            return inProducts?.get(inSection.GetProducts()!![k])?.mUrl!!
                        }
                    })
                }

                return result
            }
        })

        return mBinding.root
    }

    companion object {
        val kSelectedGroup = "selectedSection"
    }
}
