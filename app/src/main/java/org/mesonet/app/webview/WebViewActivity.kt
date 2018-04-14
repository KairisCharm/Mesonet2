package org.mesonet.app.webview

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
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


class WebViewActivity : AppCompatActivity() {


    public override fun onCreate(inSavedInstanceState: Bundle?) {
        super.onCreate(inSavedInstanceState)
        val binding = DataBindingUtil.inflate<WebViewActivityBinding>(LayoutInflater.from(this), R.layout.web_view_activity, null, false)

        val title = intent.getStringExtra(kTitle)
        var url = intent.getStringExtra(kUrl)

        if (intent.getBooleanExtra(kUseGoogleDocs, false))
            url = "http://docs.google.com/gview?embedded=true&url=$url"

        val webSettings = binding.webView.settings
        webSettings.javaScriptEnabled = true

        binding.toolBar.title = title
        binding.toolBar.setNavigationIcon(R.drawable.ic_close_white_36dp)
        binding.toolBar.setNavigationOnClickListener { finish() }

        val finalUrl = url

        if (intent.getBooleanExtra(kAllowShare, false)) {
            binding.shareButton.visibility = View.VISIBLE
            binding.shareButton.setOnClickListener {
                val i = Intent(Intent.ACTION_SEND)
                i.type = "text/plain"
                i.putExtra(Intent.EXTRA_SUBJECT, title)
                i.putExtra(Intent.EXTRA_TEXT, finalUrl)
                startActivity(Intent.createChooser(i, "Share URL"))
            }
        }

        val webInfoUrl = url

        val initialScale = intent.getIntExtra(kInitialZoom, -1)

        if (initialScale != -1) {
            binding.webView.setInitialScale(initialScale)

            binding.webView.settings.loadWithOverviewMode = true
            binding.webView.settings.useWideViewPort = true
        }

        //        if(webInfoUrl.endsWith(".txt"))
        //        {
        //            new Thread(new Runnable() {
        //                @Override
        //                public void run() {
        //                    try {
        //
        //                        String text = "";
        //                        URL url = new URL(webInfoUrl);
        //                        HttpURLConnection con=(HttpURLConnection)url.openConnection();
        //                        //get InputStream instance
        //                        InputStream is=con.getInputStream();
        //                        //create BufferedReader object
        //                        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        //                        String line;
        //                        //read content of the file line by line
        //                        while((line=br.readLine())!=null){
        //                            text+=line;
        //
        //                        }
        //
        //                        br.close();
        //
        //                        final StringBuilder sb = new StringBuilder();
        //                        String[] words = text.toString().split("\\s");  // assume no spaces in links
        //
        //                        for (String word : words) {
        //                            try {
        //                                // Attempt to add the string as a link
        //                                url = new URL(word);
        //                                sb.append("<p>");
        //                                sb.append("<a href=\"");
        //                                sb.append(url.toString());
        //                                sb.append("\">");
        //                                sb.append(url);
        //                                sb.append("</a> ");
        //                                sb.append("</p>");
        //                            } catch (MalformedURLException e) {
        //                                // This was not a valid URL, just add it to the string
        //                                sb.append(word);
        //                                sb.append(" ");
        //                            }
        //                        }
        //
        //                        runOnUiThread(new Runnable() {
        //                            @Override
        //                            public void run() {
        //                                binding.webView.loadData(sb.toString(), "text/html", "UTF-8");
        //                            }
        //                        });
        //                    }
        //                    catch (Exception e)
        //                    {
        //                        runOnUiThread(new Runnable() {
        //                            @Override
        //                            public void run() {
        //                                binding.webView.loadUrl(finalUrl);
        //                            }
        //                        });
        //                    }
        //                }
        //            }).start();
        //        }
        //        else
        binding.webView.loadUrl(url)

        setContentView(binding.root)
    }

    companion object {
        val kTitle = "title"
        val kUrl = "url"
        val kInitialZoom = "initialZoom"
        val kUseGoogleDocs = "useGoogleDocs"
        val kAllowShare = "allowShare"
    }
}
