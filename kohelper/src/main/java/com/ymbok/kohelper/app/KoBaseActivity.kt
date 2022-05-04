package com.ymbok.kohelper.app

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.ymbok.kohelper.R
import com.ymbok.kohelper.utils.KoAppUtil
import com.ymbok.kohelper.utils.KoDialogUtil
import com.ymbok.kohelper.utils.logD
import java.lang.ref.WeakReference

open class KoBaseActivity : FragmentActivity() {

    /**
     * 日志输出标志
     */
    val TAG: String = this.javaClass.simpleName

    /** 当前Activity的弱引用，防止内存泄露  */
    private lateinit var activityWR: WeakReference<Activity>

    /** View  */
    var loadingDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityWR = WeakReference(this)
        KoActivityStack.pushTask(activityWR)

        KoAppUtil.setWindowStatusBarTransparent(this, false)
    }

    /**
     * 显示占位页面
     * @param rootView       容器RelativeLayout
     * @param placeLayoutId  布局ID
     */
    open fun showPlaceView(rootView: RelativeLayout,@LayoutRes placeLayoutId:Int): View{
        val placeView = View.inflate(this, placeLayoutId, null)
        return showPlaceView(rootView,placeView)
    }
    open fun showPlaceView(rootView: RelativeLayout,placeView:View): View{
        (rootView as ViewGroup).addView(placeView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        return placeView
    }

    /**
     * 显示页面
     * @param rootView    容器RelativeLayout
     * @param imageResId  布局ID
     * @param text  提示
     */
    open fun showEmptyPage(rootView: RelativeLayout,imageResId:Int,text:String): View?{
        val pageView = View.inflate(this, R.layout.ko_empty_page, null)
        val emptyImage = pageView.findViewById<ImageView>(R.id.empty_image)
        val emptyText = pageView.findViewById<TextView>(R.id.empty_text)
        emptyImage.setImageResource(imageResId)
        emptyText.text = text
        return showPlaceView(rootView,pageView)
    }

    /**
     * 隐藏页面
     * @param rootView
     */
    open fun hidePage(rootView: RelativeLayout, pageView: View) {
        (rootView as ViewGroup).removeView(pageView)
    }

    open fun showLoading() {
        val loadingView = View.inflate(this, R.layout.ko_view_loading, null)
        loadingDialog = KoDialogUtil.showLayer(this,loadingView)
    }

    open fun hideLoading() {
        loadingDialog?.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        KoActivityStack.removeTask(activityWR)
    }
}