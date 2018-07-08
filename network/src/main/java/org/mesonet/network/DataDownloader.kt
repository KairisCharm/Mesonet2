package org.mesonet.network

import android.graphics.Bitmap
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.mesonet.models.advisories.Advisory
import org.mesonet.models.advisories.AdvisoryModel
import org.mesonet.models.advisories.AdvisoryModelConverterFactory
import org.mesonet.models.maps.MapsList
import org.mesonet.models.maps.MapsModel
import org.mesonet.models.radar.*
import org.mesonet.models.site.MesonetSiteList
import org.mesonet.models.site.MesonetSiteListModel
import org.mesonet.models.site.forecast.Forecast
import org.mesonet.models.site.forecast.ForecastModelConverterFactory
import org.mesonet.models.site.mesonetdata.MesonetData
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
class DataDownloader @Inject constructor()
{
    private val mJsonRetrofitService = Retrofit.Builder()
            .baseUrl("http://www.mesonet.org")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(MesonetService::class.java)

    private val mMesonetDataRetrofitService = Retrofit.Builder()
            .baseUrl("http://www.mesonet.org")
            .addConverterFactory(MesonetModelConverterFactory())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(MesonetService::class.java)

    private val mForecastRetrofitService = Retrofit.Builder()
            .baseUrl("http://www.mesonet.org")
            .addConverterFactory(ForecastModelConverterFactory())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(MesonetService::class.java)

    private val mRadarHistoryRetrofitService = Retrofit.Builder()
            .baseUrl("http://www.mesonet.org")
            .addConverterFactory(RadarHistoryModelConverterFactory())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(MesonetService::class.java)

    private val mAdvisoryRetrofitService = Retrofit.Builder()
            .baseUrl("http://www.mesonet.org")
            .addConverterFactory(AdvisoryModelConverterFactory())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(MesonetService::class.java)

    fun GetMesonetSites(): Observable<MesonetSiteList>
    {
        return mJsonRetrofitService.GetMesonetSites()
                .map { it as MesonetSiteList }
                .subscribeOn(Schedulers.io())
    }


    fun GetMesonetData(inStid: String): Observable<MesonetData>
    {
        return mMesonetDataRetrofitService.GetMesonetData(inStid).subscribeOn(Schedulers.computation())
    }


    fun GetForecast(inStid: String): Observable<List<Forecast>>
    {
        return mForecastRetrofitService.GetForecast(inStid).subscribeOn(Schedulers.io())
    }


    fun GetForecastImage(inFileName: String): String
    {
        return mJsonRetrofitService.GetForecastIcon(inFileName).request().url().toString()
    }



    fun GetMaps(): Observable<MapsList>
    {
        return mJsonRetrofitService.GetMaps()
                .map{ it as MapsList }
                .subscribeOn(Schedulers.io())
    }


    fun GetRadarHistory(inRadarId: String): Observable<List<RadarImageInfo>>
    {
        return mRadarHistoryRetrofitService.GetRadarHistory(inRadarId).subscribeOn(Schedulers.io())
    }


    fun GetAdvisoriesList(): Observable<ArrayList<Advisory>>
    {
        return mAdvisoryRetrofitService.GetAdvisoriesList().subscribeOn(Schedulers.io())
    }


//    fun Download(inUrl: String, inLastModified: Long? = null, inUpdateInterval: Long? = null): Observable<ResponseInfo>
//    {
//        val observable = Observable.create(Observable.OnSubscribe<ResponseInfo>{
//            val result = ResponseInfo()
//            var conn: HttpURLConnection? = null
//            var scanner: Scanner? = null
//            var resultString: StringBuilder? = null
//            try {
//                val url = URL(inUrl)
//                conn = url.openConnection() as HttpURLConnection
//                conn.readTimeout = 10000
//                conn.connectTimeout = 15000
//                conn.requestMethod = "GET"
//                conn.doInput = true
//
//                if(inLastModified != null)
//                    conn.ifModifiedSince = inLastModified
//
//                conn.connect()
//
//                if (conn.responseCode >= HttpURLConnection.HTTP_OK && conn.responseCode < HTTP_MULT_CHOICE) {
//                    resultString = StringBuilder()
//
//                    scanner = Scanner(conn.inputStream)
//
//                    while (scanner.hasNextLine()) {
//                        resultString.append(scanner.nextLine())
//                        if(scanner.hasNextLine())
//                            resultString.append("\n")
//                    }
//
//                }
//            } catch (e: IOException) {
//                e.printStackTrace()
//                it.onError(e)
//            }
//            catch (e: MalformedURLException)
//            {
//                e.printStackTrace()
//                it.onError(e)
//            }
//            catch (e: SocketTimeoutException)
//            {
//                e.printStackTrace()
//                it.onError(e)
//            }
//            finally {
//                if(resultString != null)
//                    result.mResponse = resultString.toString()
//                if(conn != null) {
//                    try {
//                        result.mResponseCode = conn.responseCode
//                    }
//                    catch (e: InvocationTargetException)
//                    {
//                        e.printStackTrace()
//                        it.onError(e)
//                    }
//                    catch (e: SocketTimeoutException)
//                    {
//                        e.printStackTrace()
//                        it.onError(e)
//                    }
//                    conn.disconnect()
//                }
//                scanner?.close()
//
//                it.onNext(result)
//            }
//        }).subscribeOn(Schedulers.io())
//
//        if(inUpdateInterval != null)
//            observable.repeat(inUpdateInterval, Schedulers.io())
//
//        return observable
//    }
//
//
//    class ResponseInfo(var mResponse: String? = null, var mResponseCode: Int = 0)
//    {
//        fun IsSuccessful(): Boolean
//        {
//            return mResponseCode >= HttpURLConnection.HTTP_OK && mResponseCode < HttpURLConnection.HTTP_BAD_REQUEST
//        }
//    }



    interface MesonetService
    {
        @GET("/find/siteinfo-json")
        fun GetMesonetSites(): Observable<MesonetSiteListModel>


        @GET("/index.php/app/latest_iphone/{stid}")
        fun GetMesonetData(@Path("stid") inStid: String): Observable<MesonetData>


        @GET("/index.php/app/forecast/{stid}")
        fun GetForecast(@Path("stid") inStid: String): Observable<List<Forecast>>


        @GET("/images/fcicons-android/{imageId}@4x.png")
        fun GetForecastIcon(@Path("imageId") inImageId: String): Call<Bitmap>



        @GET("/data/public/mesonet/meteograms/{stid}")
        fun GetMeteogram(@Path("stid") inStid: String): Observable<Bitmap>


        @GET("http://content.mesonet.org/mesonet/mobile-app/products.json")
        fun GetMaps(): Observable<MapsModel>


        @GET("http://www.mesonet.org/data/nids/maps/realtime/frames_{site}_N0Q.xml")
        fun GetRadarHistory(@Path("site") inSite: String): Observable<List<RadarImageInfo>>


        @GET("/data/public/noaa/wwa/mobile-ok.txt")
        fun GetAdvisoriesList(): Observable<ArrayList<Advisory>>
    }
}
