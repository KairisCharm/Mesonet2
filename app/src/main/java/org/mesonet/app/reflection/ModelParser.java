package org.mesonet.app.reflection;



public class ModelParser
{
    private static ModelParser sModelParser;



    public static ModelParser GetInstance()
    {
        if(sModelParser == null)
            sModelParser = new ModelParser();

        return sModelParser;
    }



    private ModelParser(){}



    public <T> T Parse(Class<T> inModelClass, String inStrValues)
    {
        return null;
    }
}
