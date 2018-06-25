package org.mesonet.models.radar

import java.lang.reflect.Field



class RadarModel : RadarDetails {

    private val name: String? = null
    private val latitude: Float? = null
    private val longitude: Float? = null
    private val range: Float? = null


    override fun GetLatitude(): Float? {
        return latitude
    }


    override fun GetLongitude(): Float? {
        return longitude
    }


    override fun GetRange(): Float? {
        return range
    }


    override fun GetName(): String? {
        return name
    }


    class Builder {
        private val mResult = RadarModel()


        internal fun SetValue(inKey: String, inType: String, inValue: String) {
            val field = FindField(inKey)

            if (field != null) {
                try {
                    field.isAccessible = true
                    if (inType.equals("string") && field.type == String::class.java)
                        field.set(mResult, inValue)
                    else if (inType.equals("real") && (field.type == Float::class.javaPrimitiveType || field.type == Float::class.javaObjectType))
                        field.set(mResult, java.lang.Float.parseFloat(inValue))
                } catch (e: IllegalAccessException ) {
                    e.printStackTrace()
                }
                catch (e: NumberFormatException)
                {
                    e.printStackTrace()
                }

                field.isAccessible = false
            }
        }


        internal fun FindField(inName: String): Field? {
            val fields = RadarModel::class.java.declaredFields

            if (fields != null) {
                for (i in fields.indices) {
                    if (fields[i].name == inName)
                        return fields[i]
                }
            }

            return null
        }


        internal fun Build(): RadarModel {
            return mResult
        }
    }
}
