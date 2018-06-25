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
import org.mesonet.dataprocessing.maps.MapsDataProvider
import org.mesonet.dataprocessing.site.MesonetSiteDataController
import android.content.res.Configuration
import android.view.Gravity
import org.mesonet.app.contact.ContactActivity
import org.mesonet.app.usersettings.UserSettingsActivity
import java.util.*


class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener, Toolbar.OnMenuItemClickListener, Observer {

  private val kSelectedTabId = "selectedTabId"

  private var mBinding: MainActivityBinding? = null

  private var mActionBarDrawerToggle: ActionBarDrawerToggle? = null


  @Inject
  internal lateinit var mAdvisoryDataProvider: AdvisoryDataProvider

  @Inject
  internal lateinit var mMapsDataProvider: MapsDataProvider

  @Inject
  internal lateinit var mMesonetSiteDataProvider: MesonetSiteDataController


  public override fun onCreate(inSavedInstanceState: Bundle?) {

    super.onCreate(inSavedInstanceState)

    var selectedTab = R.id.mesonetOption

    if(inSavedInstanceState != null)
      selectedTab = inSavedInstanceState.getInt(kSelectedTabId)

    LoadBinding(selectedTab)

    mAdvisoryDataProvider.addObserver(this)
  }


  public override fun onResume() {
    super.onResume()
    mAdvisoryDataProvider.StartUpdates()
  }


  public override fun onPause() {
    super.onPause()
    mAdvisoryDataProvider.StopUpdates()
  }



  private fun LoadBinding(inSelectedTab: Int)
  {
    var fragment: Fragment? = null

    when (inSelectedTab) {
      R.id.mesonetOption -> fragment = SiteOverviewFragment()

      R.id.mapsOption -> fragment = MapListFragment()

      R.id.radarOption -> fragment = RadarFragment()

      R.id.advisoriesOption -> fragment = AdvisoriesFragment()
    }

    if(fragment != null) {
      val fragmentTransaction = supportFragmentManager.beginTransaction()
      fragmentTransaction.replace(R.id.fragmentLayout, fragment)
      fragmentTransaction.commit()
    }

    mBinding = DataBindingUtil.setContentView(this, R.layout.main_activity)

    mBinding!!.toolBar.inflateMenu(R.menu.ticker_menu)
    mBinding!!.toolBar.setOnMenuItemClickListener(this)

    setSupportActionBar(mBinding!!.toolBar)

    val actionBar = supportActionBar

    if (actionBar != null) {
      mActionBarDrawerToggle = object : ActionBarDrawerToggle(this, mBinding!!.drawer, mBinding!!.toolBar, R.string.app_name, R.string.app_name) {
        override fun onDrawerClosed(drawerView: View) {
          syncState()
        }

        override fun onDrawerOpened(drawerView: View) {
          syncState()
        }
      }

      mActionBarDrawerToggle!!.isDrawerIndicatorEnabled = true
      actionBar.setDisplayHomeAsUpEnabled(true)
      actionBar.setHomeButtonEnabled(true)

      mBinding!!.drawer.addDrawerListener(mActionBarDrawerToggle!!)
      mBinding!!.drawerNavView.setNavigationItemSelectedListener(this)
      mActionBarDrawerToggle!!.syncState()
    }

    mBinding!!.bottomNav.setOnNavigationItemSelectedListener { inItem ->
      var fragment: Fragment? = null

      when (inItem.itemId) {
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

      true
    }

    mBinding!!.bottomNav.itemIconTintList = null

    val menuView = mBinding!!.bottomNav.getChildAt(0) as BottomNavigationMenuView


    for (i in 0 until mBinding!!.bottomNav.menu.size()) {
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

    if(mBinding != null)
      selectedTab = mBinding?.bottomNav?.selectedItemId!!

    LoadBinding(selectedTab)
  }



  override fun onSaveInstanceState(outState: Bundle)
  {
    outState.putInt(kSelectedTabId, mBinding?.bottomNav?.selectedItemId!!)
    super.onSaveInstanceState(outState)
  }


  fun CloseKeyboard()
  {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(mBinding?.root?.windowToken, 0)
  }


  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.ticker_menu, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {

    val intent = Intent(baseContext, WebViewActivity::class.java)
    intent.putExtra(WebViewActivity.kTitle, "Ticker")
    intent.putExtra(WebViewActivity.kUrl, "http://www.mesonet.org/index.php/app/ticker_article/latest.ticker.txt")
    startActivity(intent)

    return super.onOptionsItemSelected(item)
  }


  public override fun onDestroy() {
    mBinding!!.drawer.removeDrawerListener(mActionBarDrawerToggle!!)

    mActionBarDrawerToggle = null

    mAdvisoryDataProvider.StopUpdates()

    super.onDestroy()
  }


  override fun onNavigationItemSelected(inMenuItem: MenuItem): Boolean {
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
    }//            case R.id.metricUnits:
    //                mUserSettings.SetPreference(this, UserSettings.kUnitPreference, Preferences.UnitPreference.kMetric.name());
    //                mPreferencesObservable.notifyObservers();
    //                break;
    //            case R.id.imperialUnits:
    //                mUserSettings.SetPreference(this, UserSettings.kUnitPreference, Preferences.UnitPreference.kImperial.name());
    //                mPreferencesObservable.notifyObservers();
    //                break;

    mBinding?.drawer?.closeDrawer(Gravity.START)
    return false
  }

  override fun onMenuItemClick(item: MenuItem): Boolean {
    val intent = Intent(baseContext, WebViewActivity::class.java)
    intent.putExtra(WebViewActivity.kTitle, "Ticker")
    intent.putExtra(WebViewActivity.kUrl, "http://www.mesonet.org/index.php/app/ticker_article/latest.ticker.txt")
    startActivity(intent)
    return false
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


  override fun update(o: java.util.Observable, arg: Any?) {
    runOnUiThread({
      if (mAdvisoryDataProvider.GetCount() > 0)
        mBinding!!.bottomNav.menu.getItem(3).icon = resources.getDrawable(R.drawable.advisory_badge, theme)
      else
        mBinding!!.bottomNav.menu.getItem(3).setIcon(R.drawable.advisories_image)
    })
  }
}