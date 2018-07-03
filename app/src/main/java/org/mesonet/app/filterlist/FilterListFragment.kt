package org.mesonet.app.filterlist


import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

import org.mesonet.app.BasicViewHolder
import org.mesonet.app.MainActivity
import org.mesonet.app.R
import org.mesonet.app.baseclasses.BaseFragment
import org.mesonet.app.baseclasses.RecyclerViewAdapter
import org.mesonet.app.databinding.FilterListFragmentBinding
import org.mesonet.dataprocessing.BasicListData
import org.mesonet.dataprocessing.SelectSiteListener
import org.mesonet.dataprocessing.filterlist.FilterListController
import org.mesonet.dataprocessing.filterlist.FilterListDataProvider

import javax.inject.Inject


class FilterListFragment : BaseFragment(), SelectSiteListener, Observer<MutableList<Pair<String, BasicListData>>> {
    private var mTextChangedListener: TextWatcher? = null

    private lateinit var mBinding: FilterListFragmentBinding

    @Inject
    internal lateinit var mFilterListCloser: FilterListCloser

    @Inject
    internal lateinit var mMainActivity: MainActivity

    @Inject
    internal lateinit var mSelectedListener: SelectSiteListener

    @Inject
    internal lateinit var mFilterListData: FilterListDataProvider

    @Inject
    internal lateinit var mFilterListController: FilterListController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.filter_list_fragment, container, false)

        mBinding.searchList.setAdapter(FilterListAdapter())

        val closeDrawable = resources.getDrawable(R.drawable.ic_close_white_36dp)

        mBinding.siteSelectionToolbar.navigationIcon = closeDrawable
        mBinding.siteSelectionToolbar.setNavigationOnClickListener { Close() }

        mBinding.siteSelectionToolbar.inflateMenu(R.menu.search_list_menu)

        mBinding.siteSelectionToolbar.menu.findItem(R.id.nearestLocation).setOnMenuItemClickListener {
            mFilterListData.AsBasicListData().observeOn(AndroidSchedulers.mainThread()).subscribe {
                if(context != null)
                    mFilterListController.SortByNearest(context!!, mBinding.searchText.text.toString(), it.first).observeOn(AndroidSchedulers.mainThread()).subscribe(this)
            }
            false
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            closeDrawable.setTint(resources.getColor(R.color.lightTextColor, activity?.theme))
            mBinding.searchText.setTextColor(resources.getColor(R.color.lightTextColor, activity?.theme))
        } else {
            closeDrawable.setTint(resources.getColor(R.color.lightTextColor))
            mBinding.searchText.setTextColor(resources.getColor(R.color.lightTextColor))
        }

        mTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                mFilterListData.AsBasicListData().observeOn(AndroidSchedulers.mainThread()).subscribe {
                    if(context != null)
                        mFilterListController.TryGetLocationAndFillListObservable(context!!, mBinding.searchText.text.toString(), it.first).observeOn(AndroidSchedulers.mainThread()).subscribe(this@FilterListFragment)
                }
            }

            override fun afterTextChanged(editable: Editable) {
            }
        }

        mFilterListData.AsBasicListData().observeOn(AndroidSchedulers.mainThread()).subscribe{
            if (it.first.containsKey(mFilterListData.CurrentSelection()))
                mBinding.searchText.setText(it.first[mFilterListData.CurrentSelection()]?.GetName())
            mBinding.searchText.addTextChangedListener(mTextChangedListener)
            if(context != null)
                mFilterListController.TryGetLocationAndFillListObservable(context!!, mBinding.searchText.text.toString(), it.first).observeOn(AndroidSchedulers.mainThread()).subscribe(this@FilterListFragment)
        }

        return mBinding.root
    }



    override fun onDestroyView() {
        mBinding.searchText.removeTextChangedListener(mTextChangedListener)
        mTextChangedListener = null
        super.onDestroyView()
    }


    override fun SetResult(inResult: String) {
        Observable.create(ObservableOnSubscribe<Void> {
            Close()
            mSelectedListener.SetResult(inResult)
        }).observeOn(AndroidSchedulers.mainThread()).subscribe()
    }


    internal fun Close()
    {
        mMainActivity.CloseKeyboard()
        mFilterListCloser.Close()
    }


    override fun onComplete() {}
    override fun onSubscribe(d: Disposable) {}
    override fun onError(e: Throwable) {}

    override fun onNext(t: MutableList<Pair<String, BasicListData>>) {
        mBinding.searchList.SetItems(t)
    }


    interface FilterListCloser {
        fun Close()
    }


    private inner class FilterListAdapter : RecyclerViewAdapter<Pair<String, BasicListData>, BasicViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasicViewHolder {
            return BasicViewHolder(parent, this@FilterListFragment)
        }
    }
}
