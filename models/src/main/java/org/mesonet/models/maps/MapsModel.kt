package org.mesonet.models.maps


import com.google.gson.annotations.SerializedName

class MapsModel {
    @SerializedName("main")
    private val mMain: List<MainModel>? = null

    @SerializedName("sections")
    private val mSections: Map<String, SectionModel>? = null

    @SerializedName("products")
    private val mProducts: Map<String, ProductModel>? = null


    fun GetMain(): List<MainModel>? {
        return mMain
    }


    fun GetSections(): Map<String, SectionModel>? {
        return mSections
    }


    fun GetProducts(): Map<String, ProductModel>? {
        return mProducts
    }


    class MainModel {
        @SerializedName("sections")
        private val mSections: List<String>? = null

        @SerializedName("title")
        private val mTitle: String? = null


        fun GetSections(): List<String>? {
            return mSections
        }


        fun GetTitle(): String? {
            return mTitle
        }
    }


    class SectionModel {
        @SerializedName("title")
        private var mTitle: String? = null

        @SerializedName("products")
        private val mProducts: List<String>? = null


        fun GetTitle(): String? {
            return mTitle
        }


        fun GetProducts(): List<String>? {
            return mProducts
        }
    }


    class ProductModel {
        @SerializedName("url")
        private var mUrl: String? = null

        @SerializedName("title")
        private var mTitle: String? = null


        fun GetUrl(): String?
        {
            return mUrl
        }


        fun GetTitle(): String?
        {
            return mTitle
        }
    }
}
