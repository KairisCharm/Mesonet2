package org.mesonet.app.reflection;


import java.lang.reflect.Field;

public class MesonetModelParser
{
    private static MesonetModelParser sModelParser;



    public static MesonetModelParser GetInstance()
    {
        if(sModelParser == null)
            sModelParser = new MesonetModelParser();

        return sModelParser;
    }



    private MesonetModelParser(){}



    public <T> T Parse(Class<T> inModelClass, String inStrValues)
    {
        if(inModelClass == null)
            return null;

        T model = null;
        try {
            model = inModelClass.newInstance();
        }
        catch (IllegalAccessException | InstantiationException e)
        {
            e.printStackTrace();
        }

        if(model == null || inStrValues == null)
            return model;

        String[] strFields = inStrValues.split(",");
        Field[] fields = inModelClass.getDeclaredFields();

        for(int i = 0; i < strFields.length; i++)
        {
            String[] strField = strFields[i].split("=");

            if(strField.length != 2)
                continue;

            String fieldName = strField[0];
            String fieldValue = strField[1];

            for(int j = 0; j < fields.length; j++)
            {
                if(fields[j].getName().equals(fieldName))
                {
                    fields[j].setAccessible(true);
                    if(fields[j].getType() == byte.class || fields[j].getType() == Byte.class)
                    {
                        try
                        {
                            fields[j].set(model, Byte.parseByte(fieldValue));
                        }
                        catch (IllegalAccessException | IllegalArgumentException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    if(fields[j].getType() == short.class || fields[j].getType() == Short.class)
                    {
                        try
                        {
                            fields[j].set(model, Short.parseShort(fieldValue));
                        }
                        catch (IllegalAccessException | IllegalArgumentException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    if(fields[j].getType() == int.class || fields[j].getType() == Integer.class)
                    {
                        try
                        {
                            fields[j].set(model, Integer.parseInt(fieldValue));
                        }
                        catch (IllegalAccessException | IllegalArgumentException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    if(fields[j].getType() == long.class || fields[j].getType() == Long.class)
                    {
                        try
                        {
                            fields[j].set(model, Long.parseLong(fieldValue));
                        }
                        catch (IllegalAccessException | IllegalArgumentException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    if(fields[j].getType() == float.class || fields[j].getType() == Float.class)
                    {
                        try
                        {
                            fields[j].set(model, Float.parseFloat(fieldValue));
                        }
                        catch (IllegalAccessException | IllegalArgumentException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    if(fields[j].getType() == double.class || fields[j].getType() == Double.class)
                    {
                        try
                        {
                            fields[j].set(model, Double.parseDouble(fieldValue));
                        }
                        catch (IllegalAccessException | IllegalArgumentException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    if(fields[j].getType() == char.class || fields[j].getType() == Character.class)
                    {
                        try
                        {
                            fields[j].set(model, fieldValue.charAt(0));
                        }
                        catch (IllegalAccessException | IllegalArgumentException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    if(fields[j].getType() == boolean.class || fields[j].getType() == Boolean.class)
                    {
                        try
                        {
                            fields[j].set(model, Boolean.parseBoolean(fieldValue));
                        }
                        catch (IllegalAccessException | IllegalArgumentException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    if(fields[j].getType() == Number.class)
                    {
                        try
                        {
                            fields[j].set(model, Double.parseDouble(fieldValue));
                        }
                        catch (IllegalAccessException | IllegalArgumentException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    if(fields[j].getType() == String.class)
                    {
                        try
                        {
                            fields[j].set(model, fieldValue);
                        }
                        catch (IllegalAccessException | IllegalArgumentException e)
                        {
                            e.printStackTrace();
                        }
                    }

                    fields[i].setAccessible(false);
                }
            }
        }

        return model;
    }
}
