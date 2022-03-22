package com.ymbok.kohelper.utils

import android.content.Context
import android.os.Environment

object KoFileUtil {

    fun getExternalCacheDir(context: Context): String? {
        var cachePath:String?
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageRemovable()) {
            cachePath = context.externalCacheDir?.path
        } else {
            cachePath = context.cacheDir.path
        }
        return cachePath
    }

    /**
     * 获取大小的描述.
     * @param size 字节个数B
     * @return  大小的描述
     */
    fun getSizeDesc(size: Long): String {
        var sizeF = 0.0
        var suffix = "B"
        if (size >= 1000) {
            suffix = "K"
            sizeF = size / 1000.0
            if (sizeF >= 1024) {
                suffix = "M"
                sizeF /= 1024f
                if (sizeF >= 1024) {
                    suffix = "G"
                    sizeF /= 1024f
                }
            }
        }
        return KoMathUtil.round(sizeF, 2).toString() + suffix
    }
}