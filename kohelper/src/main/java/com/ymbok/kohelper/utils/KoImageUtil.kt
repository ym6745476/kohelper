package com.ymbok.kohelper.utils

import android.content.Context
import android.graphics.*
import android.graphics.Bitmap.CompressFormat
import android.graphics.Shader.TileMode
import android.graphics.drawable.*
import androidx.annotation.ColorInt
import com.ymbok.kohelper.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.lang.Exception
import java.net.URL
import java.net.URLConnection

object KoImageUtil {

    private const val TAG = "KoImageUtil"

    /** 图片处理：裁剪.  */
    const val CUTIMG = 0

    /** 图片处理：缩放.  */
    const val SCALEIMG = 1

    /** 图片处理：不处理.  */
    const val ORIGINALIMG = 2

    /** 图片最大宽度.  */
    const val MAX_WIDTH = 4096 / 2

    /** 图片最大高度.  */
    const val MAX_HEIGHT = 4096 / 2

    /**
     * 获取原图.
     * @param file File对象
     * @return Bitmap 图片
     */
    fun getBitmap(file: File): Bitmap? {
        var resizeBmp: Bitmap? = null
        try {
            resizeBmp = BitmapFactory.decodeFile(file.path)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return resizeBmp
    }

    /**
     * 从互联网上获取原始大小图片.
     * @param url 要下载文件的网络地址
     * @return Bitmap 新图片
     */
    fun getBitmap(url: String?): Bitmap? {
        return getBitmap(url, -1, -1)
    }

    /**
     * 从互联网上获取指定大小的图片.
     * @param url
     * 要下载文件的网络地址
     * @param desiredWidth
     * 新图片的宽
     * @param desiredHeight
     * 新图片的高
     * @return Bitmap 新图片
     */
    private fun getBitmap(url: String?, desiredWidth: Int, desiredHeight: Int): Bitmap? {
        var resizeBmp: Bitmap? = null
        var con: URLConnection? = null
        var inputStream: InputStream? = null
        try {
            val imageURL = URL(url)
            con = imageURL.openConnection()
            con.doInput = true
            con.connect()
            inputStream = con.getInputStream()
            resizeBmp = getBitmap(inputStream, desiredWidth, desiredHeight)
        } catch (e: Exception) {
            e.printStackTrace()
            logD(TAG, "" + e.message)
        } finally {
            try {
                inputStream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return resizeBmp
    }

    /**
     * 从流中获取指定大小的图片（压缩）.
     * @param inputStream
     * @param desiredWidth
     * @param desiredHeight
     * @return
     */
    private fun getBitmap(inputStream: InputStream, desiredWidth: Int, desiredHeight: Int): Bitmap? {
        var resizeBmp: Bitmap? = null
        try {
            if (desiredWidth <= 0 || desiredHeight <= 0) {
                val opts = BitmapFactory.Options()
                // 默认为ARGB_8888.
                opts.inPreferredConfig = Bitmap.Config.RGB_565

                // 以下两个字段需一起使用：
                // 产生的位图将得到像素空间，如果系统gc，那么将被清空。当像素再次被访问，如果Bitmap已经decode，那么将被自动重新解码
                opts.inPurgeable = true
                // 位图可以共享一个参考输入数据(inputstream、阵列等)
                opts.inInputShareable = true
                // 缩放的比例，缩放是很难按准备的比例进行缩放的，通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
                opts.inSampleSize = 1
                // 创建内存
                opts.inJustDecodeBounds = false
                // 使图片不抖动
                opts.inDither = false
                resizeBmp = BitmapFactory.decodeStream(inputStream, null, opts)
            } else {
                val data = stream2Bytes(inputStream)
                resizeBmp = data?.let { getBitmap(it, desiredWidth, desiredHeight) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            logD(TAG, "" + e.message)
        }
        return resizeBmp
    }

    /**
     * 从byte数组获取预期大小的图片（压缩）.
     * @param data
     * @param desiredWidth
     * @param desiredHeight
     * @return
     */
    private fun getBitmap(data: ByteArray, desiredWidth: Int, desiredHeight: Int): Bitmap? {
        var desiredWidth = desiredWidth
        var desiredHeight = desiredHeight
        var resizeBmp: Bitmap? = null
        try {
            val opts = BitmapFactory.Options()
            // 设置为true,decodeFile先不创建内存 只获取一些解码边界信息即图片大小信息
            opts.inJustDecodeBounds = true
            BitmapFactory.decodeByteArray(data, 0, data.size, opts)

            // 获取图片的原始宽度高度
            val srcWidth = opts.outWidth
            val srcHeight = opts.outHeight

            //等比获取最大尺寸
            val size = resizeToMaxSize(srcWidth, srcHeight, desiredWidth, desiredHeight)
            desiredWidth = size[0]
            desiredHeight = size[1]

            // 默认为ARGB_8888.
            opts.inPreferredConfig = Bitmap.Config.RGB_565

            // 以下两个字段需一起使用：
            // 产生的位图将得到像素空间，如果系统gc，那么将被清空。当像素再次被访问，如果Bitmap已经decode，那么将被自动重新解码
            opts.inPurgeable = true
            // 位图可以共享一个参考输入数据(inputstream、阵列等)
            opts.inInputShareable = true
            // 缩放的比例，缩放是很难按准备的比例进行缩放的，通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
            val sampleSize = computeSampleSize(srcWidth, srcHeight, desiredWidth, desiredHeight)
            opts.inSampleSize = sampleSize

            // 创建内存
            opts.inJustDecodeBounds = false
            // 使图片不抖动
            opts.inDither = false
            resizeBmp = BitmapFactory.decodeByteArray(data, 0, data.size, opts)

            // 缩放的比例
            val scale = getMinScale(resizeBmp.width, resizeBmp.height, desiredWidth, desiredHeight)
            if (scale < 1) {
                // 缩小
                resizeBmp = scaleBitmap(resizeBmp, scale)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            logD(TAG, "" + e.message)
        }
        return resizeBmp
    }


    /**
     * 缩放图片.
     * @param file
     * File对象
     * @param desiredWidth
     * 新图片的宽   根据原始图片比例会有不同
     * @param desiredHeight
     * 新图片的高   根据原始图片比例会有不同
     * @return Bitmap 新图片
     */
    fun getScaleBitmap(file: File, desiredWidth: Int, desiredHeight: Int): Bitmap? {
        var desiredWidth = desiredWidth
        var desiredHeight = desiredHeight
        val opts = BitmapFactory.Options()
        val size = getBitmapSize(file)

        // 获取图片的原始宽度高度
        val srcWidth = size[0]
        val srcHeight = size[1]

        //需要的尺寸重置
        val sizeNew = resizeToMaxSize(srcWidth, srcHeight, desiredWidth, desiredHeight)
        desiredWidth = sizeNew[0]
        desiredHeight = sizeNew[1]

        // 默认为ARGB_8888. 必须565  不然有水痕
        opts.inPreferredConfig = Bitmap.Config.RGB_565

        // 以下两个字段需一起使用：
        // 产生的位图将得到像素空间，如果系统gc，那么将被清空。当像素再次被访问，如果Bitmap已经decode，那么将被自动重新解码
        opts.inPurgeable = true
        // 位图可以共享一个参考输入数据(inputstream、阵列等)
        opts.inInputShareable = true
        // 缩放的比例，缩放是很难按准备的比例进行缩放的，通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
        val sampleSize = computeSampleSize(srcWidth, srcHeight, desiredWidth, desiredHeight)
        opts.inSampleSize = sampleSize
        // 创建内存
        opts.inJustDecodeBounds = false
        // 使图片不抖动
        opts.inDither = false
        var resizeBmp = BitmapFactory.decodeFile(file.path, opts)

        // 缩放的比例
        val scale = getMinScale(resizeBmp!!.width, resizeBmp.height, desiredWidth, desiredHeight)
        if (scale < 1) {
            // 缩小
            resizeBmp = scaleBitmap(resizeBmp, scale)
        }
        return resizeBmp
    }

    /**
     * 获取缩略图
     * @param file
     * @param desiredWidth
     * @param desiredHeight
     * @return
     */
    fun getThumbnail(file: File, desiredWidth: Int, desiredHeight: Int): Bitmap? {
        var desiredWidth = desiredWidth
        var desiredHeight = desiredHeight
        val opts = BitmapFactory.Options()
        val size = getBitmapSize(file)

        // 获取图片的原始宽度高度
        val srcWidth = size[0]
        val srcHeight = size[1]

        //需要的尺寸重置
        val sizeNew = resizeToMaxSize(srcWidth, srcHeight, desiredWidth, desiredHeight)
        desiredWidth = sizeNew[0]
        desiredHeight = sizeNew[1]

        // 默认为ARGB_8888. 必须565  不然有水痕
        opts.inPreferredConfig = Bitmap.Config.RGB_565

        // 以下两个字段需一起使用：
        // 产生的位图将得到像素空间，如果系统gc，那么将被清空。当像素再次被访问，如果Bitmap已经decode，那么将被自动重新解码
        opts.inPurgeable = true
        // 位图可以共享一个参考输入数据(inputstream、阵列等)
        opts.inInputShareable = true
        // 缩放的比例，缩放是很难按准备的比例进行缩放的，通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
        //int sampleSize = computeSampleSize(srcWidth,srcHeight,desiredWidth,desiredHeight);
        val wr = srcWidth.toDouble() / desiredWidth
        val hr = srcHeight.toDouble() / desiredHeight
        val ratio = Math.min(wr, hr)
        var ratioMax = 1.0
        var n = 1.0f
        //2的倍数的最大值
        while (true) {
            if (ratioMax * 2 <= ratio) {
                ratioMax *= 2.0
            } else {
                ratioMax *= 2.0
                break
            }
        }
        while (n * 2 <= ratioMax) {
            n *= 2f
        }
        opts.inSampleSize = n.toInt()
        // 创建内存
        opts.inJustDecodeBounds = false
        // 使图片不抖动
        opts.inDither = false
        return BitmapFactory.decodeFile(file.path, opts)
    }

    /**
     * 缩放图片.
     *
     * @param bitmap
     * the bitmap
     * @param desiredWidth
     * 新图片的宽
     * @param desiredHeight
     * 新图片的高
     * @return Bitmap 新图片
     */
    fun getScaleBitmap(bitmap: Bitmap, desiredWidth: Int, desiredHeight: Int): Bitmap? {
        var desiredWidth = desiredWidth
        var desiredHeight = desiredHeight
        if (!checkBitmap(bitmap)) {
            return null
        }

        // 获得图片的宽高
        val srcWidth = bitmap.width
        val srcHeight = bitmap.height
        val size = resizeToMaxSize(srcWidth, srcHeight, desiredWidth, desiredHeight)
        desiredWidth = size[0]
        desiredHeight = size[1]
        var resizeBmp: Bitmap? = bitmap
        val scale = getMinScale(srcWidth, srcHeight, desiredWidth, desiredHeight)
        if (scale < 1) {
            // 缩小
            resizeBmp = scaleBitmap(bitmap, scale)
        }
        return resizeBmp
    }


    /**
     * 裁剪图片，先缩放后裁剪.
     *
     * @param file
     * File对象
     * @param desiredWidth
     * 新图片的宽
     * @param desiredHeight
     * 新图片的高
     * @return Bitmap 新图片
     */
    fun getCutBitmap(file: File, desiredWidth: Int, desiredHeight: Int): Bitmap? {
        var desiredWidth = desiredWidth
        var desiredHeight = desiredHeight
        val opts = BitmapFactory.Options()
        val size = getBitmapSize(file)

        // 获取图片的原始宽度高度
        val srcWidth = size[0]
        val srcHeight = size[1]

        //需要的尺寸重置
        val sizeNew = resizeToMaxSize(srcWidth, srcHeight, desiredWidth, desiredHeight)
        desiredWidth = sizeNew[0]
        desiredHeight = sizeNew[1]

        // 默认为ARGB_8888. 必须565  不然有水痕
        opts.inPreferredConfig = Bitmap.Config.RGB_565

        // 以下两个字段需一起使用：
        // 产生的位图将得到像素空间，如果系统gc，那么将被清空。当像素再次被访问，如果Bitmap已经decode，那么将被自动重新解码
        opts.inPurgeable = true
        // 位图可以共享一个参考输入数据(inputstream、阵列等)
        opts.inInputShareable = true
        // 缩放的比例，缩放是很难按准备的比例进行缩放的，通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
        val sampleSize = computeSampleSize(srcWidth, srcHeight, desiredWidth, desiredHeight)
        opts.inSampleSize = sampleSize
        // 创建内存
        opts.inJustDecodeBounds = false
        // 使图片不抖动
        opts.inDither = false
        var resizeBmp = BitmapFactory.decodeFile(file.path, opts)

        // 缩放的比例,缩小或者放大
        val scale = getMinScale(resizeBmp!!.width, resizeBmp.height, desiredWidth, desiredHeight)
        resizeBmp = scaleBitmap(resizeBmp, scale)

        //超出的裁掉
        if (resizeBmp!!.width > desiredWidth || resizeBmp.height > desiredHeight) {
            resizeBmp = getCutBitmap(resizeBmp, desiredWidth, desiredHeight)
        }
        return resizeBmp
    }

    /**
     * 裁剪图片.
     *
     * @param bitmap
     * the bitmap
     * @param desiredWidth
     * 新图片的宽
     * @param desiredHeight
     * 新图片的高
     * @return Bitmap 新图片
     */
    fun getCutBitmap(bitmap: Bitmap?, desiredWidth: Int, desiredHeight: Int): Bitmap? {
        var desiredWidth = desiredWidth
        var desiredHeight = desiredHeight
        if (!checkBitmap(bitmap)) {
            return null
        }
        if (!checkSize(desiredWidth, desiredHeight)) {
            return null
        }
        var resizeBmp: Bitmap? = null
        try {
            val width = bitmap!!.width
            val height = bitmap.height
            var offsetX = 0
            var offsetY = 0
            if (width > desiredWidth) {
                offsetX = (width - desiredWidth) / 2
            } else {
                desiredWidth = width
            }
            if (height > desiredHeight) {
                offsetY = (height - desiredHeight) / 2
            } else {
                desiredHeight = height
            }
            resizeBmp = Bitmap.createBitmap(bitmap, offsetX, offsetY, desiredWidth, desiredHeight)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (resizeBmp != bitmap) {
                bitmap!!.recycle()
            }
        }
        return resizeBmp
    }

    /**
     * 裁剪图片  从顶部开始
     * @param bitmap
     * @param desiredWidth
     * @param desiredHeight
     * @param left
     * @param top
     * @return
     */
    fun getCutBitmap(bitmap: Bitmap, desiredWidth: Int, desiredHeight: Int, left: Int, top: Int): Bitmap? {
        var desiredWidth = desiredWidth
        var desiredHeight = desiredHeight
        if (!checkBitmap(bitmap)) {
            return null
        }
        if (!checkSize(desiredWidth, desiredHeight)) {
            return null
        }
        var resizeBmp: Bitmap? = null
        try {
            val width = bitmap.width
            val height = bitmap.height
            if (width > desiredWidth) {
            } else {
                desiredWidth = width
            }
            if (height > desiredHeight) {
            } else {
                desiredHeight = height
            }
            resizeBmp = Bitmap.createBitmap(bitmap, left, top, desiredWidth, desiredHeight)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (resizeBmp != bitmap) {
                bitmap.recycle()
            }
        }
        return resizeBmp
    }


    /**
     * 根据等比例缩放图片.
     *
     * @param bitmap
     * the bitmap
     * @param scale
     * 比例
     * @return Bitmap 新图片
     */
    private fun scaleBitmap(bitmap: Bitmap?, scale: Float): Bitmap? {
        return scaleBitmap(bitmap, scale, true)
    }

    /**
     * 根据等比例缩放图片.
     *
     * @param bitmap the bitmap
     * @param scale 比例
     * @param recycled 释放
     * @return Bitmap 新图片
     */
    private fun scaleBitmap(bitmap: Bitmap?, scale: Float, recycled: Boolean): Bitmap? {
        if (!checkBitmap(bitmap)) {
            return null
        }
        if (scale == 1f) {
            return bitmap
        }
        var resizeBmp: Bitmap? = null
        try {
            // 获取Bitmap资源的宽和高
            val bmpW = bitmap!!.width
            val bmpH = bitmap.height

            // 注意这个Matirx是android.graphics底下的那个
            val matrix = Matrix()
            // 设置缩放系数，分别为原来的0.8和0.8
            matrix.postScale(scale, scale)
            resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bmpW, bmpH, matrix, true)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (resizeBmp != bitmap && recycled) {
                bitmap!!.recycle()
            }
        }
        return resizeBmp
    }

    /**
     * 获取图片尺寸
     *
     * @param file File对象
     * @return Bitmap 新图片
     */
    fun getBitmapSize(file: File): IntArray {
        val size = IntArray(2)
        val opts = BitmapFactory.Options()
        // 设置为true,decodeFile先不创建内存 只获取一些解码边界信息即图片大小信息
        opts.inJustDecodeBounds = true
        BitmapFactory.decodeFile(file.path, opts)
        // 获取图片的原始宽度高度
        size[0] = opts.outWidth
        size[1] = opts.outHeight
        return size
    }

    /**
     *
     * 获取缩小的比例.
     * @param srcWidth
     * @param srcHeight
     * @param desiredWidth
     * @param desiredHeight
     * @return
     */
    private fun getMinScale(srcWidth: Int, srcHeight: Int, desiredWidth: Int,
                            desiredHeight: Int): Float {

        // 缩放的比例
        if (desiredWidth <= 0 || desiredHeight <= 0) {
            return 1f
        }
        var scale = 0f
        // 计算缩放比例，宽高的最小比例
        val scaleWidth = desiredWidth.toFloat() / srcWidth
        val scaleHeight = desiredHeight.toFloat() / srcHeight
        scale = if (scaleWidth > scaleHeight) {
            scaleWidth
        } else {
            scaleHeight
        }
        return scale
    }

    private fun resizeToMaxSize(srcWidth: Int, srcHeight: Int,
                                desiredWidth: Int, desiredHeight: Int): IntArray {
        var desiredWidth = desiredWidth
        var desiredHeight = desiredHeight
        val size = IntArray(2)
        if (desiredWidth <= 0) {
            desiredWidth = srcWidth
        }
        if (desiredHeight <= 0) {
            desiredHeight = srcHeight
        }
        if (desiredWidth > MAX_WIDTH) {
            // 重新计算大小
            desiredWidth = MAX_WIDTH
            val scaleWidth = desiredWidth.toFloat() / srcWidth
            desiredHeight = (desiredHeight * scaleWidth).toInt()
        }
        if (desiredHeight > MAX_HEIGHT) {
            // 重新计算大小
            desiredHeight = MAX_HEIGHT
            val scaleHeight = desiredHeight.toFloat() / srcHeight
            desiredWidth = (desiredWidth * scaleHeight).toInt()
        }
        size[0] = desiredWidth
        size[1] = desiredHeight
        return size
    }

    private fun checkBitmap(bitmap: Bitmap?): Boolean {
        if (bitmap == null) {
            logD(TAG, "原图Bitmap为空了")
            return false
        }
        if (bitmap.width <= 0 || bitmap.height <= 0) {
            logD(TAG, "原图Bitmap大小为0")
            return false
        }
        return true
    }

    private fun checkSize(desiredWidth: Int, desiredHeight: Int): Boolean {
        if (desiredWidth <= 0 || desiredHeight <= 0) {
            logD(TAG, "请求Bitmap的宽高参数必须大于0")
            return false
        }
        return true
    }

    /**
     * Drawable转Bitmap.
     * @param drawable 要转化的Drawable
     * @return Bitmap
     */
    fun drawableToBitmap(drawable: Drawable): Bitmap? {
        val bitmap = Bitmap
                .createBitmap(
                        drawable.intrinsicWidth,
                        drawable.intrinsicHeight,
                        if (drawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth,
                drawable.intrinsicHeight)
        drawable.draw(canvas)
        return bitmap
    }

    /**
     * Bitmap对象转换Drawable对象.
     * @param bitmap 要转化的Bitmap对象
     * @return Drawable 转化完成的Drawable对象
     */
    fun bitmapToDrawable(bitmap: Bitmap?): Drawable? {
        var mBitmapDrawable: BitmapDrawable? = null
        try {
            if (bitmap == null) {
                return null
            }
            mBitmapDrawable = BitmapDrawable(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mBitmapDrawable
    }

    /**
     * Bitmap对象转换TransitionDrawable对象.
     *
     * @param bitmap
     * 要转化的Bitmap对象 imageView.setImageDrawable(td);
     * td.startTransition(200);
     * @return Drawable 转化完成的Drawable对象
     */
    fun bitmapToTransitionDrawable(bitmap: Bitmap?): TransitionDrawable? {
        var mBitmapDrawable: TransitionDrawable? = null
        try {
            if (bitmap == null) {
                return null
            }
            mBitmapDrawable = TransitionDrawable(arrayOf(
                    ColorDrawable(Color.TRANSPARENT),
                    BitmapDrawable(bitmap)))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mBitmapDrawable
    }

    /**
     * Drawable对象转换TransitionDrawable对象.
     *
     * @param drawable
     * 要转化的Drawable对象 imageView.setImageDrawable(td);
     * td.startTransition(200);
     * @return Drawable 转化完成的Drawable对象
     */
    fun drawableToTransitionDrawable(drawable: Drawable?): TransitionDrawable? {
        var mBitmapDrawable: TransitionDrawable? = null
        try {
            if (drawable == null) {
                return null
            }
            mBitmapDrawable = TransitionDrawable(arrayOf(
                    ColorDrawable(Color.TRANSPARENT), drawable))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mBitmapDrawable
    }

    /**
     * 将Bitmap转换为byte[].
     *
     * @param bitmap
     * the bitmap
     * @param mCompressFormat
     * 图片格式 Bitmap.CompressFormat.JPEG,CompressFormat.PNG
     * @param needRecycle
     * 是否需要回收
     * @return byte[] 图片的byte[]
     */
    fun bitmap2Bytes(bitmap: Bitmap,
                     mCompressFormat: CompressFormat?, needRecycle: Boolean): ByteArray? {
        var result: ByteArray? = null
        var output: ByteArrayOutputStream? = null
        try {
            output = ByteArrayOutputStream()
            bitmap.compress(mCompressFormat, 100, output)
            result = output.toByteArray()
            if (needRecycle) {
                bitmap.recycle()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (output != null) {
                try {
                    output.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return result
    }

    /**
     * 获取Bitmap大小.
     *
     * @param bitmap
     * the bitmap
     * @param mCompressFormat
     * 图片格式 Bitmap.CompressFormat.JPEG,CompressFormat.PNG
     * @return 图片的大小
     */
    fun getByteCount(bitmap: Bitmap,mCompressFormat: CompressFormat?): Int {
        var size = 0
        var output: ByteArrayOutputStream? = null
        try {
            output = ByteArrayOutputStream()
            bitmap.compress(mCompressFormat, 100, output)
            val result = output.toByteArray()
            size = result.size
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (output != null) {
                try {
                    output.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return size
    }

    /**
     * 将byte[]转换为Bitmap.
     *
     * @param b
     * 图片格式的byte[]数组
     * @return bitmap 得到的Bitmap
     */
    fun bytes2Bimap(b: ByteArray): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            if (b.size != 0) {
                bitmap = BitmapFactory.decodeByteArray(b, 0, b.size)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmap
    }

    /**
     * 旋转Bitmap为一定的角度
     * @param bitmap the bitmap
     * @param degrees the degrees
     * @return the bitmap
     */
    fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap? {
        var mBitmap: Bitmap? = null
        try {
            val m = Matrix()
            m.setRotate(degrees % 360)
            mBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width,
                    bitmap.height, m, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mBitmap
    }

    /**
     * 旋转Bitmap为一定的角度并四周暗化处理.
     * @param bitmap the bitmap
     * @param degrees the degrees
     * @return the bitmap
     */
    fun rotateBitmapTranslate(bitmap: Bitmap, degrees: Float): Bitmap? {
        val mBitmap: Bitmap? = null
        val width: Int
        val height: Int
        try {
            val matrix = Matrix()
            if (degrees / 90 % 2 != 0f) {
                width = bitmap.width
                height = bitmap.height
            } else {
                width = bitmap.height
                height = bitmap.width
            }
            val cx = width / 2
            val cy = height / 2
            matrix.preTranslate(-cx.toFloat(), -cy.toFloat())
            matrix.postRotate(degrees)
            matrix.postTranslate(cx.toFloat(), cy.toFloat())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mBitmap
    }

    /**
     * 转换图片转换成圆形.
     *
     * @param bitmap
     * 传入Bitmap对象
     * @return the bitmap
     */
    fun toRoundBitmap(bitmap: Bitmap?): Bitmap? {
        if (bitmap == null) {
            return null
        }
        var width = bitmap.width
        var height = bitmap.height
        val roundPx: Float
        val left: Float
        val top: Float
        val right: Float
        val bottom: Float
        val dst_left: Float
        val dst_top: Float
        val dst_right: Float
        val dst_bottom: Float
        if (width <= height) {
            roundPx = (width / 2).toFloat()
            top = 0f
            bottom = width.toFloat()
            left = 0f
            right = width.toFloat()
            height = width
            dst_left = 0f
            dst_top = 0f
            dst_right = width.toFloat()
            dst_bottom = width.toFloat()
        } else {
            roundPx = (height / 2).toFloat()
            val clip = ((width - height) / 2).toFloat()
            left = clip
            right = width - clip
            top = 0f
            bottom = height.toFloat()
            width = height
            dst_left = 0f
            dst_top = 0f
            dst_right = height.toFloat()
            dst_bottom = height.toFloat()
        }
        val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val color = -0xbdbdbe
        val paint = Paint()
        val src = Rect(left.toInt(), top.toInt(), right.toInt(),
                bottom.toInt())
        val dst = Rect(dst_left.toInt(), dst_top.toInt(),
                dst_right.toInt(), dst_bottom.toInt())
        val rectF = RectF(dst)
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, src, dst, paint)
        return output
    }

    /**
     * 转换图片转换成圆角
     * @param bitmap
     * @param roundPx
     * @return
     */
    fun toRoundBitmap(bitmap: Bitmap?, roundPx: Int): Bitmap? {
        if (bitmap == null) {
            return null
        }
        var width = bitmap.width
        var height = bitmap.height
        val left: Float
        val top: Float
        val right: Float
        val bottom: Float
        val dst_left: Float
        val dst_top: Float
        val dst_right: Float
        val dst_bottom: Float
        if (width <= height) {
            top = 0f
            bottom = width.toFloat()
            left = 0f
            right = width.toFloat()
            height = width
            dst_left = 0f
            dst_top = 0f
            dst_right = width.toFloat()
            dst_bottom = width.toFloat()
        } else {
            val clip = ((width - height) / 2).toFloat()
            left = clip
            right = width - clip
            top = 0f
            bottom = height.toFloat()
            width = height
            dst_left = 0f
            dst_top = 0f
            dst_right = height.toFloat()
            dst_bottom = height.toFloat()
        }
        val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val color = -0xbdbdbe
        val paint = Paint()
        val src = Rect(left.toInt(), top.toInt(), right.toInt(),bottom.toInt())
        val dst = Rect(dst_left.toInt(), dst_top.toInt(),dst_right.toInt(), dst_bottom.toInt())
        val rectF = RectF(dst)
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawRoundRect(rectF, roundPx.toFloat(), roundPx.toFloat(), paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, src, dst, paint)
        return output
    }

    /**
     * 转换图片转换成镜面效果的图片.
     * @param bitmap 传入Bitmap对象
     * @return the bitmap
     */
    fun toReflectionBitmap(bitmap: Bitmap?): Bitmap? {
        if (bitmap == null) {
            return null
        }
        try {
            val reflectionGap = 1
            val width = bitmap.width
            val height = bitmap.height
            val matrix = Matrix()
            matrix.preScale(1f, -1f)
            val reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2, width, height / 2, matrix, false)
            val bitmapWithReflection = Bitmap.createBitmap(width, height + height / 2, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmapWithReflection)
            canvas.drawBitmap(bitmap, 0f, 0f, null)
            val deafaultPaint = Paint()
            canvas.drawRect(0f, height.toFloat(), width.toFloat(), (height + reflectionGap).toFloat(),
                    deafaultPaint)
            canvas.drawBitmap(reflectionImage, 0f, (height + reflectionGap).toFloat(), null)
            val paint = Paint()
            val shader = LinearGradient(0f, bitmap.height.toFloat(), 0f, bitmapWithReflection.height.toFloat() + reflectionGap.toFloat(), 0x70ffffff, 0x00ffffff, TileMode.CLAMP)
            paint.shader = shader
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
            canvas.drawRect(0f, height.toFloat(), width.toFloat(), (bitmapWithReflection.height
                    + reflectionGap).toFloat(), paint)
            return bitmapWithReflection
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 释放Bitmap对象
     * @param bitmap
     * 要释放的Bitmap
     */
    fun releaseBitmap(bitmap: Bitmap?) {
        var bitmap = bitmap
        if (bitmap != null) {
            try {
                if (!bitmap.isRecycled) {
                    bitmap.recycle()
                }
            } catch (e: Exception) {
            }
            bitmap = null
        }
    }

    /**
     * 释放Bitmap数组
     * @param bitmaps
     * 要释放的Bitmap数组
     */
    fun releaseBitmapArray(bitmaps: Array<Bitmap?>?) {
        if (bitmaps != null) {
            try {
                for (bitmap in bitmaps) {
                    if (bitmap != null && !bitmap.isRecycled) {
                        bitmap.recycle()
                    }
                }
            } catch (e: Exception) {
            }
        }
    }

    /**
     * 释放Bitmap数组
     * @param bitmaps 要释放的Bitmap列表
     */
    fun releaseBitmapList(bitmaps: List<Bitmap?>?) {
        if (bitmaps != null) {
            try {
                for (bitmap in bitmaps) {
                    if (bitmap != null && !bitmap.isRecycled) {
                        bitmap.recycle()
                    }
                }
            } catch (e: Exception) {
            }
        }
    }

    /**
     * 找到最合适的SampleSize
     * @param width
     * @param height
     * @param desiredWidth
     * @param desiredHeight
     * @return
     */
    private fun computeSampleSize(width: Int, height: Int, desiredWidth: Int, desiredHeight: Int): Int {
        val wr = width.toDouble() / desiredWidth
        val hr = height.toDouble() / desiredHeight
        val ratio = Math.min(wr, hr)
        var n = 1.0f
        while (n * 2 <= ratio) {
            n *= 2f
        }
        return n.toInt()
    }

    /**
     * 9图片添加颜色
     * @param context
     * @param tintColor
     * @return
     */
    fun tint9PatchDrawableFrame(context: Context, @ColorInt tintColor: Int): Drawable? {
        val ninePatchDrawable = context.resources.getDrawable(R.mipmap.ko_bg_toast,null) as NinePatchDrawable
        ninePatchDrawable.colorFilter = PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN)
        return ninePatchDrawable
    }

    /**
     * 从流中读取数据到byte[]
     * @param inStream the in stream
     * @return the byte[]
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private fun stream2Bytes(inStream: InputStream): ByteArray? {
        val buff = ByteArray(1024)
        var data: ByteArray? = null
        try {
            val swapStream = ByteArrayOutputStream()
            var read = 0
            while (inStream.read(buff, 0, 100).also { read = it } > 0) {
                swapStream.write(buff, 0, read)
            }
            data = swapStream.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return data
    }
}