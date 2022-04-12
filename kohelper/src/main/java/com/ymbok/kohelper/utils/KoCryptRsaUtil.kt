package com.ymbok.kohelper.utils

import android.util.Base64
import java.io.ByteArrayOutputStream
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


object KoCryptRsaUtil {

    const val transformation = "RSA/NONE/PKCS1Padding"
    const val ENCRYPT_MAX_SIZE = 117
    const val DECRYPT_MAX_SIZE = 128

    /**
     * 公钥加密
     */
    private fun encryptByPublicKey(str: String, publicKey: PublicKey): String {
        val byteArray = str.toByteArray()
        val cipher = Cipher.getInstance(transformation)
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)

        var temp: ByteArray?
        var offset = 0

        val outputStream = ByteArrayOutputStream()

        while (byteArray.size - offset > 0) {
            if (byteArray.size - offset >= ENCRYPT_MAX_SIZE) {
                temp = cipher.doFinal(byteArray, offset, ENCRYPT_MAX_SIZE)
                offset += ENCRYPT_MAX_SIZE
            } else {
                temp = cipher.doFinal(byteArray, offset, (byteArray.size - offset))
                offset = byteArray.size
            }
            outputStream.write(temp)
        }

        outputStream.close()
        return Base64.encodeToString(outputStream.toByteArray(),Base64.DEFAULT)
    }

    /**
     * 私钥解密
     */
    private fun decryptByPrivateKey(str: String, privateKey: PrivateKey): String {
        val byteArray = Base64.decode(str,Base64.DEFAULT)
        val cipher = Cipher.getInstance(transformation)
        cipher.init(Cipher.DECRYPT_MODE, privateKey)

        //定义缓冲区
        var temp: ByteArray?
        //当前偏移量
        var offset = 0

        val outputStream = ByteArrayOutputStream()

        while (byteArray.size - offset > 0) {
            //剩余的部分大于最大解密字段，则加密限制的最大长度
            if (byteArray.size - offset >= DECRYPT_MAX_SIZE) {
                temp = cipher.doFinal(byteArray, offset, DECRYPT_MAX_SIZE)
                //偏移量增加128
                offset += DECRYPT_MAX_SIZE
            } else {
                //如果剩余的字节数小于最大长度，则解密剩余的全部
                temp = cipher.doFinal(byteArray, offset, (byteArray.size - offset))
                offset = byteArray.size
            }
            outputStream.write(temp)
        }
        outputStream.close()
        return String(outputStream.toByteArray())
    }

    /**
     * 公钥加密
     */
    fun encryptByPublicKeyString(str: String, publicKey: String): String {
        val keySpec = X509EncodedKeySpec(Base64.decode(publicKey,Base64.DEFAULT))
        val kf = KeyFactory.getInstance("RSA")
        val keyPublic = kf.generatePublic(keySpec)
        return encryptByPublicKey(str, keyPublic)
    }


    /**
     * 私钥解密
     */
    fun decryptByPrivateKeyString(str: String, privateKey: String): String {
        val keySpec = PKCS8EncodedKeySpec(Base64.decode(privateKey,Base64.DEFAULT))
        val kf = KeyFactory.getInstance("RSA")
        val keyPrivate = kf.generatePrivate(keySpec)
        return decryptByPrivateKey(str, keyPrivate)
    }

    /**
     * HmacSHA1加密
     */
    fun hmacSha1(str:String, key:String):String {
        val type = "HmacSHA1"
        val secret = SecretKeySpec(key.toByteArray(), "type")
        val mac = Mac.getInstance(type)
        mac.init(secret)
        val digest = mac.doFinal(str.toByteArray())
        return KoStringUtil.bytesToHexString(digest).uppercase()
    }

}

fun main(args: Array<String>) {

}
