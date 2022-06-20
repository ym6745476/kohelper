package com.ymbok.kohelper.utils
import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.os.Process
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import java.io.File
import java.lang.Exception

object KoAppUtil {

    /**
     * 获取进程名
     */
    fun getProcessName(context: Context?): String? {
        if (context == null) return null
        val manager = context.getSystemService(Application.ACTIVITY_SERVICE) as ActivityManager
        for (processInfo in manager.runningAppProcesses) {
            if (processInfo.pid == Process.myPid()) {
                return processInfo.processName
            }
        }
        return null
    }

    /**
     * 获取包信息
     * @param context the context
     */
    fun getPackageInfo(context: Context): PackageInfo? {
        var info: PackageInfo? = null
        try {
            info = context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_ACTIVITIES)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return info
    }

    /**
     * 获取屏幕尺寸与密度
     * @param context the context
     * @return mDisplayMetrics
     */
    fun getDisplayMetrics(context: Context?): DisplayMetrics {
        var resources = if (context == null) {
            Resources.getSystem()
        } else {
            context.resources
        }
        //DisplayMetrics{density=1.5, width=480, height=854, scaledDensity=1.5, xdpi=160.421, ydpi=159.497}
        //DisplayMetrics{density=2.0, width=720, height=1280, scaledDensity=2.0, xdpi=160.42105, ydpi=160.15764}
        return resources.displayMetrics
    }

    /**
     * 获取状态栏的高度
     * @param context
     * @return
     */
    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    /**
     * 设置状态栏颜色和状态栏文字颜色
     * @param activity
     * @param color
     * @param whiteTextColor
     */
    fun setWindowStatusBarColor(activity: Activity, color: Int, whiteTextColor: Boolean) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window = activity.window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = color
                //底部导航栏
                window.navigationBarColor = color
                //android6.0以后可以对状态栏文字颜色和图标进行修改
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (whiteTextColor) {
                        //白色文字
                        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    } else {
                        //黑色文字
                        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    /**
     * 设置透明状态栏和状态栏文字颜色
     * @param activity
     */
    fun setWindowStatusBarTransparent(activity: Activity, whiteTextColor: Boolean) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window = activity.window
                //底部导航栏
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    // 状态栏透明
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                    if (whiteTextColor) {
                        //白色文字
                        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
                    } else {
                        //黑色文字
                        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
                    }
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    window.statusBarColor = Color.TRANSPARENT
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        //解决华为手机状态栏即使透明 仍旧有蒙层的问题
                        try {
                            val decorViewClazz = Class.forName("com.android.internal.policy.DecorView")
                            val field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor")
                            field.isAccessible = true
                            //改为透明
                            field.setInt(window.decorView, Color.TRANSPARENT)
                        } catch (e: Exception) {
                            //e.printStackTrace();
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    /**
     * 设置Dialog状态栏颜色
     * @param dialog
     * @param color
     * @param whiteTextColor
     */
    fun setWindowStatusBarColor(dialog: Dialog, color: Int, whiteTextColor: Boolean) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window = dialog.window
                window!!.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = color
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (whiteTextColor) {
                        //白色文字
                        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    } else {
                        //黑色文字
                        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 设置透明状态栏 + 沉浸
     * @param activity 当前展示的activity
     * @param toolbar
     * @return
     */
    fun setWindowStatusBarTransparent(activity: Activity, toolbar: Toolbar) {
        setWindowStatusBarTransparent(activity, true)
        val layoutParams = toolbar.layoutParams as MarginLayoutParams
        layoutParams.setMargins(
                layoutParams.leftMargin,
                layoutParams.topMargin + getStatusBarHeight(activity),
                layoutParams.rightMargin,
                layoutParams.bottomMargin)
        return
    }

    /**
     * 设置夜间模式
     * @param context
     * @param mode
     */
    fun setNightMode(context: Context, mode: Boolean) {
        if (mode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    /**
     * 打开键盘
     * @param context the context
     */
    fun showInputMethod(context: Context) {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    /**
     * 关闭键盘事件
     * @param context the context
     */
    fun closeInputMethod(context: Context) {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if ((context as Activity).currentFocus != null) {
            inputMethodManager.hideSoftInputFromWindow(context.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

}