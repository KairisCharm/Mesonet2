package org.mesonet.app.maps

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import org.mesonet.app.R
import org.mesonet.app.baseclasses.BaseActivity
import org.mesonet.app.baseclasses.BaseFragment
import org.mesonet.app.databinding.MapListFragmentBinding
import org.mesonet.dataprocessing.maps.MapsDataProvider

import javax.inject.Inject


class MapListFragment : BaseFragment() {

    private lateinit var mBinding: MapListFragmentBinding

    @Inject
    internal lateinit var mDataProvider: MapsDataProvider

    @Inject
    internal lateinit var mActivity: BaseActivity


    override fun onCreateView(inInflater: LayoutInflater, inParent: ViewGroup?, inSavedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inInflater, R.layout.map_list_fragment, inParent, false)

        mBinding.mapList.setAdapter(MapsRecyclerViewAdapter(mActivity))

        var selectedGroup: Int? = null
        val args = getArguments()

        if (args != null && args.containsKey(kSelectedGroup))
            selectedGroup = args.getInt(kSelectedGroup)

        mDataProvider.Download(selectedGroup, object : MapsDataProvider.MapsListListener {
            override fun ListLoaded(inMapsList: MutableList<Any>?, inGroupName: String?) {
                if(activity != null && isAdded()) {
                    activity?.runOnUiThread({
                        mBinding.progressBar.visibility = View.GONE
                        if (inMapsList != null) {
                            if (inGroupName != null && !inGroupName.isEmpty()) {
                                mBinding.groupNameToolbar.title = inGroupName
                                mBinding.groupNameToolbar.visibility = View.VISIBLE
                            }
                            mBinding.mapList.SetItems(inMapsList)
                        }
                        if(inMapsList == null || inMapsList.size == 0)
                            mBinding.emptyListText.visibility = View.VISIBLE
                    })
                }
            }
        })

        return mBinding.root
    }

    companion object {
        val kSelectedGroup = "selectedSection"
    }
}
