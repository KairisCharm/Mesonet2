package org.mesonet.app.advisories

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

import org.mesonet.app.R
import org.mesonet.app.baseclasses.BaseFragment
import org.mesonet.app.databinding.AdvisoriesFragmentBinding
import org.mesonet.dataprocessing.advisories.AdvisoryDataProvider
import org.mesonet.dataprocessing.advisories.AdvisoryDisplayListBuilder


import javax.inject.Inject


class AdvisoriesFragment : BaseFragment()
{
    private var mBinding: AdvisoriesFragmentBinding? = null

    @Inject
    internal lateinit var mAdvisoryDataProvider: AdvisoryDataProvider

    @Inject
    internal lateinit var mAdvisoryListBuilder: AdvisoryDisplayListBuilder

    var mAdvisoryDisposable: Disposable? = null



    override fun onCreateView(inInflater: LayoutInflater, inParent: ViewGroup?, inSavedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inInflater, R.layout.advisories_fragment, inParent, false)

        mBinding!!.advisoriesRecyclerView.setAdapter(AdvisoriesRecyclerViewAdapter())

        return mBinding!!.root
    }


    override fun onResume() {
        super.onResume()

        mAdvisoryDisposable = mAdvisoryDataProvider.GetDataObservable().observeOn(AndroidSchedulers.mainThread()).subscribe{ list ->
            mAdvisoryListBuilder.BuildList(list).observeOn(AndroidSchedulers.mainThread()).subscribe {
                if (mBinding != null) {
                    mBinding?.progressBar?.visibility = View.GONE
                    mBinding?.advisoriesRecyclerView?.SetItems(it as ArrayList<*>)

                    if(list.size == 0)
                        mBinding?.emptyListText?.visibility = View.VISIBLE
                    else
                        mBinding?.emptyListText?.visibility = View.GONE
                }
            }
        }
    }


    override fun onPause() {
        mAdvisoryDisposable?.dispose()
        super.onPause()
    }
}
