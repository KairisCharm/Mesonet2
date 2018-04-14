package org.mesonet.app.radar

import java.lang.reflect.Field
import java.util.HashMap


class RadarModel {
    private val mRadarDetailMap = HashMap<String, RadarDetailModel>()


    class RadarDetailModel {
        private val state: String? = null
        private val name: String? = null
        private val latitude: Float = 0.toFloat()
        private val longitude: Float = 0.toFloat()
        private val latDelta: Float = 0.toFloat()
        private val lonDelta: Float = 0.toFloat()
        private val range: Float = 0.toFloat()


        fun GetLatitude(): Float {
            return latitude
        }


        fun GetLongitude(): Float {
            return longitude
        }


        fun GetLatDelta(): Float {
            return latDelta
        }


        fun GetLonDelta(): Float {
            return lonDelta
        }


        fun GetRange(): Float {
            return range
        }


        fun GetName(): String? {
            return name
        }


        class Builder {
            private val mResult = RadarDetailModel()


            fun SetValue(inKey: String, inType: String, inValue: String) {
                val field = FindField(inKey)

                if (field != null) {
                    try {
                        field.isAccessible = true
                        if (inType == "string" && field.type == String::class.java)
                            field.set(mResult, inValue)
                        else if (inType == "real" && field.type == Float::class.javaPrimitiveType)
                            field.set(mResult, java.lang.Float.parseFloat(inValue))
                    } catch (e: IllegalAccessException) {
                        e.printStackTrace()
                    }

                    field.isAccessible = false
                }
            }


            private fun FindField(inName: String): Field? {
                val fields = RadarDetailModel::class.java.declaredFields

                if (fields != null) {
                    for (i in fields.indices) {
                        if (fields[i].name == inName)
                            return fields[i]
                    }
                }

                return null
            }


            fun Build(): RadarDetailModel {
                return mResult
            }
        }
    }
}
