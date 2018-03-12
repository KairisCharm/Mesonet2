package org.mesonet.app.radar;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Xml;

import com.squareup.picasso.Picasso;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.inject.Inject;



public abstract class BaseRadarDataController extends Observable implements Observer
{
    RadarSiteDataProvider mSiteDataProvider;

    List<RadarImageModel> mRadarImages;



    public BaseRadarDataController(RadarSiteDataProvider inSiteDataProvider)
    {
        mSiteDataProvider = inSiteDataProvider;

        mSiteDataProvider.addObserver(this);
    }


    protected void ProcessRadarXml(InputStream inRadarXmlStream)
    {
        XmlPullParser xmlParser = Xml.newPullParser();

        try {
            xmlParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlParser.setInput(inRadarXmlStream, null);
            mRadarImages = ParseRadarXml(xmlParser, 6);
            setChanged();
            notifyObservers();
        }
        catch (XmlPullParserException | IOException e)
        {
            e.printStackTrace();
        }

        try {
            inRadarXmlStream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }



    private List<RadarImageModel> ParseRadarXml(XmlPullParser inXmlParser, int inLimit) throws XmlPullParserException, IOException
    {
        List<RadarImageModel> result = new ArrayList<>();

        while (inXmlParser.next() != XmlPullParser.END_DOCUMENT && result.size() < inLimit)
        {
            if (inXmlParser.getEventType() != XmlPullParser.START_TAG)
                continue;

            String name = inXmlParser.getName();
            // Starts by looking for the entry tag
            if (name.equals("frame"))
            {
                RadarImageModel.Builder builder = new RadarImageModel.Builder(this);
                for(int i = 0; i < inXmlParser.getAttributeCount(); i++)
                {
                    builder.SetValue(inXmlParser.getAttributeName(i), inXmlParser.getAttributeValue(i));
                }

                result.add(builder.Build());
            }
        }

        return result;
    }



    public List<RadarImageModel> GetRadarImageDetails()
    {
        return mRadarImages;
    }



    public void GetImage(final Context inContext, final String inImagePath, final ImageLoadedListener inImageLoadedListener)
    {
        Looper callingLooper = Looper.myLooper();

        if(callingLooper == null)
        {
            Looper.prepare();
            callingLooper = Looper.myLooper();
        }

        final Looper finalCallingLooper = callingLooper;

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Bitmap original = Picasso.with(inContext).load(inImagePath).get();

                    Matrix matrix = new Matrix();
                    matrix.postScale(2, 2);
                    final Bitmap resizedBitmap = Bitmap.createBitmap(1200, 1200, Bitmap.Config.ARGB_8888);

                    Canvas canvas = new Canvas(resizedBitmap);
                    canvas.drawBitmap(original, matrix, new Paint());
                    canvas.save();

                    original.recycle();

                    new Handler(finalCallingLooper, new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message message) {
                            inImageLoadedListener.BitmapLoaded(resizedBitmap);
                            return false;
                        }
                    }).sendEmptyMessage(0);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    @Override
    public void update(Observable o, Object arg) {
        Update();
    }



    protected abstract void Update();

    public interface ImageLoadedListener
    {
        void BitmapLoaded(Bitmap inResult);
    }
}
