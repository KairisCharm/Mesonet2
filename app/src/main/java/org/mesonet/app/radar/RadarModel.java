package org.mesonet.app.radar;

import org.mesonet.app.caching.Cacher;
import org.mesonet.app.dependencyinjection.PerFragment;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;



public class RadarModel
{
    private Map<String, RadarDetailModel> mRadarDetailMap = new HashMap<>();



    public static class RadarDetailModel
    {
        private String state;
        private String name;
        private float latitude;
        private float longitude;
        private float latDelta;
        private float lonDelta;
        private float range;



        public float GetLatitude()
        {
            return latitude;
        }



        public float GetLongitude()
        {
            return longitude;
        }



        public float GetLatDelta()
        {
            return latDelta;
        }



        public float GetLonDelta()
        {
            return lonDelta;
        }



        public float GetRange()
        {
            return range;
        }



        public String GetName()
        {
            return name;
        }



        public static class Builder
        {
            private RadarDetailModel mResult = new RadarDetailModel();



            public void SetValue(String inKey, String inType, String inValue)
            {
                Field field = FindField(inKey);

                if(field != null)
                {
                    try {
                        field.setAccessible(true);
                        if(inType.equals("string") && field.getType() == String.class)
                            field.set(mResult, inValue);
                        else if(inType.equals("real") && field.getType() == float.class)
                            field.set(mResult, Float.parseFloat(inValue));
                    }
                    catch (IllegalAccessException e)
                    {
                        e.printStackTrace();
                    }

                    field.setAccessible(false);
                }
            }



            private Field FindField(String inName)
            {
                Field[] fields = RadarDetailModel.class.getDeclaredFields();

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



            public RadarDetailModel Build()
            {
                return mResult;
            }
        }
    }
}
