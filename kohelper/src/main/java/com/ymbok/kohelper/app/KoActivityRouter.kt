package com.ymbok.kohelper.app

import android.app.Activity
import android.content.Context
import android.view.View
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavigationCallback
import com.alibaba.android.arouter.launcher.ARouter
import com.ymbok.kohelper.utils.logI
import java.lang.Boolean.parseBoolean
import java.util.HashMap

object KoActivityRouter {

    val TAG: String = this.javaClass.simpleName

    fun clickRoute(view: View, path: String) {
        view.setOnClickListener {
            route(view.context, path)
        }
    }

    fun clickRouteFinish(view: View, path: String) {
        view.setOnClickListener {
            routeFinish(view.context, path)
        }
    }

    fun route(context: Context, path: String) {
        logI(TAG, "开始Route：$path")

        // 如果是http  跳转到webView
        if (isHttp(path)) {
            val urlParams = parseUrl(path)
            urlParams.params["url"] = urlParams.url
            execRoute(context, "/app/browser", urlParams)
        } else {
            // 注解路由
            execRoute(context, path)
        }
    }

    fun routeFinish(context: Context, path: String) {
        route(context, path)
        (context as Activity).finish()
    }

    private fun isHttp(path: String): Boolean {
        return path.indexOf("http") == 0 || path.indexOf("https") == 0 || path.indexOf("www") == 0
    }

    /**
     * 解析url
     * @param url
     * @return
     */
    private fun parseUrl(url: String): UrlParams {
        var url = url
        val urlParams = UrlParams()
        urlParams.params.clear()

        url = url.trim { it <= ' ' }
        val urlParts = url.split("?")
        urlParams.url = urlParts[0]
        //没有参数
        if (urlParts.size == 1) {
            return urlParams
        }
        //有参数
        val params = urlParts[1].split("&")
        for (param in params) {
            val keyValue = param.split("=")
            if (keyValue.size > 1) {
                if ("true" == keyValue[1] || "false" == keyValue[1]) {
                    urlParams.params[keyValue[0]] = parseBoolean(keyValue[1])
                } else {
                    urlParams.params[keyValue[0]] = keyValue[1]
                }
            }
        }
        return urlParams
    }

    /**
     * 符合aroute条件才执行这个函数
     * @param context
     * @param path
     */
    private fun execRoute(context: Context, path: String) {
        val urlParams = parseUrl(path)
        val postcard: Postcard = ARouter.getInstance().build(urlParams.url)
        for ((key, value) in urlParams.params.entries) {
            if (value is Boolean) {
                postcard.withBoolean(key, value)
            } else {
                postcard.withString(key, value as String)
            }
        }
        postcard.navigation(context, object : NavigationCallback {
            override fun onFound(postcard: Postcard) {}
            override fun onLost(postcard: Postcard) {}
            override fun onArrival(postcard: Postcard) {}
            override fun onInterrupt(postcard: Postcard) {}
        })
    }

    private fun execRoute(context: Context, path: String, urlParams: UrlParams) {
        val postcard = ARouter.getInstance().build(path)
        for ((key, value) in urlParams.params.entries) {
            if (value is Boolean) {
                postcard.withBoolean(key, value)
            } else {
                postcard.withString(key, value as String)
            }
        }
        postcard.navigation(context, object : NavigationCallback {
            override fun onFound(postcard: Postcard) {}
            override fun onLost(postcard: Postcard) {}
            override fun onArrival(postcard: Postcard) {}
            override fun onInterrupt(postcard: Postcard) {}
        })
    }

    class UrlParams {
        /**
         * 基础url
         */
        lateinit var url: String

        /**
         * url参数
         */
        var params = HashMap<String, Any>()

        fun getString(key: String): String {
            return if (params.containsKey(key)) {
                params[key] as String
            } else {
                ""
            }
        }

        fun getBoolean(key: String): Boolean {
            return if (params.containsKey(key)) {
                params[key] as Boolean
            } else {
                false
            }
        }
    }
}