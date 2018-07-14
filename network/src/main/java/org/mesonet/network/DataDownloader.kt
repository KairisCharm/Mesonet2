package org.mesonet.network

import android.graphics.Bitmap
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.mesonet.models.advisories.Advisory
import org.mesonet.models.advisories.AdvisoryConverter
import org.mesonet.models.advisories.AdvisoryModelConverterFactory
import org.mesonet.models.maps.MapsList
import org.mesonet.models.maps.MapsModel
import org.mesonet.models.radar.*
import org.mesonet.models.site.MesonetSiteList
import org.mesonet.models.site.MesonetSiteListModel
import org.mesonet.models.site.forecast.Forecast
import org.mesonet.models.site.forecast.ForecastConverter
import org.mesonet.models.site.forecast.ForecastModelConverterFactory
import org.mesonet.models.site.mesonetdata.MesonetData
import org.mesonet.models.site.mesonetdata.MesonetDataConverter
import org.mesonet.models.site.mesonetdata.MesonetModelConverterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DataDownloader @Inject constructor() {
    private val mMesonetService = Retrofit.Builder()
            .baseUrl("http://www.mesonet.org")
            .addConverterFactory(MesonetModelConverterFactory())
            .addConverterFactory(ForecastModelConverterFactory())
            .addConverterFactory(AdvisoryModelConverterFactory())
            .addConverterFactory(RadarHistoryConverterFactory())
            .addConverterFactory(BitmapConverterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(MesonetService::class.java)


    fun GetMesonetSites(): Observable<MesonetSiteList> {
        return mMesonetService.GetMesonetSites()
                .map { it as MesonetSiteList }
                .subscribeOn(Schedulers.io())
    }


    fun GetMesonetData(inStid: String): Observable<MesonetData> {
        return mMesonetService.GetMesonetData(inStid).subscribeOn(Schedulers.computation())
    }


    fun GetForecast(inStid: String): Observable<List<Forecast>> {
        return mMesonetService.GetForecast(inStid).subscribeOn(Schedulers.io())
    }


    fun GetForecastImage(inFileName: String): String {
        return mMesonetService.GetForecastIcon(inFileName).request().url().toString()
    }


    fun GetMaps(): Observable<MapsList> {
        return mMesonetService.GetMaps()
                .map { it as MapsList }
                .subscribeOn(Schedulers.io())
    }


    fun GetRadarHistory(inSiteId: String): Observable<RadarHistory> {
        return mMesonetService.GetRadarHistory(inSiteId).map { it as RadarHistory }.subscribeOn(Schedulers.io())
    }


    fun GetRadarImage(inSiteId: String,
                      inImageId: String): Observable<Bitmap>
    {
        return mMesonetService.GetRadarImage(inSiteId, inImageId)
    }


    fun GetAdvisoriesList(): Observable<Advisory.AdvisoryList>
    {
        return mMesonetService.GetAdvisoriesList().subscribeOn(Schedulers.io())
    }





    interface MesonetService
    {
        @GET("/find/siteinfo-json")
        fun GetMesonetSites(): Observable<MesonetSiteListModel>


        @MesonetDataConverter
        @GET("/index.php/app/latest_iphone/{stid}")
        fun GetMesonetData(@Path("stid") inStid: String): Observable<MesonetData>


        @ForecastConverter
        @GET("/index.php/app/forecast/{stid}")
        fun GetForecast(@Path("stid") inStid: String): Observable<List<Forecast>>


        @GET("/images/fcicons-android/{imageId}@4x.png")
        fun GetForecastIcon(@Path("imageId") inImageId: String): Call<Bitmap>


        @GET("/data/public/mesonet/meteograms/{stid}")
        fun GetMeteogram(@Path("stid") inStid: String): Observable<Bitmap>


        @GET("http://content.mesonet.org/mesonet/mobile-app/products.json")
        fun GetMaps(): Observable<MapsModel>


        @RadarHistoryConverter
        @GET("/data/nids/maps/realtime/frames_{siteId}_N0Q.xml")
        fun GetRadarHistory(@Path("siteId") inSite: String): Observable<RadarHistoryModel>


        @GET("/data/nids/maps/realtime/storage/{siteId}/N0Q/{imageId}.fullrange.gif")
        fun GetRadarImage(@Path("siteId") inSite: String,
                          @Path("imageId") inImageId: String) : Observable<Bitmap>


        @AdvisoryConverter
        @GET("/data/public/noaa/wwa/mobile-ok.txt")
        fun GetAdvisoriesList(): Observable<Advisory.AdvisoryList>
    }
}
