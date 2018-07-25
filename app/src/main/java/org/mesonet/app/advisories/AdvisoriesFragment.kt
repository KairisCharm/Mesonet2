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

        mBinding?.advisoriesRecyclerView?.setAdapter(AdvisoriesRecyclerViewAdapter())

        return mBinding?.root
    }


    override fun onResume() {
        super.onResume()

        mAdvisoryDataProvider.GetDataObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(object: Observer<Advisory.AdvisoryList>
        {
            override fun onComplete() {}
            override fun onSubscribe(d: Disposable) {
                mAdvisoryDisposable = d
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(list: Advisory.AdvisoryList) {
                mAdvisoryListBuilder.BuildList(list).observeOn(AndroidSchedulers.mainThread()).subscribe(object : Observer<ArrayList<Pair<AdvisoryDisplayListBuilder.AdvisoryDataType, AdvisoryDisplayListBuilder.AdvisoryData>>> {
                    override fun onComplete() {}
                    override fun onSubscribe(d: Disposable) {}

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        if (mBinding != null) {
                            mBinding?.progressBar?.visibility = View.GONE
                            mBinding?.emptyListText?.visibility = View.VISIBLE
                        }
                    }

                    override fun onNext(t: ArrayList<Pair<AdvisoryDisplayListBuilder.AdvisoryDataType, AdvisoryDisplayListBuilder.AdvisoryData>>) {
                        if (mBinding != null) {
                            mBinding?.progressBar?.visibility = View.GONE
                            mBinding?.advisoriesRecyclerView?.SetItems(t)

                            if (list.size == 0)
                                mBinding?.emptyListText?.visibility = View.VISIBLE
                            else
                                mBinding?.emptyListText?.visibility = View.GONE
                        }
                    }
                })
            }
        })
    }


    override fun onPause() {
        mAdvisoryDisposable?.dispose()
        super.onPause()
    }
}
