package org.mesonet.app

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.AnimRes
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager

import org.mesonet.app.databinding.MainActivityBinding
import org.mesonet.app.baseclasses.BaseActivity

import javax.inject.Inject

import org.mesonet.app.advisories.AdvisoriesFragment
import org.mesonet.app.maps.MapListFragment
import org.mesonet.app.radar.RadarFragment
import org.mesonet.app.site.SiteOverviewFragment
import org.mesonet.app.webview.WebViewActivity
import org.mesonet.dataprocessing.advisories.AdvisoryDataProvider
import android.content.res.Configuration
import android.support.v4.widget.DrawerLayout
import android.view.Gravity
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import org.mesonet.app.contact.ContactActivity
import org.mesonet.app.usersettings.UserSettingsActivity
import org.mesonet.core.PerActivity
import org.mesonet.dataprocessing.LocationProvider
import org.mesonet.dataprocessing.site.MesonetSiteDataController
import org.mesonet.dataprocessing.site.forecast.FiveDayForecastDataController
import org.mesonet.dataprocessing.userdata.Preferences
import org.mesonet.models.advisories.Advisory
import java.util.concurrent.TimeUnit


@PerActivity
class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener, Toolbar.OnMenuItemClickListener {



    private val kSelectedTabId = "selectedTabId"

    private var mBinding: MainActivityBinding? = null

    private var mActionBarDrawerToggle: ActionBarDrawerToggle? = null


    @Inject
    internal lateinit var mAdvisoryDataProvider: AdvisoryDataProvider

    @Inject
    internal lateinit var mMesonetSiteDataController: MesonetSiteDataController

    @Inject
    internal lateinit var mFiveDayForecastDataController: FiveDayForecastDataController

    @Inject
    lateinit var mLocationProvider: LocationProvider

    @Inject
    lateinit var mPreferences: Preferences

    var mAdvisoryDisposable: Disposable? = null



    public override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        var selectedTab = R.id.mesonetOption

        if (savedInstanceState != null)
            selectedTab = savedInstanceState.getInt(kSelectedTabId)

