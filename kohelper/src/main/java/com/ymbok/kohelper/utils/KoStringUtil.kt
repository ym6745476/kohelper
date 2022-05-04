package com.ymbok.kohelper.utils

import java.util.regex.Pattern

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

    /**
     * 手机号格式验证.
     * @param str 指定的手机号码字符串
     * @return 是否为手机号码格式:是为true，否则false
     */
    fun isMobile(str: String): Boolean {
        var isMobile = false
        try {
            val p = Pattern.compile("^1[0-9]{10}$")
            val m = p.matcher(str)
            isMobile = m.matches()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return isMobile
    }

    /**
     * 是否包含字母和数字.
     * @param str 指定的字符串
     * @return 是否只是字母和数字:是为true，否则false
     */
    fun isNumberAndLetter(str: String): Boolean {
        if (isEmpty(str)) {
            return false
        }
        var isNoLetter = false
        val p = Pattern.compile("^[a-z0-9A-Z]+\$")
        val m = p.matcher(str)
        isNoLetter = m.matches()
        return isNoLetter
    }

    fun formatStarName(str: String): String {
        return if (str.length == 2) {
            "*" + str.substring(1, 2)
        } else if (str.length == 3) {
            str.substring(0, 1) + "*" + str.substring(2)
        } else if (str.length == 4) {
            str.substring(0, 2) + "*" + str.substring(3)
        } else if (str.length > 4) {
            str.substring(0, 2) + "**" + str.substring(4)
        } else {
            str
        }
    }

    fun formatStarMobile(str: String): String {
        return if (str.length == 11) {
           str.substring(0, 3) +  "****" + str.substring(7, 11)
        }else {
            str
        }
    }

    /**
     * 不足2个字符的在前面补“0”.
     * @param str 指定的字符串
     * @return 至少2个字符的字符串
     */
    fun formatNumberZero(str: String,size:Int): String {
        var str = str
        try {
            if (str.length < size) {
                for (i in str.length until size) {
                    str = "0$str"
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return str
    }


    /**
     * 字节数组转16进制字符串
     */
    fun bytesToHexString(src: ByteArray): String {
        val stringBuilder = StringBuilder("")
        for (element in src) {
            val v = element.toInt() and 0xFF
            val hv = Integer.toHexString(v)
            if (hv.length < 2) {
                stringBuilder.append(0)
            }
            stringBuilder.append(hv)
        }
        return stringBuilder.toString()
    }

    /**
     * 二进制转为十六进制.
     * @param binary the binary
     * @return char hex
     */
    fun binaryToHex(binary: Int): Char {
        var ch = ' '
        ch = when (binary) {
            0 -> '0'
            1 -> '1'
            2 -> '2'
            3 -> '3'
            4 -> '4'
            5 -> '5'
            6 -> '6'
            7 -> '7'
            8 -> '8'
            9 -> '9'
            10 -> 'a'
            11 -> 'b'
            12 -> 'c'
            13 -> 'd'
            14 -> 'e'
            15 -> 'f'
            else -> ' '
        }
        return ch
    }

    /**
     * 一维数组转为二维数组
     * @param m      the m
     * @param width  the width
     * @param height the height
     * @return the int[][]
     */
    fun arrayToMatrix(m: IntArray, width: Int, height: Int): Array<IntArray> {
        val result = Array(height) { IntArray(width) }
        for (i in 0 until height) {
            for (j in 0 until width) {
                val p = j * height + i
                result[i][j] = m[p]
            }
        }
        return result
    }

    /**
     * 二维数组转为一维数组
     * @param m the m
     * @return the double[]
     */
    fun matrixToArray(m: Array<DoubleArray>): DoubleArray {
        val p: Int = m.size * m[0].size
        val result = DoubleArray(p)
        for (i in m.indices) {
            for (j in 0 until m[i].size) {
                val q = j * m.size + i
                result[q] = m[i][j]
            }
        }
        return result
    }

    /**
     * int数组转换为double数组.
     * @param input the input
     * @return the double[]
     */
    fun intToDoubleArray(input: IntArray): DoubleArray {
        val length = input.size
        val output = DoubleArray(length)
        for (i in 0 until length) {
            output[i] = java.lang.Double.valueOf(input[i].toString())
        }
        return output
    }

    /**
     * int二维数组转换为double二维数组.
     * @param input the input
     * @return the double[][]
     */
    fun intToDoubleMatrix(input: Array<IntArray>): Array<DoubleArray> {
        val height = input.size
        val width: Int = input[0].size
        val output = Array(height) { DoubleArray(width) }
        for (i in 0 until height) {
            // 列
            for (j in 0 until width) {
                // 行
                output[i][j] = java.lang.Double.valueOf(input[i][j].toString())
            }
        }
        return output
    }

}