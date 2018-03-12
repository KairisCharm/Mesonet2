package org.mesonet.app.radar;


import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.util.Pair;
import android.util.Xml;

import org.mesonet.app.BasicViewHolder;
import org.mesonet.app.dependencyinjection.PerFragment;
import org.mesonet.app.filterlist.FilterListFragment;
import org.mesonet.app.site.SiteSelectionInterfaces;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import javax.inject.Inject;


@PerFragment
public class RadarSiteDataProvider extends Observable implements FilterListFragment.FilterListDataProvider, SiteSelectionInterfaces.SelectSiteListener
{
    private String mSelectedRadar = "KTLX";
    Map<String, RadarModel.RadarDetailModel> mRadarDetail;



    @Inject
    public RadarSiteDataProvider(Activity inContext)
    {
        InputStream radarList = inContext.getResources().openRawResource(inContext.getResources().getIdentifier("radar_list", "raw", inContext.getPackageName()));
        mRadarDetail = ProcessRadarXml(radarList);
        setChanged();
        notifyObservers();
    }



    private Map<String, RadarModel.RadarDetailModel> ProcessRadarXml(InputStream inRadarXmlStream)
    {
        XmlPullParser xmlParser = Xml.newPullParser();

        Map<String, RadarModel.RadarDetailModel> result = null;

        try {
            xmlParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlParser.setInput(inRadarXmlStream, null);
            result = ParseRadarXml(xmlParser);
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

        return result;
    }



    private Map<String, RadarModel.RadarDetailModel> ParseRadarXml(XmlPullParser inXmlParser) throws XmlPullParserException, IOException
    {
        Map<String, RadarModel.RadarDetailModel> result = new HashMap<>();

        while (inXmlParser.next() != XmlPullParser.END_TAG)
        {
            if (inXmlParser.getEventType() != XmlPullParser.START_TAG)
                continue;

            String name = inXmlParser.getName();
            // Starts by looking for the entry tag
            if (name.equals("key"))
            {
                if(inXmlParser.next() == XmlPullParser.TEXT)
                {
                    String key = inXmlParser.getText();

                    inXmlParser.next();
                    inXmlParser.next();
                    inXmlParser.next();

                    if(inXmlParser.getName().equals("dict"))
                        result.put(key, ParseRadarDetail(inXmlParser));
                }
            }
        }

        return result;
    }



    private RadarModel.RadarDetailModel ParseRadarDetail(XmlPullParser inXmlParser) throws XmlPullParserException, IOException
    {
        RadarModel.RadarDetailModel.Builder builder = new RadarModel.RadarDetailModel.Builder();

        while (inXmlParser.next() != XmlPullParser.END_TAG || (inXmlParser.getName() == null || !inXmlParser.getName().equals("dict")))
        {
            if (inXmlParser.getEventType() != XmlPullParser.START_TAG)
                continue;

            if(inXmlParser.getName().equals("key"))
            {
                inXmlParser.next();

                String key = inXmlParser.getText();

                inXmlParser.next();
                inXmlParser.next();
                inXmlParser.next();

                String type = inXmlParser.getName();

                inXmlParser.next();

                String value = inXmlParser.getText();

                builder.SetValue(key, type, value);
            }
        }

        return builder.Build();
    }



    public RadarModel.RadarDetailModel GetRadarDetail(String inKey)
    {
        return mRadarDetail.get(inKey);
    }



    public RadarModel.RadarDetailModel GetRadarDetail()
    {
        return GetRadarDetail(mSelectedRadar);
    }



    public String GetRadarName()
    {
        return mSelectedRadar + " - " + mRadarDetail.get(mSelectedRadar).GetName();
    }

    @Override
    public Map<String, BasicViewHolder.BasicViewHolderData> AllViewHolderData() {
        Map<String, BasicViewHolder.BasicViewHolderData> data = new LinkedHashMap<>();

        List<String> keys = new ArrayList<>(mRadarDetail.keySet());

        for(int i = 0; i < keys.size(); i++)
        {
            RadarModel.RadarDetailModel radar = mRadarDetail.get(keys.get(i));

            Location location = new Location("none");
            location.setLatitude(radar.GetLatitude());
            location.setLongitude(radar.GetLongitude());

            data.put(keys.get(i), new BasicViewHolder.BasicViewHolderData(keys.get(i) + " - " + mRadarDetail.get(keys.get(i)).GetName(), location, false));
        }

        return data;
    }

    @Override
    public String CurrentSelection() {
        return mSelectedRadar;
    }

    @Override
    public Observable GetDataObservable() {
        return this;
    }

    @Override
    public void SetResult(String inResult) {
        mSelectedRadar = inResult;
        setChanged();
        notifyObservers();
    }
}