        LoadBinding(selectedTab)
    }


    override fun onResume() {
        super.onResume()

        mAdvisoryDataProvider.GetDataObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(object: Observer<Advisory.AdvisoryList>{
            override fun onComplete() {}

            override fun onSubscribe(d: Disposable) {
                mAdvisoryDisposable = d
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(t: Advisory.AdvisoryList) {
                if (t.isNotEmpty())
                    mBinding?.bottomNav?.menu?.getItem(3)?.icon = resources.getDrawable(R.drawable.advisory_badge, theme)
                else
                    mBinding?.bottomNav?.menu?.getItem(3)?.setIcon(R.drawable.advisories_image)
            }
        })
    }


    override fun onPause() {
        mAdvisoryDisposable?.dispose()

        super.onPause()
    }



    private fun LoadBinding(inSelectedTab: Int) {
        var fragment: Fragment? = null

        when (inSelectedTab) {
            R.id.mesonetOption -> fragment = SiteOverviewFragment()

            R.id.mapsOption -> fragment = MapListFragment()

            R.id.radarOption -> fragment = RadarFragment()

            R.id.advisoriesOption -> fragment = AdvisoriesFragment()
        }

        if (fragment != null) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentLayout, fragment)
            fragmentTransaction.commit()
        }

        mBinding = DataBindingUtil.setContentView(this, R.layout.main_activity)

        mBinding?.toolBar?.inflateMenu(R.menu.ticker_menu)
        mBinding?.toolBar?.setOnMenuItemClickListener(this)

        setSupportActionBar(mBinding?.toolBar)

        val actionBar = supportActionBar

        if (actionBar != null) {
            mActionBarDrawerToggle = object : ActionBarDrawerToggle(this, mBinding?.drawer, mBinding?.toolBar, R.string.app_name, R.string.app_name) {
                override fun onDrawerClosed(drawerView: View) {
                    syncState()
                }

                override fun onDrawerOpened(drawerView: View) {
                    syncState()
                }
            }

            mActionBarDrawerToggle?.isDrawerIndicatorEnabled = true
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeButtonEnabled(true)

            mBinding?.drawer?.addDrawerListener(mActionBarDrawerToggle?: object: DrawerLayout.DrawerListener{
                override fun onDrawerStateChanged(p0: Int) {

                }

                override fun onDrawerSlide(p0: View, p1: Float) {

                }

                override fun onDrawerClosed(p0: View) {

                }

                override fun onDrawerOpened(p0: View) {

                }
            })
            mBinding?.drawerNavView?.setNavigationItemSelectedListener(this)
            mActionBarDrawerToggle?.syncState()
        }

        mBinding?.bottomNav?.setOnNavigationItemSelectedListener { inItem ->
            var resultFragment: Fragment? = null

            when (inItem.itemId) {
                R.id.mesonetOption -> resultFragment = SiteOverviewFragment()

                R.id.mapsOption -> resultFragment = MapListFragment()

                R.id.radarOption -> resultFragment = RadarFragment()

                R.id.advisoriesOption -> resultFragment = AdvisoriesFragment()
            }

            if (resultFragment != null) {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragmentLayout, resultFragment)
                fragmentTransaction.commit()
            }

            true
        }

        mBinding?.bottomNav?.itemIconTintList = null

        val menuView = mBinding?.bottomNav?.getChildAt(0) as BottomNavigationMenuView


        for (i in 0 until (mBinding?.bottomNav?.menu?.size()?: 0)) {
            val iconView = menuView.getChildAt(i).findViewById<View>(android.support.design.R.id.icon)
            val layoutParams = iconView.layoutParams
            val displayMetrics = resources.displayMetrics
            layoutParams.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32f, displayMetrics).toInt()
            layoutParams.width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32f, displayMetrics).toInt()
            iconView.layoutParams = layoutParams
        }
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        var selectedTab: Int = R.id.mesonetOption

        if (mBinding != null)
            selectedTab = mBinding?.bottomNav?.selectedItemId?: 0

        LoadBinding(selectedTab)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(kSelectedTabId, mBinding?.bottomNav?.selectedItemId?: 0)
        super.onSaveInstanceState(outState)
    }


    fun CloseKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mBinding?.root?.windowToken, 0)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.ticker_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        item.isEnabled = false
        val intent = Intent(baseContext, WebViewActivity::class.java)
        intent.putExtra(WebViewActivity.kTitle, "Ticker")
        intent.putExtra(WebViewActivity.kUrl, "http://www.mesonet.org/index.php/app/ticker_article/latest.ticker.txt")
        startActivity(intent)
        item.isEnabled = true

        return super.onOptionsItemSelected(item)
    }


    public override fun onDestroy() {
        mBinding?.drawer?.removeDrawerListener(mActionBarDrawerToggle?: object: DrawerLayout.DrawerListener{
            override fun onDrawerStateChanged(p0: Int) {
            }

            override fun onDrawerSlide(p0: View, p1: Float) {
            }

            override fun onDrawerClosed(p0: View) {
            }

            override fun onDrawerOpened(p0: View) {
            }

        })

        mActionBarDrawerToggle = null

        mMesonetSiteDataController.Dispose()
        mFiveDayForecastDataController.Dispose()
        mAdvisoryDataProvider.Dispose()
        mPreferences.Dispose()

        super.onDestroy()
    }


    override fun onNavigationItemSelected(inMenuItem: MenuItem): Boolean {
        inMenuItem.isEnabled = false
        when (inMenuItem.itemId) {
            R.id.userSettings -> {
                val userSettingsIntent = Intent(baseContext, UserSettingsActivity::class.java)

                startActivity(userSettingsIntent)
            }
            R.id.contact -> {
                val contactIntent = Intent(baseContext, ContactActivity::class.java)

                startActivity(contactIntent)
            }
            R.id.about -> {
                val intent = Intent(baseContext, WebViewActivity::class.java)
                intent.putExtra(WebViewActivity.kTitle, getString(R.string.AboutAndTermsOfUse))
                intent.putExtra(WebViewActivity.kRaw, "file:///android_res/raw/about.html")
                startActivity(intent)
            }
        }

        mBinding?.drawer?.closeDrawer(Gravity.START)

        inMenuItem.isEnabled = true
        return false
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        item.isEnabled = false
        val intent = Intent(baseContext, WebViewActivity::class.java)
        intent.putExtra(WebViewActivity.kTitle, "Ticker")
        intent.putExtra(WebViewActivity.kUrl, "http://www.mesonet.org/index.php/app/ticker_article/latest.ticker.txt")
        startActivity(intent)
        item.isEnabled = true

        return false
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int,
                                  data: Intent)
    {
        if(requestCode == mPermissions.LocationRequestCode())
        {
            mLocationProvider.RegisterGpsResult(resultCode)
        }
    }


    override fun NavigateToPage(inFragment: Fragment, inPushToBackStack: Boolean, @AnimRes inAnimationIn: Int, @AnimRes inAnimationOut: Int) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        if (inAnimationIn != 0 || inAnimationOut != 0)
            fragmentTransaction.setCustomAnimations(inAnimationIn, inAnimationOut)

        fragmentTransaction.replace(R.id.fragmentLayout, inFragment)

        if (inPushToBackStack)
            fragmentTransaction.addToBackStack(inFragment.javaClass.name)
        fragmentTransaction.commit()
    }
}