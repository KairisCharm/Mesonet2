package org.mesonet.network

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import retrofit2.Retrofit
import okhttp3.ResponseBody
import retrofit2.Converter
import java.lang.reflect.Type


class BitmapConverterFactory : Converter.Factory() {

    override fun responseBodyConverter(type: Type, annotations: Array<Annotation>,
                              retrofit: Retrofit): Converter<ResponseBody, Bitmap>? {
        return if (type === Bitmap::class.java) {
            Converter { value -> BitmapFactory.decodeStream(value.byteStream()) }
        } else {
            retrofit.nextResponseBodyConverter(this, type, annotations)
        }
    }
}