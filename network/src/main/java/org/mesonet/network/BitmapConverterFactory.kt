package org.mesonet.network

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import retrofit2.Retrofit
import okhttp3.ResponseBody
import retrofit2.Converter
import java.io.ByteArrayOutputStream
import java.lang.reflect.Type


class BitmapConverterFactory : Converter.Factory() {

    override fun responseBodyConverter(type: Type, annotations: Array<Annotation>,
                              retrofit: Retrofit): Converter<ResponseBody, Bitmap>? {
        return if (type === Bitmap::class.java) {
            Converter { value ->
                val original = BitmapFactory.decodeStream(value.byteStream())
                val stream = ByteArrayOutputStream()
                original.compress(Bitmap.CompressFormat.PNG, 100, stream)

                BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size())
            }
        } else {
            retrofit.nextResponseBodyConverter(this, type, annotations)
        }
    }
}