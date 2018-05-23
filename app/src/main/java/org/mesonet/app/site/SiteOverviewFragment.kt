package org.mesonet.app.site

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.support.v7.widget.Toolbar
import android.util.Log

import org.mesonet.app.R
import org.mesonet.app.baseclasses.BaseFragment
import org.mesonet.app.databinding.MesonetDataContainerBinding
import org.mesonet.app.webview.WebViewActivity
import org.mesonet.app.databinding.SiteOverviewFragmentBinding
import org.mesonet.app.filterlist.FilterListFragment
import org.mesonet.app.site.forecast.ForecastListView
//import org.mesonet.app.filterlist.dependencyinjection.DaggerFilterListComponent;
import org.mesonet.dataprocessing.site.MesonetSiteDataController
import org.mesonet.dataprocessing.site.mesonetdata.MesonetUIController
import org.mesonet.core.ThreadHandler
import org.mesonet.dataprocessing.site.forecast.FiveDayForecastDataController
import org.mesonet.dataprocessing.site.mesonetdata.MesonetDataController
//import org.mesonet.app.site.mesonetdata.dependencyinjection.DaggerMesonetDataComponent;

import java.util.ArrayList
import java.util.Observable
import java.util.Observer

import javax.inject.Inject


class SiteOverviewFragment : BaseFragment(), FilterListFragment.FilterListCloser, Toolbar.OnMenuItemClickListener, Observer {
    private var mMesonetDataBinding: MesonetDataContainerBinding? = null
    private var mBinding: SiteOverviewFragmentBinding? = null

    @Inject
    internal lateinit var mMesonetSiteDataController: MesonetSiteDataController

    @Inject
    internal lateinit var mFiveDayForecastDataController: FiveDayForecastDataController

    @Inject
    internal lateinit var mMesonetDataController: MesonetDataController

    @Inject
    internal lateinit var mMesonetUIController: MesonetUIController

    @Inject
    internal lateinit var mThreadHandler: ThreadHandler


    override fun onCreateView(inInflater: LayoutInflater, inParent: ViewGroup?, inSavedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inInflater, R.layout.site_overview_fragment, inParent, false)

        mMesonetSiteDataController.GetDataObservable().addObserver(this)
        mFiveDayForecastDataController.GetObservable().addObserver(this)
        mMesonetDataController.addObserver(this)

        //        FragmentManager fragmentManager = getChildFragmentManager();
        //        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //        transaction.add(R.id.mesonetDetailContainer, new MesonetFragment());
        //        transaction.commit();

        Update()

        mBinding!!.previousFab.setOnClickListener {
            val currentSelected = mBinding!!.forecastViewPager.currentItem

            if (currentSelected > 0)
                mBinding!!.forecastViewPager.currentItem = currentSelected - 1
        }

        mBinding!!.nextFab.setOnClickListener {
            val currentSelected = mBinding!!.forecastViewPager.currentItem

            if (currentSelected < mBinding!!.forecastViewPager.childCount - 1)
                mBinding!!.forecastViewPager.currentItem = currentSelected + 1
        }

