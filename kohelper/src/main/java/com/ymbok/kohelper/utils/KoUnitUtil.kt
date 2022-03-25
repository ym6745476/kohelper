package com.ymbok.kohelper.utils
import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue

/**
 * 单位转换工具类
 * @author ym6745476
 * @since  2022/03/14
 */
object KoUnitUtil {

    fun applyDimension(unit: Int, value: Float,
                       metrics: DisplayMetrics): Float {
        when (unit) {
            TypedValue.COMPLEX_UNIT_PX -> return value
            TypedValue.COMPLEX_UNIT_DIP -> return value * metrics.density
            TypedValue.COMPLEX_UNIT_SP -> return value * metrics.scaledDensity
            TypedValue.COMPLEX_UNIT_PT -> return value * metrics.xdpi * (1.0f / 72)
            TypedValue.COMPLEX_UNIT_IN -> return value * metrics.xdpi
            TypedValue.COMPLEX_UNIT_MM -> return value * metrics.xdpi * (1.0f / 25.4f)
        }
        return 0f
    }

    fun dip2px(context: Context, dipValue: Float): Float {
        val displayMetrics = KoAppUtil.getDisplayMetrics(context)
        return applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, displayMetrics)
    }


    fun px2dip(context: Context, pxValue: Float): Float {
        val displayMetrics = KoAppUtil.getDisplayMetrics(context)
        return pxValue / displayMetrics.density
    }

    fun sp2px(context: Context, spValue: Float): Float {
        val displayMetrics = KoAppUtil.getDisplayMetrics(context)
        return applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, displayMetrics)
    }

    fun px2sp(context: Context, pxValue: Float): Float {
        val displayMetrics = KoAppUtil.getDisplayMetrics(context)
        return pxValue / displayMetrics.scaledDensity
    }
}