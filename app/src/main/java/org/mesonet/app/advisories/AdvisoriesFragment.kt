package org.mesonet.app.advisories

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import org.mesonet.app.R
import org.mesonet.app.baseclasses.BaseFragment
import org.mesonet.app.databinding.AdvisoriesFragmentBinding
import org.mesonet.dataprocessiFilenamevisories.AdvisoryListBuilder
import org.mesonet.dataprocessing.advisories.AdvisoryDataProvider

import java.util.Observable
import java.util.Observer

import javax.inject.Inject


class AdvisoriesFragment : BaseFragment(), Observer {
    private var mBinding: AdvisoriesFragmentBinding? = null

    @Inject
    internal lateinit var mAdvisoryDataProvider: AdvisoryDataProvider

    @Inject
    internal lateinit var mAdvisoryListBuilder: AdvisoryListBuilder


    override fun onCreateView(inInflater: LayoutInflater, inParent: ViewGroup?, inSavedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inInflater, R.layout.advisories_fragment, inParent, false)

        mAdvisoryDataProvider.addObserver(this)
        mBinding!!.advisoriesRecyclerView.setAdapter(AdvisoriesRecyclerViewAdapter())

        return mBinding!!.root
    }


    override fun update(o: Observable, arg: Any?) {
        if(activity != null && isAdded()) {
            activity!!.runOnUiThread({
                mAdvisoryListBuilder.BuildList(mAdvisoryDataProvider.GetAdvisories(), object : AdvisoryListBuilder.AdvisoryListListener {
                    override fun ListComplete(inResult: MutableList<Pair<AdvisoryListBuilder.AdvisoryDataType, AdvisoryListBuilder.AdvisoryData>>) {
                        if(activity != null) {
                            activity?.runOnUiThread({
                                if (mBinding != null) {
                                    mBinding?.progressBar?.visibility = View.GONE
                                    mBinding?.advisoriesRecyclerView?.SetItems(inResult)

                                    if(inResult.size == 0)
                                        mBinding?.emptyListText?.visibility = View.VISIBLE
                                }
                            })
                        }
                    }
                })
            })
        }
    }
}
