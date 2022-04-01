package com.ymbok.kohelper.app

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ymbok.kohelper.R
import com.ymbok.kohelper.utils.KoAppUtil
import com.ymbok.kohelper.utils.logD
import java.lang.ref.WeakReference

open class KoBaseActivity : AppCompatActivity() {

    /**
     * 日志输出标志
     */
    val TAG: String = this.javaClass.simpleName

    /** 当前Activity的弱引用，防止内存泄露  */
    private lateinit var activityWR: WeakReference<Activity>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logD(TAG, "BaseActivity-->onCreate()")

        activityWR = WeakReference(this)
        KoActivityStack.pushTask(activityWR)

        KoAppUtil.setWindowStatusBarTransparent(this, false)
    }

    /**
     * 显示页面
     * @param rootView
     * @param layoutResID  布局ID
     */
    open fun showEmptyPage(rootView: RelativeLayout,imageResId:Int,text:String): View?{
        val pageView = View.inflate(this, R.layout.ko_empty_page, null)
        val emptyImage = pageView.findViewById<ImageView>(R.id.empty_image)
        val emptyText = pageView.findViewById<TextView>(R.id.empty_text)
        emptyImage.setImageResource(imageResId)
        emptyText.text = text
        (rootView as ViewGroup).addView(pageView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        return pageView
    }

    /**
     * 隐藏页面
     * @param rootView
     */
    open fun hidePage(rootView: RelativeLayout, pageView: View?) {
        if (pageView != null) {
            (rootView as ViewGroup).removeView(pageView)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        logD(TAG, "BaseActivity-->onDestroy()")
        KoActivityStack.removeTask(activityWR)
    }
}