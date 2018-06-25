package org.mesonet.dataprocessing.radar

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.mesonet.models.radar.RadarDetails
import org.mesonet.models.radar.RadarImageInfo
import org.mesonet.network.DataDownloader
import java.io.IOException

import javax.inject.Inject
import kotlin.collections.ArrayList


class RadarDataController @Inject
constructor(private var mSiteDataProvider: RadarSiteDataProvider) : Observable<Void>(), Observer<Pair<Map<String, RadarDetails>, String>> {


    private var mRadarImageInfo: List<RadarImageInfo> = ArrayList()
    private var mCurrentRadar = ""

    @Inject
    lateinit private var mDataDownloader: DataDownloader


    init {
        mSiteDataProvider.observeOn(Schedulers.computation()).subscribe(this)
    }


    override fun subscribeActual(observer: Observer<in Void>?)
    {
        mCurrentRadar = mSiteDataProvider.CurrentSelection()

        mDataDownloader.GetRadarHistory(mCurrentRadar).observeOn(Schedulers.computation()).subscribe {
            mRadarImageInfo = it
        }
    }


    override fun onComplete() {}
    override fun onSubscribe(d: Disposable) {}
    override fun onError(e: Throwable) {}



    internal fun GetRadarImageDetails(): List<RadarImageInfo> {
        return mRadarImageInfo
    }


    internal fun GetImage(inIndex: Int, inContext: Context): Observable<Bitmap> {
        return GetImage(inContext, "https://www.mesonet.org" + mRadarImageInfo[inIndex])
    }


    internal fun GetImage(inContext: Context, inImagePath: String): Observable<Bitmap> {

        return Observable.create(ObservableOnSubscribe<Bitmap> {
            try {
                val original = Picasso.with(inContext).load(inImagePath).get()

                val matrix = Matrix()
                matrix.postScale(2f, 2f)
                val resizedBitmap = Bitmap.createBitmap(1200, 1200, Bitmap.Config.ARGB_8888)

                val canvas = Canvas(resizedBitmap)
                canvas.drawBitmap(original, matrix, Paint())
                canvas.save()

                original.recycle()

                it.onNext(resizedBitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }).subscribeOn(Schedulers.computation())
    }


    override fun onNext(t: Pair<Map<String, RadarDetails>, String>) {
        if(mCurrentRadar != mSiteDataProvider.CurrentSelection()) {
            subscribe()
        }
    }
}