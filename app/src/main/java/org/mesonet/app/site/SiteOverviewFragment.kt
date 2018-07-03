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
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

import org.mesonet.app.R
import org.mesonet.app.baseclasses.BaseFragment
import org.mesonet.app.databinding.MesonetDataContainerBinding
import org.mesonet.app.webview.WebViewActivity
import org.mesonet.app.databinding.SiteOverviewFragmentBinding
import org.mesonet.app.filterlist.FilterListFragment
import org.mesonet.app.site.forecast.ForecastListView
import org.mesonet.dataprocessing.site.MesonetSiteDataController
import org.mesonet.dataprocessing.site.mesonetdata.MesonetUIController
import org.mesonet.dataprocessing.site.forecast.FiveDayForecastDataController
import org.mesonet.dataprocessing.site.forecast.ForecastData
import org.mesonet.dataprocessing.site.forecast.SemiDayForecastDataController

import java.util.ArrayList
import java.util.concurrent.TimeUnit

import javax.inject.Inject


class SiteOverviewFragment : BaseFragment(), FilterListFragment.FilterListCloser, Toolbar.OnMenuItemClickListener, Observer<String> {
    private var mMesonetDataBinding: MesonetDataContainerBinding? = null

    private var mBinding: SiteOverviewFragmentBinding? = null

    @Inject
    internal lateinit var mFiveDayForecastDataController: FiveDayForecastDataController

    @Inject
    internal lateinit var mMesonetUIController: MesonetUIController

    @Inject
    internal lateinit var mMesonetSiteDataController: MesonetSiteDataController

    internal var mCurrentStid: String? = null

    internal var mMesonetDisposable: Disposable? = null

    internal var mForecastDisposable: Disposable? = null
    internal val mMesonetObserver = object: Observer<MesonetUIController.MesonetDisplayFields>{
        override fun onComplete() {
        }

        override fun onSubscribe(d: Disposable) {
        }

        override fun onNext(t: MesonetUIController.MesonetDisplayFields) {
            if(isAdded) {
                mBinding?.mesonetDataContainer?.displayFields = t
                mBinding?.mesonetProgressBar?.visibility = View.GONE
                mMesonetDataBinding?.mesonetData?.mesonetLayout?.visibility = View.VISIBLE
            }
        }

        override fun onError(e: Throwable) {
            e.printStackTrace()
        }
    }

    internal val mForecastObserver = object: Observer<List<SemiDayForecastDataController>>{
        override fun onComplete() {}
        override fun onSubscribe(d: Disposable) {}
        override fun onNext(t: List<SemiDayForecastDataController>)
        {
            if(isAdded)
            {
                mBinding!!.forecastViewPager.removeAllViews()

                val forecastsPerPage = getResources().getInteger(R.integer.forecastsPerPage)
                var pageCount = mFiveDayForecastDataController.GetCount() / forecastsPerPage

                val forecastPages = ArrayList<ForecastListView>()
                var i = 0
                while (i < mFiveDayForecastDataController.GetCount()) {
                    val forecastListView = ForecastListView(activity!!)

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

                        override fun destroyItem(inParent: ViewGroup, inPosition: Int, inObject: Any) {}
                    }

                    mBinding!!.forecastViewPager.adapter = pagerAdapter
                    pagerAdapter.notifyDataSetChanged()
                }
            }
        }

        override fun onError(e: Throwable)
        {
            e.printStackTrace()
        }

    }

    override fun onCreateView(inInflater: LayoutInflater, inParent: ViewGroup?, inSavedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inInflater, R.layout.site_overview_fragment, inParent, false)

        mMesonetSiteDataController.GetCurrentSelectionObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(this)

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

            override fun onPageSelected(position: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}
        })

        CalculateScrollButtonVisibility()

        return mBinding!!.root
    }


    private fun CalculateScrollButtonVisibility() {
        if (mBinding?.forecastViewPager?.currentItem == 0)
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
        Observable.create(ObservableOnSubscribe<Void> {
            RevealView(mBinding!!.mesonetDataContainer?.siteToolbar!!)
        }).subscribeOn(AndroidSchedulers.mainThread())
    }

    override fun onMenuItemClick(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.openList -> {
                val transaction = childFragmentManager.beginTransaction()
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

        SetMesonetBinding(mBinding!!.mesonetDataContainer!!)
        mMesonetDataBinding!!.mesonetData!!.mesonetLayout.visibility = View.GONE
        mBinding!!.mesonetProgressBar.visibility = View.VISIBLE

        mMesonetDisposable = Observable.interval(0,1, TimeUnit.MINUTES).subscribe {
            mMesonetUIController.GetDisplayFieldsObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(mMesonetObserver)
        }
        mForecastDisposable = Observable.interval(0,1, TimeUnit.MINUTES).subscribe {
            mFiveDayForecastDataController.GetForecastDataObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(mForecastObserver)
        }
    }


    override fun onPause() {
        mMesonetDisposable?.dispose()
        mForecastDisposable?.dispose()

        super.onPause()
    }


    override fun onComplete() {}
    override fun onSubscribe(d: Disposable) {}
    override fun onError(e: Throwable)
    {
        e.printStackTrace()
    }

    override fun onNext(t: String) {
        if(t != mCurrentStid)
        {
            mMesonetDisposable?.dispose()
            mForecastDisposable?.dispose()

            mMesonetDisposable = Observable.interval(0, 1, TimeUnit.MINUTES).subscribe {
                mMesonetUIController.GetDisplayFieldsObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(mMesonetObserver)
            }
            mForecastDisposable = Observable.interval(0, 1, TimeUnit.MINUTES).subscribe {
                mFiveDayForecastDataController.GetForecastDataObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(mForecastObserver)
            }
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
            if (mMesonetSiteDataController.CurrentSelection() != "") {
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

        mMesonetUIController.GetDisplayFieldsObservable().observeOn(AndroidSchedulers.mainThread()).subscribe {
            mMesonetDataBinding!!.mesonetData?.displayFields = it
        }

        mMesonetDataBinding!!.mesonetData!!.mesonetLayout.visibility = View.VISIBLE
        mBinding!!.mesonetProgressBar.visibility = View.GONE

        mBinding!!.invalidateAll()
    }
}