package org.mesonet.dataprocessing.radar

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


        internal fun GetLatitude(): Float {
            return latitude
        }


        internal fun GetLongitude(): Float {
            return longitude
        }


        internal fun GetLatDelta(): Float {
            return latDelta
        }


        internal fun GetLonDelta(): Float {
            return lonDelta
        }


        internal fun GetRange(): Float {
            return range
        }


        internal fun GetName(): String? {
            return name
        }


        class Builder {
            private val mResult = RadarDetailModel()


            internal fun SetValue(inKey: String, inType: String, inValue: String) {
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


            internal fun Build(): RadarDetailModel {
                return mResult
            }
        }
    }
}
