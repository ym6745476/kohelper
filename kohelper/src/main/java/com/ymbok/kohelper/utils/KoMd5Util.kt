package com.ymbok.kohelper.utils
import java.security.MessageDigest

/**
 * MD5工具类
 * @author ym6745476
 * @since  2022/03/14
 */
object KoMd5Util {

    /** md5加密 */
    fun md5(text: String): String {
        val hash = MessageDigest.getInstance("MD5").digest(text.toByteArray())
        val hex = StringBuilder(hash.size * 2)
        for (b in hash) {
            var str = Integer.toHexString(b.toInt())
            if (b < 0x10) {
                str = "0$str"
            }
            hex.append(str.substring(str.length -2))
        }
        return hex.toString()
    }
}