package com.ymbok.kohelper.utils
import android.content.Context
import android.widget.Toast

/**
 * Toast工具类
 * @author ym6745476
 * @since  2022/03/14
 */

fun showToast(context: Context, message: String) {
    Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
}
