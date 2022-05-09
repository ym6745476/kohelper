package com.ymbok.kohelper.app

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.ymbok.kohelper.R
import com.ymbok.kohelper.utils.KoDialogUtil

open class KoBaseFragment : Fragment() {

    /**
     * 日志输出标志
     */
    val TAG: String = this.javaClass.simpleName

    lateinit var glide: RequestManager

    /** View  */
    var loadingView: View? = null
    var loadingRootView: RelativeLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        glide = Glide.with(this)
    }

    open fun showLoading(rootView: RelativeLayout) {
        loadingRootView = rootView
        loadingView = showPlaceView(rootView,R.layout.ko_view_loading)
    }

    open fun hideLoading() {
        loadingRootView?.let { loadingView?.let { it1 -> hidePlaceView(it, it1) } }
    }

    /**
     * 显示占位页面
     * @param rootView       容器RelativeLayout
     * @param placeLayoutId  布局ID
     */
    open fun showPlaceView(rootView: RelativeLayout,@LayoutRes placeLayoutId:Int): View{
        val placeView = View.inflate(activity, placeLayoutId, null)
        return showPlaceView(rootView,placeView)
    }
    open fun showPlaceView(rootView: RelativeLayout,placeView:View): View{
        (rootView as ViewGroup).addView(placeView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        return placeView
    }

    /**
     * 隐藏页面
     * @param rootView
     * @param pageView
     */
    open fun hidePlaceView(rootView: RelativeLayout, pageView: View) {
        (rootView as ViewGroup).removeView(pageView)
    }

}