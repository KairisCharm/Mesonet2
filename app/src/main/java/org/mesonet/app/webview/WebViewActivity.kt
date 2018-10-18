package org.mesonet.app.webview

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebViewClient
import org.mesonet.app.R

import org.mesonet.app.databinding.WebViewActivityBinding
import java.util.*


class WebViewActivity : AppCompatActivity() {
    var mPaused = true
    var mLastUpdate = 0L
    var mBinding: WebViewActivityBinding? = null

    public override fun onCreate(inSavedInstanceState: Bundle?) {
        super.onCreate(inSavedInstanceState)
        mBinding = DataBindingUtil.inflate<WebViewActivityBinding>(LayoutInflater.from(this), R.layout.web_view_activity, null, false)

        val title = intent.getStringExtra(kTitle)
        var url: String? = null

        if(intent.hasExtra(kUrl))
            url = intent.getStringExtra(kUrl)

        if (url != null && intent.getBooleanExtra(kUseGoogleDocs, false))
            url = "http://docs.google.com/gview?embedded=true&url=$url"// + URLEncoder.encode(url, "UTF-8")

        if (intent.getBooleanExtra(kAllowUserZoom, false)) {
            mBinding?.webView?.settings?.setSupportZoom(true)
            mBinding?.webView?.settings?.builtInZoomControls = true
        }

        val webSettings = mBinding?.webView?.settings
        webSettings?.javaScriptEnabled = true
        mBinding?.webView?.webViewClient = WebViewClient()

        mBinding?.toolBar?.title = title
        mBinding?.toolBar?.setNavigationIcon(R.drawable.ic_close_white_36dp)
        mBinding?.toolBar?.setNavigationOnClickListener { finish() }

        val finalUrl = url

        if (intent.getBooleanExtra(kAllowShare, false)) {
            mBinding?.shareButton?.visibility = View.VISIBLE
            mBinding?.shareButton?.setOnClickListener {
                it.isEnabled = false
                val i = Intent(Intent.ACTION_SEND)
                i.type = "text/plain"
                i.putExtra(Intent.EXTRA_SUBJECT, title)
                i.putExtra(Intent.EXTRA_TEXT, finalUrl)
                startActivity(Intent.createChooser(i, "Share URL"))
                it.isEnabled = true
            }
        }

        val initialScale = intent.getIntExtra(kInitialZoom, -1)

        if (initialScale != -1) {
            mBinding?.webView?.setInitialScale(initialScale)

            mBinding?.webView?.settings?.loadWithOverviewMode = true
            mBinding?.webView?.settings?.useWideViewPort = true
        }

        if(url != null)
            mBinding?.webView?.loadUrl(url)
        else if(intent.hasExtra(kRaw))
            mBinding?.webView?.loadUrl(intent.getStringExtra(kRaw))

        mLastUpdate = Date().time

        setContentView(mBinding?.root)
    }

    override fun onResume() {
        super.onResume()

        val updateInterval = intent.getLongExtra(kUpdateInterval, -1)

        if(mPaused) {
            mPaused = false
            if (updateInterval > -1) {
                Handler().post(object : Runnable {
                    override fun run() {
                        if (!mPaused) {
                            if(mLastUpdate - updateInterval < Date().time) {
                                mBinding?.webView?.reload()
                                mLastUpdate = Date().time
                            }
                            Handler().postDelayed(this, updateInterval)
                        }
                    }
                })
            }
        }
    }

    override fun onPause() {
        mPaused = true

        super.onPause()
    }

    companion object {
        val kTitle = "title"
        val kUrl = "url"
        val kRaw = "raw"
        val kInitialZoom = "initialZoom"
        val kAllowUserZoom = "allowUserZoom"
        val kUseGoogleDocs = "useGoogleDocs"
        val kAllowShare = "allowShare"
        val kUpdateInterval = "updateInterval"
    }
}
