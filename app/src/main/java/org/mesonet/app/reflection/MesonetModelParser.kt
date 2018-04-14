package org.mesonet.app.reflection


import java.lang.reflect.Field

import javax.inject.Inject
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.full.withNullability
import kotlin.reflect.jvm.isAccessible

class MesonetModelParser @Inject
internal constructor() {


    fun <T : Any> Parse(inModelClass: KClass<T>?, inStrValues: String?): T? {
        if (inModelClass == null)
            return null

        var model: T? = null
        try {
            model = inModelClass.primaryConstructor?.call()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        }

        if (model == null || inStrValues == null)
            return model

        val strFields = inStrValues.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val fields = ArrayList(inModelClass.declaredMemberProperties)

        for (i in strFields.indices) {
            val strField = strFields[i].split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            if (strField.size != 2)
                continue

            val fieldName = strField[0]
            val fieldValue = strField[1]

            for (j in fields.indices) {
                if (fields[j].name == fieldName) {
                    fields[j].isAccessible = true
                    if (fields[j].returnType == Byte::class.javaPrimitiveType ||
                        fields[j].returnType == Byte::class.javaObjectType ||
                        fields[j].returnType == Byte::class ||
                        fields[j].returnType == Byte::class.starProjectedType.withNullability(true))
                    {
                        try {
                            (fields[j] as KMutableProperty<*>).setter.call(model, java.lang.Byte.parseByte(fieldValue))
                        } catch (e: IllegalAccessException) {
                            e.printStackTrace()
                        } catch (e: IllegalArgumentException) {
                            e.printStackTrace()
                        }

                    }
                    if (fields[j].returnType == Short::class.javaPrimitiveType ||
                            fields[j].returnType == Short::class.javaObjectType ||
                            fields[j].returnType == Short::class ||
                            fields[j].returnType == Short::class.starProjectedType.withNullability(true)) {
                        try {
                            (fields[j] as KMutableProperty<*>).setter.call(model, java.lang.Short.parseShort(fieldValue))
                        } catch (e: IllegalAccessException) {
                            e.printStackTrace()
                        } catch (e: IllegalArgumentException) {
                            e.printStackTrace()
                        }

                    }
                    if (fields[j].returnType == Int::class.javaPrimitiveType ||
                            fields[j].returnType == Int::class.javaObjectType ||
                            fields[j].returnType == Int::class ||
                            fields[j].returnType == Int::class.starProjectedType.withNullability(true)) {
                        try {
                            (fields[j] as KMutableProperty<*>).setter.call(model, Integer.parseInt(fieldValue))
                        } catch (e: IllegalAccessException) {
                            e.printStackTrace()
                        } catch (e: IllegalArgumentException) {
                            e.printStackTrace()
                        }

                    }
                    if (fields[j].returnType == Long::class.javaPrimitiveType ||
                            fields[j].returnType == Long::class.javaObjectType ||
                            fields[j].returnType == Long::class ||
                            fields[j].returnType == Long::class.starProjectedType.withNullability(true)) {
                        try {
                            (fields[j] as KMutableProperty<*>).setter.call(model, java.lang.Long.parseLong(fieldValue))
                        } catch (e: IllegalAccessException) {
                            e.printStackTrace()
                        } catch (e: IllegalArgumentException) {
                            e.printStackTrace()
                        }

                    }
                    if (fields[j].returnType == Float::class.javaPrimitiveType ||
                            fields[j].returnType == Float::class.javaObjectType ||
                            fields[j].returnType == Float::class ||
                            fields[j].returnType == Float::class.starProjectedType.withNullability(true)) {
                        try {
                            (fields[j] as KMutableProperty<*>).setter.call(model, java.lang.Float.parseFloat(fieldValue))
                        } catch (e: IllegalAccessException) {
                            e.printStackTrace()
                        } catch (e: IllegalArgumentException) {
                            e.printStackTrace()
                        }

                    }
                    if (fields[j].returnType == Double::class.javaPrimitiveType ||
                            fields[j].returnType == Double::class.javaObjectType ||
                            fields[j].returnType == Double::class ||
                            fields[j].returnType == Double::class.starProjectedType.withNullability(true)) {
                        try {
                            (fields[j] as KMutableProperty<*>).setter.call(model, java.lang.Double.parseDouble(fieldValue))
                        } catch (e: IllegalAccessException) {
                            e.printStackTrace()
                        } catch (e: IllegalArgumentException) {
                            e.printStackTrace()
                        }

                    }
                    if (fields[j].returnType == Char::class.javaPrimitiveType ||
                            fields[j].returnType == Char::class.javaObjectType ||
                            fields[j].returnType == Char::class ||
                            fields[j].returnType == Char::class.starProjectedType.withNullability(true)) {
                        try {
                            (fields[j] as KMutableProperty<*>).setter.call(model, fieldValue[0])
                        } catch (e: IllegalAccessException) {
                            e.printStackTrace()
                        } catch (e: IllegalArgumentException) {
                            e.printStackTrace()
                        }

                    }
                    if (fields[j].returnType == Boolean::class.javaPrimitiveType ||
                            fields[j].returnType == Boolean::class.javaObjectType ||
                            fields[j].returnType == Boolean::class ||
                            fields[j].returnType == Boolean::class.starProjectedType.withNullability(true)) {
                        try {
                            (fields[j] as KMutableProperty<*>).setter.call(model, java.lang.Boolean.parseBoolean(fieldValue))
                        } catch (e: IllegalAccessException) {
                            e.printStackTrace()
                        } catch (e: IllegalArgumentException) {
                            e.printStackTrace()
                        }

                    }
                    if (fields[j].returnType == Number::class.javaPrimitiveType ||
                            fields[j].returnType == Number::class.javaObjectType ||
                            fields[j].returnType == Number::class ||
                            fields[j].returnType == Number::class.starProjectedType.withNullability(true)) {
                        try {
                            (fields[j] as KMutableProperty<*>).setter.call(model, java.lang.Double.parseDouble(fieldValue))
                        } catch (e: IllegalAccessException) {
                            e.printStackTrace()
                        } catch (e: IllegalArgumentException) {
                            e.printStackTrace()
                        }

                    }
                    if (fields[j].returnType == String::class.javaPrimitiveType ||
                            fields[j].returnType == String::class.javaObjectType ||
                            fields[j].returnType == String::class ||
                            fields[j].returnType == String::class.starProjectedType.withNullability(true)) {
                        try {
                            (fields[j] as KMutableProperty<*>).setter.call(model, fieldValue)
                        } catch (e: IllegalAccessException) {
                            e.printStackTrace()
                        } catch (e: IllegalArgumentException) {
                            e.printStackTrace()
                        }

                    }

                    fields[i].isAccessible = false
                }
            }
        }

        return model
    }
}
