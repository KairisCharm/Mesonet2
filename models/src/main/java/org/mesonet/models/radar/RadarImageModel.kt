package org.mesonet.models.radar


import java.lang.reflect.Field

class RadarImageModel : RadarImageInfo {
    private val filename: String = ""
    private val timestring: String = ""



    override fun GetTimeString(): String {
        return timestring
    }


    override fun GetFilename(): String {
        return filename
    }


    class Builder {
        private val mResult = RadarImageModel()


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


        internal fun FindField(inName: String): Field? {
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
