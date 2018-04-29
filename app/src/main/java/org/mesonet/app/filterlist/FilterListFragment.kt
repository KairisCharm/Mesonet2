package org.mesonet.app.filterlist


import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

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

import java.util.Observable
import java.util.Observer

import javax.inject.Inject


class FilterListFragment : BaseFragment(), SelectSiteListener, FilterListController.ListFilterListener, Observer {

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
            mFilterListData.AllViewHolderData(object: FilterListDataProvider.FilterListDataListener{
                override fun ListDataBuilt(inListData: Map<String, BasicListData>?) {
                    if(activity != null && isAdded()) {
                        activity?.runOnUiThread({
                            mFilterListController.SortByNearest(mBinding.searchText.text.toString(), inListData, this@FilterListFragment)
                        })
                    }
                }

            })
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
                mFilterListData.AllViewHolderData(object: FilterListDataProvider.FilterListDataListener{
                    override fun ListDataBuilt(inListData: Map<String, BasicListData>?) {
                        if(activity != null && isAdded()) {
                            activity?.runOnUiThread({
                                mFilterListController.FillList(mBinding.searchText.text.toString(), inListData, this@FilterListFragment)
                            })
                        }
                    }
                })
            }

            override fun afterTextChanged(editable: Editable) {

            }
        }

        mFilterListData.GetDataObservable().addObserver(this)

        mFilterListData.AllViewHolderData(object: FilterListDataProvider.FilterListDataListener{
            override fun ListDataBuilt(inListData: Map<String, BasicListData>?) {
                if(activity != null && isAdded()) {
                    activity?.runOnUiThread({
                        if (inListData != null && inListData.containsKey(mFilterListData.CurrentSelection()))
                            mBinding.searchText.setText(inListData[mFilterListData.CurrentSelection()]?.GetName())
                        mBinding.searchText.addTextChangedListener(mTextChangedListener)
                        mFilterListController.FillList(mBinding.searchText.text.toString(), inListData, this@FilterListFragment)
                    })
                }
            }

        })

        return mBinding.root
    }


    override fun onDestroyView() {
        mFilterListData.GetDataObservable().deleteObserver(this)
        mBinding.searchText.removeTextChangedListener(mTextChangedListener)
        mTextChangedListener = null
        super.onDestroyView()
    }


    override fun SetResult(inResult: String) {
        if(activity != null && isAdded()) {
            activity?.runOnUiThread({
                Close()
                mSelectedListener.SetResult(inResult)
            })
        }
    }


    internal fun Close()
    {
        mMainActivity.CloseKeyboard()
        mFilterListCloser.Close()
    }



    override fun update(observable: Observable, o: Any?) {
        mFilterListData.AllViewHolderData(object: FilterListDataProvider.FilterListDataListener{
            override fun ListDataBuilt(inListData: Map<String, BasicListData>?) {
                if(activity != null && isAdded()) {
                    activity?.runOnUiThread({
                        mFilterListController.FillList(mBinding.searchText.text.toString(), inListData,this@FilterListFragment)
                    })
                }
            }
        })
    }


    override fun ListFiltered(inFilteredResults: MutableList<Pair<String, BasicListData>>) {
        if(activity != null && isAdded())
        {
            activity?.runOnUiThread({
                mBinding.searchList.SetItems(inFilteredResults)
            })
        }
    }


    interface FilterListCloser {
        fun Close()
    }


    private inner class FilterListAdapter : RecyclerViewAdapter<Pair<String, String>, BasicViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasicViewHolder {
            return BasicViewHolder(parent, this@FilterListFragment)
        }
    }
}
