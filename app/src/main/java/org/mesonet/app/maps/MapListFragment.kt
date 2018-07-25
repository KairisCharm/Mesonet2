package org.mesonet.app.maps

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

import org.mesonet.app.R
import org.mesonet.app.baseclasses.BaseActivity
import org.mesonet.app.baseclasses.BaseFragment
import org.mesonet.app.databinding.MapListFragmentBinding
import org.mesonet.dataprocessing.maps.MapsDataProvider

import javax.inject.Inject


class MapListFragment : BaseFragment()
{
    companion object {
        val kMapGroupFullList = "mapGroupFullList"
    }

    private lateinit var mBinding: MapListFragmentBinding

    @Inject
    internal lateinit var mDataProvider: MapsDataProvider

    @Inject
    internal lateinit var mActivity: BaseActivity


    override fun onCreateView(inInflater: LayoutInflater, inParent: ViewGroup?, inSavedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inInflater, R.layout.map_list_fragment, inParent, false)

        var group: MapsDataProvider.MapFullGroupDisplayData? = null

        if (arguments != null && arguments?.containsKey(kMapGroupFullList) == true)
            group = arguments?.getSerializable(kMapGroupFullList) as MapsDataProvider.MapFullGroupDisplayData

        if(group == null) {
            mBinding.mapList.setAdapter(MapsGroupRecyclerViewAdapter(mActivity))

            mDataProvider.GetMapsListObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(object: Observer<MutableList<MapsDataProvider.MapAbbreviatedGroupDisplayData>>
            {
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}

                override fun onNext(t: MutableList<MapsDataProvider.MapAbbreviatedGroupDisplayData>)
                {
                    mBinding.progressBar.visibility = View.GONE
                    if (t.isEmpty())
                        mBinding.emptyListText.visibility = View.VISIBLE
                    else {
                        mBinding.emptyListText.visibility = View.GONE
                        mBinding.mapList.SetItems(t)
                    }
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }
            })
        }
        else
        {
            mBinding.groupNameToolbar.title = group.GetTitle()
            mBinding.groupNameToolbar.visibility = View.VISIBLE
            mBinding.progressBar.visibility = View.GONE
            mBinding.mapList.setAdapter(MapsSectionRecyclerViewAdapter())
            mBinding.mapList.SetItems(ArrayList(group.GetSections().values))
        }

        return mBinding.root
    }
}