        mBinding!!.forecastViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                CalculateScrollButtonVisibility()
            }

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        CalculateScrollButtonVisibility()

        return mBinding!!.root
    }


    private fun CalculateScrollButtonVisibility()
    {
        if(mBinding?.forecastViewPager?.currentItem == 0)
            mBinding!!.previousFab.visibility = View.GONE
        else
            mBinding!!.previousFab.visibility = View.VISIBLE

        if (mBinding?.forecastViewPager?.currentItem!! < mBinding!!.forecastViewPager.childCount - 1)
            mBinding!!.nextFab.visibility = View.VISIBLE
        else
            mBinding!!.nextFab.visibility = View.GONE
    }


    private fun RevealView(view: View) {
        val cx = view.left + view.right - 24
        val cy = (view.top + view.bottom) / 2
        val radius = Math.max(mBinding!!.childFragmentContainer.width, mBinding!!.childFragmentContainer.height) * 2.0f

        if (mBinding!!.childFragmentContainer.visibility != View.VISIBLE) {


            mBinding!!.childFragmentContainer.visibility = View.VISIBLE
            val hide = ViewAnimationUtils.createCircularReveal(mBinding!!.childFragmentContainer, cx, cy, 0f, radius)
            hide.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    mBinding!!.childFragmentContainer.visibility = View.VISIBLE
                    hide.removeListener(this)
                }
            })
            hide.start()

        } else {
            val reveal = ViewAnimationUtils.createCircularReveal(
                    mBinding!!.childFragmentContainer, cx, cy, radius, 0f)
            reveal.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    mBinding!!.childFragmentContainer.visibility = View.INVISIBLE
                    reveal.removeListener(this)
                }
            })
            reveal.start()
        }
    }

    override fun Close() {
        activity?.runOnUiThread({
            RevealView(mBinding!!.mesonetDataContainer?.siteToolbar!!)
        })
    }

    override fun onMenuItemClick(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.openList -> {
                val transaction = getChildFragmentManager().beginTransaction()
                transaction.replace(R.id.childFragmentContainer, FilterListFragment())
                transaction.commit()
                RevealView(mBinding!!.mesonetDataContainer?.siteToolbar!!)
            }
            R.id.favorite -> ToggleFavorite()
        }
        return false
    }


    private fun ToggleFavorite() {
        mMesonetSiteDataController.ToggleFavorite(mMesonetSiteDataController.CurrentSelection())
    }


    override fun onResume() {
        super.onResume()

        mMesonetDataController.StartUpdates()
        mFiveDayForecastDataController.StartUpdates()
    }


    override fun onPause() {
        mMesonetDataController.StopUpdates()
        mFiveDayForecastDataController.StopUpdates()

        super.onPause()
    }


    override fun update(observable: Observable, o: Any?) {
        activity?.runOnUiThread({
            Update()
        })
    }


    private fun Update() {
        if (isAdded()) {
//            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            SetMesonetBinding(mBinding!!.mesonetDataContainer!!)

            mBinding!!.forecastViewPager.removeAllViews()

            val forecastsPerPage = getResources().getInteger(R.integer.forecastsPerPage)
            var pageCount = mFiveDayForecastDataController.GetCount() / forecastsPerPage

            val forecastPages = ArrayList<ForecastListView>()
            var i = 0
            while (i < mFiveDayForecastDataController.GetCount()) {
                val forecastListView = ForecastListView(activity!!, mThreadHandler)

                var j = 0
                while (j < forecastsPerPage && i + j < mFiveDayForecastDataController.GetCount()) {
                    forecastListView.SetSemiDayForecast(j, mFiveDayForecastDataController.GetForecast(i + j))
                    j++
                }

                forecastPages.add(forecastListView)
                i += forecastsPerPage
            }

            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                pageCount++
            }

            val finalPageCount = pageCount

            if (mBinding!!.forecastViewPager.childCount == 0) {
                val pagerAdapter = object : PagerAdapter() {
                    override fun getCount(): Int {
                        return finalPageCount
                    }

                    override fun isViewFromObject(view: View, `object`: Any): Boolean {
                        return view == `object`
                    }

                    override fun instantiateItem(inViewGroup: ViewGroup, inPosition: Int): View {
                        mBinding!!.forecastProgressBar?.visibility = View.GONE
                        var forecastPostion = inPosition
                        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                            if (inPosition == 0) {
                                val mesonetBinding = DataBindingUtil.inflate<MesonetDataContainerBinding>(LayoutInflater.from(getContext()), R.layout.mesonet_data_container, null, false)
                                SetMesonetBinding(mesonetBinding)
                                inViewGroup.addView(mesonetBinding.root)
                                return mesonetBinding.root
                            }

                            forecastPostion--
                        }

                        if (forecastPages[forecastPostion].parent != null)
                            (forecastPages[forecastPostion].parent as ViewGroup).removeView(forecastPages[forecastPostion])
                        inViewGroup.addView(forecastPages[forecastPostion])
                        return forecastPages[forecastPostion]
                    }

                    override fun destroyItem(inParent: ViewGroup, inPosition: Int, inObject: Any) {

                    }
                }

                mBinding!!.forecastViewPager.adapter = pagerAdapter
                pagerAdapter.notifyDataSetChanged()
            }

            //            mBinding.invalidateAll();
        }
    }


    private fun SetMesonetBinding(inContainerBinding: MesonetDataContainerBinding) {
        mMesonetDataBinding = inContainerBinding

        mMesonetDataBinding?.siteToolbar?.title = mMesonetSiteDataController.CurrentStationName()

        if (!mMesonetDataBinding!!.siteToolbar.menu.hasVisibleItems()) {
            mMesonetDataBinding!!.siteToolbar.inflateMenu(R.menu.mesonet_site_menu)
        }
        mMesonetDataBinding!!.siteToolbar.setOnMenuItemClickListener(this)
        mMesonetDataBinding!!.siteToolbar.setNavigationIcon(R.drawable.ic_multiline_chart_white_36dp)
        mMesonetDataBinding!!.siteToolbar.setNavigationOnClickListener {
            if(mMesonetSiteDataController.CurrentSelection() != "") {
                val intent = Intent(activity?.baseContext, WebViewActivity::class.java)
                intent.putExtra(WebViewActivity.kTitle, mMesonetSiteDataController.CurrentStationName()!! + " Meteogram")
                intent.putExtra(WebViewActivity.kUrl, "http://www.mesonet.org/data/public/mesonet/meteograms/" + mMesonetSiteDataController.CurrentSelection().toUpperCase() + ".met.gif")
                intent.putExtra(WebViewActivity.kInitialZoom, 1)
                intent.putExtra(WebViewActivity.kAllowUserZoom, true)
                intent.putExtra(WebViewActivity.kAllowShare, true)
                startActivity(intent)
            }
        }
        if (mMesonetSiteDataController.CurrentIsFavorite()) {
            mMesonetDataBinding!!.siteToolbar.menu.findItem(R.id.favorite).setIcon(R.drawable.ic_favorite_white_36dp)
        } else {
            mMesonetDataBinding!!.siteToolbar.menu.findItem(R.id.favorite).setIcon(R.drawable.ic_favorite_border_white_36dp)
        }
        mMesonetDataBinding!!.mesonetSiteDataController = mMesonetSiteDataController
        mMesonetDataBinding!!.mesonetData?.setUiController(mMesonetUIController)

        if(mMesonetUIController.DoingInitialUpdate())
        {
            mMesonetDataBinding!!.mesonetData!!.mesonetLayout?.visibility = View.GONE
            mBinding!!.mesonetProgressBar?.visibility = View.VISIBLE
        }
        else
        {
            mMesonetDataBinding!!.mesonetData!!.mesonetLayout?.visibility = View.VISIBLE
            mBinding!!.mesonetProgressBar?.visibility = View.GONE
        }
    }
}