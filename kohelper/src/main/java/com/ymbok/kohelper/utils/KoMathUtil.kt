package com.ymbok.kohelper.utils

import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.*
import kotlin.experimental.and
import kotlin.math.*

/**
 * 数字处理类
 */
object KoMathUtil {

    /**
     * 四舍五入
     * @param number  原数
     * @param decimal 保留几位小数
     * @return 四舍五入后的值
     */
    fun round(number: Double, decimal: Int): Double {
        return BigDecimal(number).setScale(decimal, BigDecimal.ROUND_HALF_UP).toDouble()
    }

    /**
     * 四舍五入
     * @param number  原数
     * @param decimal 保留几位小数
     * @return 四舍五入后的值
     */
    fun roundString(number: Double, decimal: Int): String {
        return BigDecimal(number).setScale(decimal, BigDecimal.ROUND_HALF_UP).toString()
    }

    /**
     * 四舍五入
     * @param number  原数
     * @param decimal 保留几位小数
     * @return 四舍五入后的值
     */
    fun roundInteger(number: Double, decimal: Int): Double {
        return BigDecimal(number).setScale(decimal, BigDecimal.ROUND_HALF_UP).toDouble()
    }

    /**
     * 截取2位小数
     * @param number 原数
     * @return 四舍五入后的值
     */
    fun formatTwo(number: String): String {
        return String.format("%.2f", number.toDouble())
    }

    /**
     * 截取2位小数
     * @param number 原数
     * @return 四舍五入后的值
     */
    fun formatTwo(number: Double): String {
        return String.format("%.2f", number)
    }

    /**
     * 把.0去掉
     * @param number 原数
     * @return
     */
    fun formatZero(number: Double): String? {
        val decimalFormat = DecimalFormat("###################.###########")
        return decimalFormat.format(number)
    }

    /**
     * 每3位加逗号
     * @param number
     * @return
     */
    fun format3Number(number: Float): String {
        val df = DecimalFormat("#,###.00")
        return df.format(number.toDouble())
    }

    /**
     * 每3位加逗号
     * @param number
     * @return
     */
    fun format3Number(number: Double): String {
        val df = DecimalFormat("#,###")
        return df.format(number)
    }

    /**
     * 每3位加逗号
     * @param number
     * @return
     */
    fun format3NumberTwo(number: Double): String? {
        val df = DecimalFormat("#,###.00")
        return df.format(number)
    }

    /**
     * 字节数组转换成16进制串.
     * @param b      the b
     * @param length the length
     * @return the string
     */
    fun byte2HexStr(b: ByteArray, length: Int): String {
        var hs = ""
        var stmp = ""
        for (n in 0 until length) {
            stmp = Integer.toHexString(b[n].and(0xFF.toByte()).toInt())
            hs = if (stmp.length == 1) hs + "0" + stmp else {
                hs + stmp
            }
            hs = "$hs,"
        }
        return hs.uppercase(Locale.getDefault())
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

    /**
     * 计算数组的平均值.
     * @param pixels 数组
     * @return int 平均值
     */
    fun average(pixels: IntArray): Int {
        var m = 0f
        for (i in pixels.indices) {
            m += pixels[i]
        }
        m /= pixels.size
        return m.toInt()
    }

    /**
     * 计算数组的平均值.
     * @param pixels 数组
     * @return int 平均值
     */
    fun average(pixels: DoubleArray): Int {
        var m = 0f
        for (i in pixels.indices) {
            m += pixels[i].toFloat()
        }
        m /= pixels.size
        return m.toInt()
    }

    /**
     * 计算对数
     * @param value value的对数
     * @param base  以base为底
     * @return
     */
    fun log(value: Double, base: Double): Double {
        return ln(value) / ln(base)
    }


    fun rad(d: Double): Double {
        return d * Math.PI / 180.0
    }

    /**
     * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米.
     * @param longitude1
     * @param latitude1
     * @param longitude2
     * @param latitude2
     * @return
     */
    fun getGeoDistance(longitude1: Double, latitude1: Double, longitude2: Double, latitude2: Double): Double {
        val radius = 6378137.0
        val radLat1 = rad(latitude1)
        val radLat2 = rad(latitude2)
        val a = radLat1 - radLat2
        val b = rad(longitude1) - rad(longitude2)
        var s = 2 * asin(sqrt(sin(a / 2).pow(2.0) + cos(radLat1) * cos(radLat2) * Math.sin(b / 2).pow(2.0)))
        s *= radius
        s = ((s * 10000).roundToInt() / 10000).toDouble()
        return s
    }

    /**
     * 去掉最大和最小  再求平均值
     * @param values
     * @return
     */
    fun averageOptimization(values: DoubleArray): Double {
        var tem: Double
        var sum = 0.0
        for (i in 0 until values.size - 1) {
            for (j in 0 until values.size - 1) {
                if (values[j] < values[j + 1]) {
                    tem = values[j]
                    values[j] = values[j + 1]
                    values[j + 1] = tem
                }
            }
        }
        for (i in values.indices) {
            if (i != 0 && i != values.size - 1) {
                sum = values[i]
            }
        }
        return sum / (values.size - 2)
    }


    @JvmStatic
    fun main(args: Array<String>) {
        //System.out.println(formatTwo("181895.515"));
        println(format3Number(KoMathUtil.roundInteger(2154807067.520000, 0)))
    }
}