package com.ymbok.kohelper.utils
import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue

/**
 * 单位转换工具类。
 * @author ym6745476
 * @since  2021/10/14
 */

fun getDisplayMetrics(context: Context?): DisplayMetrics {
    val mResources: Resources = if (context == null) {
        Resources.getSystem()
    } else {
        context.resources
    }
    //DisplayMetrics{density=1.5, width=480, height=854, scaledDensity=1.5, xdpi=160.421, ydpi=159.497}
    //DisplayMetrics{density=2.0, width=720, height=1280, scaledDensity=2.0, xdpi=160.42105, ydpi=160.15764}
    return mResources.displayMetrics
}

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

fun dip2px(context: Context?, dipValue: Float): Float {
    val displayMetrics = getDisplayMetrics(context)
    return applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, displayMetrics)
}


fun px2dip(context: Context?, pxValue: Float): Float {
    val displayMetrics = getDisplayMetrics(context)
    return pxValue / displayMetrics.density
}

fun sp2px(context: Context?, spValue: Float): Float {
    val displayMetrics = getDisplayMetrics(context)
    return applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, displayMetrics)
}

fun px2sp(context: Context?, pxValue: Float): Float {
    val displayMetrics = getDisplayMetrics(context)
    return pxValue / displayMetrics.scaledDensity
}