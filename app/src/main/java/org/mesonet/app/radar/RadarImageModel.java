package org.mesonet.app.radar;


import android.content.Context;
import android.graphics.Bitmap;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RadarImageModel
{
    private String filename;
    private String timestring;
    private long datatime;
    private long looptime;

    private BaseRadarDataController mRadarDataController;

    private List<BaseRadarDataController.ImageLoadedListener> mImageLoadedListeners = new ArrayList<>();


    public void GetImage(Context inContext, BaseRadarDataController.ImageLoadedListener inImageLoadedListener)
    {
        mRadarDataController.GetImage(inContext, "https://www.mesonet.org" + filename, inImageLoadedListener);
    }



    public String GetTimeString()
    {
        return timestring;
    }


    public static class Builder
    {
        private RadarImageModel mResult = new RadarImageModel();


        public Builder(BaseRadarDataController inRadarDataController)
        {
            mResult.mRadarDataController = inRadarDataController;
        }



        public void SetValue(String inKey, String inValue)
        {
            Field field = FindField(inKey);

            if(field != null)
            {
                try {
                    field.setAccessible(true);
                    if(field.getType() == String.class)
                        field.set(mResult, inValue);
                    else if(field.getType() == long.class)
                        field.set(mResult, Long.parseLong(inValue));
                }
                catch (IllegalAccessException | NumberFormatException e)
                {
                    e.printStackTrace();
                }

                field.setAccessible(false);
            }
        }



        private Field FindField(String inName)
        {
            Field[] fields = RadarImageModel.class.getDeclaredFields();

            if(fields != null)
            {
                for (int i = 0; i < fields.length; i++)
                {
                    if(fields[i].getName().equals(inName))
                        return fields[i];
                }
            }

            return null;
        }



        public RadarImageModel Build()
        {
            return mResult;
        }
    }
}
