package com.ymbok.kohelper.utils
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.Exception

object KoJsonUtil {

    private val gsonBuilder = GsonBuilder()

    /**
     *
     * 将对象转化为json.
     * @param src
     * @return
     */
    fun toJson(src: Any): String? {
        var json: String? = null
        try {
            val gson = gsonBuilder.disableHtmlEscaping().create()
            json = gson.toJson(src)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return json
    }

    /**
     *
     * 将列表转化为json.
     * @param list
     * @return
     */
    fun toJson(list: List<*>): String? {
        var json: String? = null
        try {
            val gson = gsonBuilder.create()
            json = gson.toJson(list)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return json
    }

    /**
     *
     * 将json转化为列表.
     * @param json
     * @param typeToken new TypeToken<ArrayList></ArrayList>>() {};
     * @return
     */
    fun <T> fromJson(json: String, typeToken: TypeToken<T>): T? {
        var obj: T? = null
        try {
            val gson = gsonBuilder.disableHtmlEscaping().create()
            val type = typeToken.type
            obj = gson.fromJson(json, type)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return obj
    }

    /**
     *
     * 将json转化为对象.
     * @param json
     * @param clazz
     * @return
     */
    fun <T> fromJson(json: String, clazz: Class<T>?): T? {
        var obj: T? = null
        try {
            val gson = gsonBuilder.disableHtmlEscaping().create()
            obj = gson.fromJson(json, clazz)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return obj
    }


    /**
     * 设置日期格式
     * @param format
     */
    fun setGsonBuilderDateFormat(format: String) {
        gsonBuilder.setDateFormat(format)
    }
}