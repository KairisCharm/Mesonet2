package org.mesonet.app.webview;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;

import org.mesonet.app.DataDownloader;
import org.mesonet.app.R;
import org.mesonet.app.baseclasses.BaseDialogFragment;
import org.mesonet.app.databinding.WebViewFragmentBinding;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.inject.Inject;



public class WebViewFragment extends DialogFragment
{
    WebViewInformation mWebViewInformation;


    @Override
    public void setupDialog(Dialog dialog, int style)
    {
        final WebViewFragmentBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.web_view_fragment, null, false);

        binding.toolBar.setTitle(mWebViewInformation.Title());
        binding.toolBar.setNavigationIcon(R.drawable.ic_close_white_36dp);
        binding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        final String webInfoUrl = mWebViewInformation.Url();

        if(webInfoUrl.endsWith(".txt"))
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        String text = "";
                        URL url = new URL(webInfoUrl);
                        HttpURLConnection con=(HttpURLConnection)url.openConnection();
                        //get InputStream instance
                        InputStream is=con.getInputStream();
                        //create BufferedReader object
                        BufferedReader br=new BufferedReader(new InputStreamReader(is));
                        String line;
                        //read content of the file line by line
                        while((line=br.readLine())!=null){
                            text+=line;

                        }

                        br.close();

                        final StringBuilder sb = new StringBuilder();
                        String[] words = text.toString().split("\\s");  // assume no spaces in links

                        for (String word : words) {
                            try {
                                // Attempt to add the string as a link
                                url = new URL(word);
                                sb.append("<p>");
                                sb.append("<a href=\"");
                                sb.append(url.toString());
                                sb.append("\">");
                                sb.append(url);
                                sb.append("</a> ");
                                sb.append("</p>");
                            } catch (MalformedURLException e) {
                                // This was not a valid URL, just add it to the string
                                sb.append(word);
                                sb.append(" ");
                            }
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                binding.webView.loadData(sb.toString(), "text/html", "UTF-8");
                            }
                        });
                    }
                    catch (Exception e)
                    {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                binding.webView.loadUrl(mWebViewInformation.Url());
                            }
                        });
                    }
                }
            }).start();
        }
        else
            binding.webView.loadUrl(mWebViewInformation.Url());

        dialog.setContentView(binding.getRoot());
    }



    public void SetInfo(WebViewInformation inWebViewInformation)
    {
        mWebViewInformation = inWebViewInformation;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.WebViewStyle);
    }



    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }



    public interface WebViewInformation
    {
        String Url();
        String Title();
    }
}
