package org.mesonet.dataprocessing.radar


import android.content.Context

import java.lang.reflect.Field
import java.util.ArrayList

class RadarImageModel {
    private val filename: String? = null
    private val timestring: String? = null
    private val datatime: Long = 0
    private val looptime: Long = 0

    private var mRadarDataController: RadarDataController? = null

    private val mImageLoadedListeners = ArrayList<RadarDataController.ImageLoadedListener>()


    internal fun GetImage(inContext: Context, inImageLoadedListener: RadarDataController.ImageLoadedListener) {
        mRadarDataController!!.GetImage(inContext, "https://www.mesonet.org" + filename!!, inImageLoadedListener)
    }


    internal fun GetTimeString(): String? {
        return timestring
    }


    class Builder(inRadarDataController: RadarDataController) {
        private val mResult = RadarImageModel()


        init {
            mResult.mRadarDataController = inRadarDataController
        }


        internal fun SetValue(inKey: String, inValue: String) {
            val field = FindField(inKey)

            if (field != null) {
                try {
                    field.isAccessible = true
                    if (field.type == String::class.java || field.type == String::class)
                        field.set(mResult, inValue)
                    else if (field.type == Long::class.javaPrimitiveType || field.type == Long::class)
                        field.set(mResult, java.lang.Long.parseLong(inValue))
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }

                field.isAccessible = false
            }
        }


        private fun FindField(inName: String): Field? {
            val fields = RadarImageModel::class.java.declaredFields

            if (fields != null) {
                for (i in fields.indices) {
                    if (fields[i].name == inName)
                        return fields[i]
                }
            }

            return null
        }


        internal fun Build(): RadarImageModel {
            return mResult
        }
    }
}
