package org.mesonet.app.filterlist


import android.databinding.DataBindingUtil
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup

import org.mesonet.app.BasicViewHolder
import org.mesonet.app.MainActivity
import org.mesonet.app.R
import org.mesonet.app.androidsystem.DeviceLocation
import org.mesonet.app.baseclasses.BaseFragment
import org.mesonet.app.baseclasses.RecyclerViewAdapter
import org.mesonet.app.databinding.FilterListFragmentBinding
import org.mesonet.app.site.SiteSelectionInterfaces

import java.util.ArrayList
import java.util.Collections
import java.util.Comparator
import java.util.Observable
import java.util.Observer

import javax.inject.Inject


class FilterListFragment : BaseFragment(), SiteSelectionInterfaces.SelectSiteListener, Observer {
    private var mSortByNearest = false

    internal var mTextChangedListener: TextWatcher? = null

    internal lateinit var mBinding: FilterListFragmentBinding

    @Inject
    lateinit var mFilterListData: FilterListDataProvider

    @Inject
    lateinit var mFilterListCloser: FilterListCloser

    @Inject
    lateinit var mSelectedListener: SiteSelectionInterfaces.SelectSiteListener

    @Inject
    lateinit var mDeviceLocation: DeviceLocation

    @Inject
    lateinit var mMainActivity: MainActivity


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.filter_list_fragment, container, false)

        mBinding.searchList.setAdapter(FilterListAdapter())

        val closeDrawable = resources.getDrawable(R.drawable.ic_close_white_36dp)

        mBinding.siteSelectionToolbar.navigationIcon = closeDrawable
        mBinding.siteSelectionToolbar.setNavigationOnClickListener { Close() }

        mBinding.siteSelectionToolbar.inflateMenu(R.menu.search_list_menu)

        mBinding.siteSelectionToolbar.menu.findItem(R.id.nearestLocation).setOnMenuItemClickListener {
            mSortByNearest = true
            FillList()
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
                FillList()
            }

            override fun afterTextChanged(editable: Editable) {

            }
        }

        val data = mFilterListData.AllViewHolderData()

        if (data != null && data.containsKey(mFilterListData.CurrentSelection()))
            mBinding.searchText.setText(data[mFilterListData.CurrentSelection()]?.GetName())
        mBinding.searchText.addTextChangedListener(mTextChangedListener)

        mFilterListData.GetDataObservable().addObserver(this)

        FillList()

        return mBinding.root
    }


    override fun onDestroyView() {
        mFilterListData.GetDataObservable().deleteObserver(this)
        mBinding.searchText.removeTextChangedListener(mTextChangedListener)
        mTextChangedListener = null
        super.onDestroyView()
    }


    override fun SetResult(inResult: String) {
        Close()
        mSelectedListener.SetResult(inResult)
    }


    fun Close()
    {
        mMainActivity.CloseKeyboard()
        mFilterListCloser.Close()
    }


    private fun FillList() {
        if (mSortByNearest) {
            mDeviceLocation!!.GetLocation(object : DeviceLocation.LocationListener {
                override fun LastLocationFound(inLocation: Location?) {
                    FillList(inLocation)
                }

                override fun LocationUnavailable() {
                    FillList(null)
                }
            })
        } else {
            FillList(null)
        }
    }


    private fun FillList(inLocation: Location?) {
        mBinding.searchList.SetItems(ArrayList<Any>())

        val data = mFilterListData.AllViewHolderData()

        if (data != null) {
            mBinding.searchList.SetItems(SortList(mBinding.searchText.text.toString(), MapToListOfPairs().Create(data), inLocation))
        }
    }


    private fun SortList(inCurrentValue: String, inSearchFields: MutableList<Pair<String, BasicViewHolder.BasicViewHolderData>>, inLocation: Location?): MutableList<Pair<String, BasicViewHolder.BasicViewHolderData>> {
        Collections.sort<Pair<String, BasicViewHolder.BasicViewHolderData>>(inSearchFields, object : Comparator<Pair<String, BasicViewHolder.BasicViewHolderData>> {
            override fun compare(stringPairPair: Pair<String, BasicViewHolder.BasicViewHolderData>, t1: Pair<String, BasicViewHolder.BasicViewHolderData>): Int {
                val name1 = stringPairPair.second.GetName()
                val name2 = t1.second.GetName()

                var result = 0

                if (inLocation != null) {
                    if (stringPairPair.second.GetLocation().distanceTo(inLocation) < t1.second.GetLocation().distanceTo(inLocation))
                        result = -1
                    else if (stringPairPair.second.GetLocation().distanceTo(inLocation) > t1.second.GetLocation().distanceTo(inLocation))
                        result = 1
                }


                if (result == 0) {
                    if (stringPairPair.second.IsFavorite() && !t1.second.IsFavorite())
                        result = -1
                    else if (!stringPairPair.second.IsFavorite() && t1.second.IsFavorite())
                        result = 1
                }

                if (result == 0)
                    result = HasSearchValue(inCurrentValue, name1, name2)

                if (result == 0)
                    result = HasSearchValue(inCurrentValue, name2, name1)

                if (result == 0)
                    result = name1.compareTo(name2)

                return result
            }


            private fun HasSearchValue(inSearchValue: String, inName1: String, inName2: String): Int {
                val lowerSearchValue = inSearchValue.toLowerCase()
                val lowerName1 = inName1.toLowerCase()
                val lowerName2 = inName2.toLowerCase()

                if (lowerName1 == lowerSearchValue && lowerName2 != lowerSearchValue)
                    return -1

                if (lowerName2 == lowerSearchValue && lowerName1 != lowerSearchValue)
                    return 1

                if (lowerName1.contains(lowerSearchValue) && !lowerName2.contains(lowerSearchValue))
                    return -1

                return if (lowerName2.contains(lowerSearchValue) && !lowerName1.contains(lowerSearchValue)) 1 else 0

            }
        })

        return inSearchFields
    }

    override fun update(observable: Observable, o: Any?) {
        FillList()
    }


    interface FilterListCloser {
        fun Close()
    }


    interface FilterListDataProvider {
        fun AllViewHolderData(): Map<String, BasicViewHolder.BasicViewHolderData>?
        fun CurrentSelection(): String
        fun GetDataObservable(): Observable
    }


    private inner class FilterListAdapter : RecyclerViewAdapter<Pair<String, String>, BasicViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasicViewHolder {
            return BasicViewHolder(parent, this@FilterListFragment)
        }
    }
}
