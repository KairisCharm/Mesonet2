package org.mesonet.app.site

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
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
import org.mesonet.dataprocessing.site.forecast.SemiDayForecastDataController
import java.util.*

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

    var mMesonetDisposable: Disposable? = null
    var mForecastDisposable: Disposable? = null

    internal var mCurrentStid: String? = null

    override fun onCreateView(inInflater: LayoutInflater, inParent: ViewGroup?, inSavedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inInflater, R.layout.site_overview_fragment, inParent, false)

        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            SetMesonetBinding(mBinding?.mesonetDataContainer)

        mBinding?.mesonetDataContainer?.mesonetData?.mesonetLayout?.visibility = View.GONE

        mMesonetSiteDataController.GetCurrentSelectionSubject().observeOn(AndroidSchedulers.mainThread()).subscribe(this)

        mBinding?.previousFab?.setOnClickListener {
            val currentSelected = mBinding?.forecastViewPager?.currentItem ?: 0

            if (currentSelected > 0)
                mBinding?.forecastViewPager?.currentItem = currentSelected - 1
        }

        mBinding?.nextFab?.setOnClickListener {
            val currentSelected = mBinding?.forecastViewPager?.currentItem ?: 0

            if (currentSelected < ((mBinding?.forecastViewPager?.childCount ?: 0) - 1))
                mBinding?.forecastViewPager?.currentItem = currentSelected + 1
        }

        mBinding?.forecastViewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                CalculateScrollButtonVisibility()
            }

            override fun onPageSelected(position: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}
        })

        CalculateScrollButtonVisibility()

        return mBinding?.root ?: View(context)
    }


    private fun CalculateScrollButtonVisibility() {
        if(mBinding != null && mBinding?.forecastViewPager?.adapter != null) {
            if (mBinding?.forecastViewPager?.currentItem == 0)
                mBinding?.previousFab?.visibility = View.GONE
            else
                mBinding?.previousFab?.visibility = View.VISIBLE

            if ((mBinding?.forecastViewPager?.currentItem ?: 0) < ((mBinding?.forecastViewPager?.adapter?.count ?: 0) - 1))
                mBinding?.nextFab?.visibility = View.VISIBLE
            else
                mBinding?.nextFab?.visibility = View.GONE
        }
    }


    private fun RevealView(view: View?) {
        val cx = (view?.left ?: 0) + (view?.right ?: 0) - 24
        val cy = (view?.top ?: 0) + (view?.bottom ?: 0) / 2
        val radius = Math.max(mBinding?.childFragmentContainer?.width ?: 0, mBinding?.childFragmentContainer?.height ?: 0) * 2.0f

        if (mBinding?.childFragmentContainer?.visibility != View.VISIBLE) {

            mBinding?.childFragmentContainer?.visibility = View.VISIBLE
            val hide = ViewAnimationUtils.createCircularReveal(mBinding?.childFragmentContainer, cx, cy, 0f, radius)
            hide.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    mBinding?.childFragmentContainer?.visibility = View.VISIBLE
                    hide.removeListener(this)
                }
            })
            hide.start()

        } else {
            val reveal = ViewAnimationUtils.createCircularReveal(
                    mBinding?.childFragmentContainer, cx, cy, radius, 0f)
            reveal.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    mBinding?.childFragmentContainer?.visibility = View.INVISIBLE
                    reveal.removeListener(this)
                }
            })
            reveal.start()
        }
    }


    override fun Close() {
        Observable.create(ObservableOnSubscribe<Void> {
            RevealView(mBinding?.mesonetDataContainer?.siteToolbar)
            it.onComplete()
        }).subscribeOn(AndroidSchedulers.mainThread()).subscribe(object: Observer<Void> {
            override fun onComplete() {}
            override fun onSubscribe(d: Disposable) {}
            override fun onNext(t: Void) {}
            override fun onError(e: Throwable) {
                e.printStackTrace()
            }
        })
    }

    override fun onMenuItemClick(menuItem: MenuItem): Boolean {
        menuItem.isEnabled = false
        when (menuItem.itemId) {
            R.id.openList -> {
                val transaction = childFragmentManager.beginTransaction()
                transaction.replace(R.id.childFragmentContainer, FilterListFragment())
                transaction.commit()
                RevealView(mBinding?.mesonetDataContainer?.siteToolbar)
            }
            R.id.favorite -> ToggleFavorite()
        }
        menuItem.isEnabled = true
        return false
    }

    private fun ToggleFavorite() {
        mMesonetSiteDataController.ToggleFavorite(mMesonetSiteDataController.CurrentSelection()).subscribe (object: Observer<Boolean>{
            override fun onComplete() {}
            override fun onSubscribe(d: Disposable) {}
            override fun onNext(t: Boolean) {
                if (t) {
                    mMesonetDataBinding?.siteToolbar?.menu?.findItem(R.id.favorite)?.setIcon(R.drawable.ic_favorite_white_36dp)
                } else {
                    mMesonetDataBinding?.siteToolbar?.menu?.findItem(R.id.favorite)?.setIcon(R.drawable.ic_favorite_border_white_36dp)
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

        })
    }


    override fun onResume() {
        super.onResume()

        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            SetMesonetBinding(mBinding?.mesonetDataContainer)

        mMesonetUIController.GetDisplayFieldsSubject().observeOn(AndroidSchedulers.mainThread()).subscribe(object: Observer<MesonetUIController.MesonetDisplayFields>{
            override fun onComplete() {}

            override fun onSubscribe(d: Disposable) {
                mMesonetDisposable = d
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()

                onNext(object: MesonetUIController.MesonetDisplayFields{
                    override fun GetStationName(): String {
                        return "-"
                    }

                    override fun GetAirTempString(): String {
                        return "-"
                    }

                    override fun GetApparentTempString(): String {
                        return "-"
                    }

                    override fun GetDewpointString(): String {
                        return "-"
                    }

                    override fun GetWindString(): String {
                        return "-"
                    }

                    override fun Get24HrRainfallString(): String {
                        return "-"
                    }

                    override fun GetHumidityString(): String {
                        return "-"
                    }

                    override fun GetWindGustsString(): String {
                        return "-"
                    }

                    override fun GetPressureString(): String {
                        return "-"
                    }

                    override fun GetTimeString(): String {
                        return "Error"
                    }
                })
            }

            override fun onNext(t: MesonetUIController.MesonetDisplayFields) {
                mBinding?.mesonetDataContainer?.displayFields = t
                mBinding?.mesonetProgressBar?.visibility = View.GONE
                mMesonetDataBinding?.mesonetData?.mesonetLayout?.visibility = View.VISIBLE
            }
        })

        mFiveDayForecastDataController.GetForecastDataSubject().observeOn(AndroidSchedulers.mainThread()).subscribe(object: Observer<List<SemiDayForecastDataController>>
        {
            override fun onComplete() {}

            override fun onSubscribe(d: Disposable) {
                mForecastDisposable = d
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()

                mBinding?.forecastViewPager?.visibility = View.GONE
                mBinding?.forecstErrorText?.visibility = View.VISIBLE
            }

            override fun onNext(t: List<SemiDayForecastDataController>) {
                if (mBinding?.forecastViewPager?.adapter == null || mBinding?.forecastViewPager?.adapter?.count == 0) {
                    val forecastsPerPage = resources.getInteger(R.integer.forecastsPerPage)
                    var pageCount = mFiveDayForecastDataController.GetCount() / forecastsPerPage

                    val forecastPages = ArrayList<ForecastListView>()
                    var i = 0
                    while (i < mFiveDayForecastDataController.GetCount()) {
                        val forecastListView = ForecastListView(activity ?: Activity())

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

                    if (mBinding?.forecastViewPager?.childCount == 0) {
                        val pagerAdapter = object : PagerAdapter() {
                            override fun getCount(): Int {
                                return finalPageCount
                            }

                            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                                return view == `object`
                            }

                            override fun instantiateItem(inViewGroup: ViewGroup, inPosition: Int): View {
                                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
                                    mBinding?.forecastProgressBar?.visibility = View.GONE
                                var forecastPostion = inPosition
                                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                                    if (inPosition == 0) {
                                        val mesonetBinding = DataBindingUtil.inflate<MesonetDataContainerBinding>(LayoutInflater.from(context), R.layout.mesonet_data_container, null, false)
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

                        mBinding?.forecastViewPager?.adapter = pagerAdapter
                        mBinding?.forecastViewPager?.adapter?.notifyDataSetChanged()
                    }

                    mBinding?.forecastProgressBar?.visibility = View.GONE
                    mBinding?.forecastViewPager?.visibility = View.VISIBLE
                }
            }
        })
    }


    override fun onPause() {
        mMesonetDisposable?.dispose()
        mForecastDisposable?.dispose()

        super.onPause()
    }


    override fun onDestroyView() {
        for(i in 0..(mBinding?.forecastViewPager?.childCount?: 0 - 1))
        {
            if(mBinding?.forecastViewPager?.getChildAt(i) is ForecastListView)
                (mBinding?.forecastViewPager?.getChildAt(i) as ForecastListView).Dispose()
        }
        super.onDestroyView()
    }


    override fun onComplete() {}
    override fun onSubscribe(d: Disposable) {}
    override fun onError(e: Throwable)
    {
        e.printStackTrace()
    }

    override fun onNext(t: String) {
        if (isAdded && t != mCurrentStid) {
            mBinding?.mesonetDataContainer?.siteToolbar?.title = ""
            mMesonetDataBinding?.mesonetData?.mesonetLayout?.visibility = View.GONE
            mBinding?.mesonetProgressBar?.visibility = View.VISIBLE
        }
    }


    private fun SetMesonetBinding(inContainerBinding: MesonetDataContainerBinding?) {
        mMesonetDataBinding = inContainerBinding

        mMesonetDataBinding?.siteToolbar?.title = mMesonetSiteDataController.CurrentStationName()

        if (mMesonetDataBinding?.siteToolbar?.menu?.hasVisibleItems() != true) {
            mMesonetDataBinding?.siteToolbar?.inflateMenu(R.menu.mesonet_site_menu)
        }
        mMesonetDataBinding?.siteToolbar?.setOnMenuItemClickListener(this)
        mMesonetDataBinding?.siteToolbar?.setNavigationIcon(R.drawable.ic_multiline_chart_white_36dp)
        mMesonetDataBinding?.siteToolbar?.setNavigationOnClickListener {
            it.isEnabled = false
            if (mMesonetSiteDataController.CurrentSelection() != "") {
                val intent = Intent(activity?.baseContext, WebViewActivity::class.java)
                intent.putExtra(WebViewActivity.kTitle, mMesonetSiteDataController.CurrentStationName() + " Meteogram")
                intent.putExtra(WebViewActivity.kUrl, mMesonetSiteDataController.GetMeteogramUrl())
                intent.putExtra(WebViewActivity.kInitialZoom, 1)
                intent.putExtra(WebViewActivity.kAllowUserZoom, true)
                intent.putExtra(WebViewActivity.kAllowShare, true)
                startActivity(intent)
            }

            it.isEnabled = true
        }
        if (mMesonetSiteDataController.CurrentIsFavorite()) {
            mMesonetDataBinding?.siteToolbar?.menu?.findItem(R.id.favorite)?.setIcon(R.drawable.ic_favorite_white_36dp)
        } else {
            mMesonetDataBinding?.siteToolbar?.menu?.findItem(R.id.favorite)?.setIcon(R.drawable.ic_favorite_border_white_36dp)
        }

        mMesonetUIController.GetDisplayFieldsSubject().observeOn(AndroidSchedulers.mainThread()).subscribe(object: Observer<MesonetUIController.MesonetDisplayFields>{
            override fun onComplete() {}
            override fun onSubscribe(d: Disposable) {}

            override fun onNext(t: MesonetUIController.MesonetDisplayFields) {
                mMesonetDataBinding?.mesonetData?.displayFields = t
            }

            override fun onError(e: Throwable) {
                mMesonetDataBinding?.mesonetData?.readingTime?.text = "Error"
                e.printStackTrace()
            }

        })

        mBinding?.invalidateAll()
    }
}