package org.mesonet.app.advisories

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

import org.mesonet.app.R
import org.mesonet.app.baseclasses.BaseFragment
import org.mesonet.app.databinding.AdvisoriesFragmentBinding
import org.mesonet.dataprocessing.advisories.AdvisoryDataProvider
import org.mesonet.dataprocessing.advisories.AdvisoryDisplayListBuilder
import org.mesonet.models.advisories.Advisory
import java.util.concurrent.TimeUnit


import javax.inject.Inject


class AdvisoriesFragment : BaseFragment(), Observer<MutableList<Advisory>> {
    private var mBinding: AdvisoriesFragmentBinding? = null

    @Inject
    internal lateinit var mAdvisoryDataProvider: AdvisoryDataProvider

    @Inject
    internal lateinit var mAdvisoryListBuilder: AdvisoryDisplayListBuilder

    private var mDisposable: Disposable? = null

    override fun onCreateView(inInflater: LayoutInflater, inParent: ViewGroup?, inSavedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inInflater, R.layout.advisories_fragment, inParent, false)

        mDisposable = Observable.timer(1, TimeUnit.MINUTES).observeOn(AndroidSchedulers.mainThread()).subscribe {
            mAdvisoryDataProvider.subscribe(this)
        }

        mBinding!!.advisoriesRecyclerView.setAdapter(AdvisoriesRecyclerViewAdapter())

        return mBinding!!.root
    }


    override fun onComplete() {}
    override fun onSubscribe(d: Disposable) {}
    override fun onError(e: Throwable) {}

    override fun onNext(t: MutableList<Advisory>) {

        mAdvisoryListBuilder.BuildList(t).observeOn(AndroidSchedulers.mainThread()).subscribe {
            if (mBinding != null) {
                mBinding?.progressBar?.visibility = View.GONE
                mBinding?.advisoriesRecyclerView?.SetItems(t)

                if(t.size == 0)
                    mBinding?.emptyListText?.visibility = View.VISIBLE
            }
        }
    }


    override fun onDestroyView() {
        mDisposable?.dispose()
        super.onDestroyView()
    }
}
