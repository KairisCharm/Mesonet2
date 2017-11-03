package org.mesonet.app;


import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;



public class DataDownloader
{
    private static DataDownloader sDataDownloader;



    public static DataDownloader GetInstance()
    {
        if(sDataDownloader == null)
            sDataDownloader = new DataDownloader();

        return sDataDownloader;
    }



    private DataDownloader(){}



    public void Download (final String inUrl, final long inIfModifiedSince, DownloadCallback inDownloadCallback, DownloadUpdateCheckListener inDownloadUpdateCheckListener)
    {
        new DownloadAsyncTask().execute(new DownloadInfo(inUrl, inIfModifiedSince, inDownloadCallback, inDownloadUpdateCheckListener));
    }



    private static class DownloadAsyncTask extends AsyncTask<DownloadInfo, Object, DownloadInfo[]>
    {
        @Override
        protected DownloadInfo[] doInBackground (DownloadInfo... inDownloadInfo)
        {
            for(DownloadInfo dlInfo : inDownloadInfo)
            {
                try
                {
                    URL url = new URL(dlInfo.mUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);

                    conn.setIfModifiedSince(dlInfo.mModifiedSinceTime);

                    conn.connect();

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK)
                    {
                        StringBuilder resultString = new StringBuilder();

                        Scanner scanner = new Scanner(conn.getInputStream());

                        while (scanner.hasNextLine())
                        {
                            resultString.append(scanner.nextLine());
                        }

                        scanner.close();

                        conn.disconnect();

                        dlInfo.mResult = resultString.toString();
                        dlInfo.mResponseCode = conn.getResponseCode();

                        if(dlInfo.mDownloadUpdateCheckListener != null && dlInfo.mDownloadUpdateCheckListener.ContinueUpdates())
                        {
                            SetUpdateTimer(conn.getDate(), dlInfo.mDownloadUpdateCheckListener);
                        }
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    dlInfo.mDownloadCallback.DownloadFailed();
                }
            }

            return inDownloadInfo;
        }



        @Override
        protected void onPostExecute(DownloadInfo[] inResult)
        {
            for(DownloadInfo dlInfo : inResult)
            {
                if(dlInfo != null)
                    dlInfo.mDownloadCallback.DownloadComplete(dlInfo.mResponseCode,
                                                              dlInfo.mResult);
            }
        }



        public void SetUpdateTimer (final long inLastUpdated, final DownloadUpdateCheckListener inDownloadUpdateCheckListener)
        {
            if(inDownloadUpdateCheckListener == null)
                return;

            long interval = inDownloadUpdateCheckListener.GetInterval();

            if(interval > 0)
            {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable()
                                          {
                                              @Override
                                              public void run ()
                                              {
                                                  inDownloadUpdateCheckListener.CheckForUpdate(inLastUpdated);
                                              }
                                          },
                                          interval);
            }
        }
    }



    public interface DownloadCallback
    {
        void DownloadComplete (int inResponseCode, String inResult);
        void DownloadFailed();
    }



    public interface DownloadUpdateCheckListener
    {
        void CheckForUpdate(long inLastUpdated);
        boolean ContinueUpdates();
        long GetInterval();
    }



    private static class DownloadInfo
    {
        final String mUrl;
        final long mModifiedSinceTime;
        final DownloadCallback mDownloadCallback;
        final DownloadUpdateCheckListener mDownloadUpdateCheckListener;
        String mResult;
        int mResponseCode = -1;



        public DownloadInfo(String inUrl, long inModifiedSinceTime, DownloadCallback inDownloadCallback, DownloadUpdateCheckListener inDownloadUpdateCheckListener)
        {
            mUrl = inUrl;
            mModifiedSinceTime = inModifiedSinceTime;
            mDownloadCallback = inDownloadCallback;
            mDownloadUpdateCheckListener = inDownloadUpdateCheckListener;
        }
    }
}
