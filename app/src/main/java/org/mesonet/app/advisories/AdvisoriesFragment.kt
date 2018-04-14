package org.mesonet.app.advisories

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import org.mesonet.app.R
import org.mesonet.app.baseclasses.BaseFragment
import org.mesonet.app.databinding.AdvisoriesFragmentBinding

import java.util.ArrayList
import java.util.Observable
import java.util.Observer

import javax.inject.Inject


class AdvisoriesFragment : BaseFragment(), Observer {
    private var mBinding: AdvisoriesFragmentBinding? = null

    @Inject
    lateinit var mAdvisoryDataProvider: AdvisoryDataProvider

    @Inject
    lateinit var mAdvisorySorter: AdvisorySorter


    override fun onCreateView(inInflater: LayoutInflater, inParent: ViewGroup?, inSavedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inInflater, R.layout.advisories_fragment, inParent, false)

        mAdvisoryDataProvider.addObserver(this)
        mBinding!!.advisoriesRecyclerView.setAdapter(AdvisoriesRecyclerViewAdapter())

        return mBinding!!.root
    }


    override fun update(o: Observable, arg: Any?) {
        var advisories: List<AdvisoryModel>? = mAdvisoryDataProvider.GetAdvisories()

        if (advisories != null) {
            val endResult = ArrayList<Any>()

            var currentLevel: AdvisoryModel.AdvisoryLevel? = null
            var currentType: AdvisoryModel.AdvisoryType? = null

            advisories = mAdvisorySorter.Sort(advisories)

            for (i in advisories!!.indices) {
                if (advisories[i].mAdvisoryType.mAdvisoryLevel != currentLevel || advisories[i].mAdvisoryType.mAdvisoryType != currentType) {
                    endResult.add(advisories[i].mAdvisoryType)
                    currentLevel = advisories[i].mAdvisoryType.mAdvisoryLevel
                    currentType = advisories[i].mAdvisoryType.mAdvisoryType
                }

                endResult.add(advisories[i])
            }

            mBinding!!.advisoriesRecyclerView.SetItems(endResult)
        }
    }
}
