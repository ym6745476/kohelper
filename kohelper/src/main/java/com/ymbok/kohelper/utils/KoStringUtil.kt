package com.ymbok.kohelper.utils

/**
 * 字符串工具类
 * @author ym6745476
 * @since  2022/03/14
 */
object KoStringUtil {

    /**
     * 判断一个字符串是否为null或空值
     * @param str 指定的字符串
     * @return true or false
     */
    fun isEmpty(str: String?): Boolean {
        return str == null || str.trim { it <= ' ' }.isEmpty()
    }

    /**
     * 判断一个字符串是否非空值
     * @param str 指定的字符串
     * @return true or false
     */
    fun isNotEmpty(str: String?): Boolean {
        return str != null && str.trim { it <= ' ' }.isNotEmpty()
    }

    /**
     * 是否只是数字.
     * @param str 指定的字符串
     * @return 是否只是数字:是为true，否则false
     */
    fun isNumber(str: String): Boolean {
        if (isEmpty(str)) {
            return false
        }
        var isNumber = false
        val expr = "^[0-9]+$"
        if (str.matches(Regex(expr))) {
            isNumber = true
        }
        return isNumber
    }
}