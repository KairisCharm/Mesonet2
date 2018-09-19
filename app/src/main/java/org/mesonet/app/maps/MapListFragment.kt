package org.mesonet.app.maps

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.RecyclerView
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
import org.mesonet.dataprocessing.PageStateInfo
import org.mesonet.dataprocessing.maps.MapsDataProvider

import javax.inject.Inject


class MapListFragment : BaseFragment()
{
    companion object {
        val kMapGroupFullList = "mapGroupFullList"
    }

    private lateinit var mBinding: MapListFragmentBinding

    private var mDisposable: Disposable? = null
    private var mPageStateDisposable: Disposable? = null

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
                override fun onSubscribe(d: Disposable)
                {
                    mDisposable = d
                }

                override fun onNext(t: MutableList<MapsDataProvider.MapAbbreviatedGroupDisplayData>)
                {
                    mBinding.mapList.SetItems(t)
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }
            })

            if(mPageStateDisposable?.isDisposed != false) {
                mDataProvider.GetPageStateObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(object: Observer<PageStateInfo>{
                    override fun onComplete() {}
                    override fun onSubscribe(d: Disposable) {
                        mPageStateDisposable = d
                    }

                    override fun onNext(t: PageStateInfo) {
                        when(t.GetPageState())
                        {
                            PageStateInfo.PageState.kData ->
                            {
                                mBinding.groupNameToolbar.visibility = View.GONE
                                (mBinding.mapList as RecyclerView).visibility = View.VISIBLE
                                mBinding.progressBar.visibility = View.GONE
                                mBinding.emptyListText.visibility = View.GONE
                            }
                            PageStateInfo.PageState.kLoading ->
                            {
                                mBinding.groupNameToolbar.visibility = View.GONE
                                (mBinding.mapList as RecyclerView).visibility = View.GONE
                                mBinding.progressBar.visibility = View.VISIBLE
                                mBinding.emptyListText.visibility = View.GONE
                            }
                            PageStateInfo.PageState.kError ->
                            {
                                mBinding.groupNameToolbar.visibility = View.GONE
                                (mBinding.mapList as RecyclerView).visibility = View.GONE
                                mBinding.progressBar.visibility = View.GONE
                                mBinding.emptyListText.visibility = View.VISIBLE
                                mBinding.emptyListText.text = t.GetErrorMessage()
                            }
                        }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
            }
        }
        else
        {
            mBinding.progressBar.visibility = View.GONE
            mBinding.emptyListText.visibility = View.GONE
            (mBinding.mapList as RecyclerView).visibility = View.VISIBLE
            mBinding.groupNameToolbar.title = group.GetTitle()
            mBinding.groupNameToolbar.visibility = View.VISIBLE
            mBinding.mapList.setAdapter(MapsSectionRecyclerViewAdapter())
            mBinding.mapList.SetItems(ArrayList(group.GetSections().values))
        }

        return mBinding.root
    }


    override fun onResume() {
        super.onResume()

        val myContext = context

        if(myContext != null)
            mDataProvider.OnResume(myContext)
    }


    override fun onPause() {
        super.onPause()

        mDataProvider.OnPause()
    }


    override fun onDestroyView() {
        mDisposable?.dispose()
        mDisposable = null
        mPageStateDisposable?.dispose()
        mPageStateDisposable = null

        super.onDestroyView()
    }
}
