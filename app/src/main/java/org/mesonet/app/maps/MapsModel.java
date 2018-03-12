package org.mesonet.app.maps;


import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class MapsModel
{
    @SerializedName("main")
    private List<MainModel> mMain;

    @SerializedName("sections")
    private Map<String, SectionModel> mSections;

    @SerializedName("products")
    private Map<String, ProductModel> mProducts;


    public List<MainModel> GetMain()
    {
        return mMain;
    }



    public Map<String, SectionModel> GetSections()
    {
        return mSections;
    }



    public Map<String, ProductModel> GetProducts()
    {
        return mProducts;
    }


    public static class MainModel
    {
        @SerializedName("sections")
        private List<String> mSections;

        @SerializedName("title")
        private String mTitle;



        public List<String> GetSections()
        {
            return mSections;
        }



        public String GetTitle()
        {
            return mTitle;
        }
    }



    public static class SectionModel
    {
        @SerializedName("title")
        public String mTitle;

        @SerializedName("products")
        private List<String> mProducts;



        public String GetTitle()
        {
            return mTitle;
        }



        public List<String> GetProducts()
        {
            return mProducts;
        }
    }



    public static class ProductModel
    {
        @SerializedName("url")
        public String mUrl;

        @SerializedName("title")
        public String mTitle;
    }
}
