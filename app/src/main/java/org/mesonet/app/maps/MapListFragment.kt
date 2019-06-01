package org.mesonet.app.maps

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.subjects.BehaviorSubject
import org.mesonet.app.R

import org.mesonet.app.baseclasses.BaseActivity
import org.mesonet.app.baseclasses.BaseFragment
import org.mesonet.app.databinding.MapListFragmentBinding
import org.mesonet.app.maps.traditional.TraditionalMapsGroupRecyclerViewAdapter
import org.mesonet.app.maps.traditional.TraditionalMapsSectionRecyclerViewAdapter
import org.mesonet.dataprocessing.PageStateInfo
import org.mesonet.dataprocessing.maps.MapsDataProvider
import org.mesonet.dataprocessing.userdata.Preferences

import javax.inject.Inject


class MapListFragment : BaseFragment()
{
    companion object {
        val kMapGroupFullList = "mapGroupFullList"
    }

    private lateinit var mBinding: MapListFragmentBinding

    private var mGroupListDataDisposable: Disposable? = null
    private var mPageStateDisposable: Disposable? = null
    private var mDataAndDisplayModeDisposable: Disposable? = null

    @Inject
    internal lateinit var mDataProvider: MapsDataProvider

    @Inject
    internal lateinit var mActivity: BaseActivity

    @Inject
    internal lateinit var mPreferences: Preferences

    private val mGroupsListSubject: BehaviorSubject<MutableList<MapsDataProvider.MapAbbreviatedGroupDisplayData>> = BehaviorSubject.create()
    private val mSectionListSubject: BehaviorSubject<MutableList<MapsDataProvider.MapFullGroupDisplayData.MapGroupSection>> = BehaviorSubject.create()


    override fun onCreateView(inInflater: LayoutInflater, inParent: ViewGroup?, inSavedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inInflater, R.layout.map_list_fragment, inParent, false)

        var group: MapsDataProvider.MapFullGroupDisplayData? = null

        if (arguments != null && arguments?.containsKey(kMapGroupFullList) == true)
            group = arguments?.getSerializable(kMapGroupFullList) as MapsDataProvider.MapFullGroupDisplayData

        if(group == null) {

            mDataProvider.GetMapsListObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(object: Observer<MutableList<MapsDataProvider.MapAbbreviatedGroupDisplayData>>
            {
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable)
                {
                    mGroupListDataDisposable = d
                }

                override fun onNext(t: MutableList<MapsDataProvider.MapAbbreviatedGroupDisplayData>)
                {
                    mGroupsListSubject.onNext(t)
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

            mSectionListSubject.onNext(ArrayList(group.GetSections().values))

        }

        return mBinding.root
    }


    override fun onResume() {
        super.onResume()

        val myContext = context

        if(myContext != null)
            mDataProvider.OnResume(myContext)

        var mapListDataObservable: Observable<Any>? = null

        if(mSectionListSubject.hasValue()) {
            mapListDataObservable = Observables.combineLatest(mSectionListSubject, mPreferences.MapsDisplayModePreferenceObservable(mActivity)) {
                list, mapPreference ->

                if(mapPreference == Preferences.MapsDisplayModePreference.kTraditional)
                    mBinding.mapList.setAdapter(TraditionalMapsSectionRecyclerViewAdapter())
                else
                    mBinding.mapList.setAdapter(MapsSectionRecyclerViewAdapter())

                mBinding.mapList.SetItems(list)
            }
        }
        else {
            mapListDataObservable = Observables.combineLatest(mGroupsListSubject, mPreferences.MapsDisplayModePreferenceObservable(mActivity)) {
                list, mapPreference ->

                if(mapPreference == Preferences.MapsDisplayModePreference.kTraditional)
                    mBinding.mapList.setAdapter(TraditionalMapsGroupRecyclerViewAdapter(mActivity))
                else
                    mBinding.mapList.setAdapter(MapsGroupRecyclerViewAdapter(mActivity))

                mBinding.mapList.SetItems(list)
            }
        }

        mapListDataObservable.subscribe(object: Observer<Any>{
            override fun onComplete() {}
            override fun onSubscribe(d: Disposable) {
                mDataAndDisplayModeDisposable?.dispose()
                mDataAndDisplayModeDisposable = d
            }
            override fun onNext(t: Any) {}
            override fun onError(e: Throwable) {
                e.printStackTrace()
            }
        })
    }


    override fun onPause() {
        super.onPause()

        mDataProvider.OnPause()

        mDataAndDisplayModeDisposable?.dispose()
    }


    override fun onDestroyView() {
        mGroupListDataDisposable?.dispose()
        mGroupListDataDisposable = null
        mPageStateDisposable?.dispose()
        mPageStateDisposable = null

        super.onDestroyView()
    }
}
