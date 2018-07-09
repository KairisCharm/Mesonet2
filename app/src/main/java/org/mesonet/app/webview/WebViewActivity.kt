package org.mesonet.app.webview

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.SystemClock
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebSettings

import org.mesonet.app.R
import org.mesonet.app.databinding.WebViewActivityBinding

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*


class WebViewActivity : AppCompatActivity() {


    public override fun onCreate(inSavedInstanceState: Bundle?) {
        super.onCreate(inSavedInstanceState)
        val binding = DataBindingUtil.inflate<WebViewActivityBinding>(LayoutInflater.from(this), R.layout.web_view_activity, null, false)

        val title = intent.getStringExtra(kTitle)
        var url: String? = null

        if(intent.hasExtra(kUrl))
            url = intent.getStringExtra(kUrl)

        if (url != null && intent.getBooleanExtra(kUseGoogleDocs, false))
            url = "http://docs.google.com/gview?embedded=true&url=$url"

        if (intent.getBooleanExtra(kAllowUserZoom, false)) {
            binding.webView.settings.setSupportZoom(true)
            binding.webView.settings.builtInZoomControls = true
        }

        val webSettings = binding.webView.settings
        webSettings.javaScriptEnabled = true

        binding.toolBar.title = title
        binding.toolBar.setNavigationIcon(R.drawable.ic_close_white_36dp)
        binding.toolBar.setNavigationOnClickListener { finish() }

        val finalUrl = url

        if (intent.getBooleanExtra(kAllowShare, false)) {
            binding.shareButton.visibility = View.VISIBLE
            binding.shareButton.setOnClickListener {
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
            binding.webView.setInitialScale(initialScale)

            binding.webView.settings.loadWithOverviewMode = true
            binding.webView.settings.useWideViewPort = true
        }

        if(url != null)
            binding.webView.loadUrl(url)
        else if(intent.hasExtra(kRaw))
            binding.webView.loadUrl(intent.getStringExtra(kRaw))

        setContentView(binding.root)
    }

    companion object {
        val kTitle = "title"
        val kUrl = "url"
        val kRaw = "raw"
        val kInitialZoom = "initialZoom"
        val kAllowUserZoom = "allowUserZoom"
        val kUseGoogleDocs = "useGoogleDocs"
        val kAllowShare = "allowShare"
    }
}
