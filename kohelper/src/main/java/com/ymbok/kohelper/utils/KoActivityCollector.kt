
package com.ymbok.kohelper.utils

import android.app.Activity
import java.lang.ref.WeakReference
import java.util.*

/**
 * 管理应用程序中所有Activity。
 * @author ym6745476
 * @since  2021/10/14
 */

// object对象声明 单例声明
object KoActivityCollector {

    private val activitys = Stack<WeakReference<Activity>>()

    /**
     * 将Activity压入Application栈
     *
     * @param task 将要压入栈的Activity对象
     */
    fun pushTask(task: WeakReference<Activity>?) {
        activitys.push(task)
    }

    /**
     * 将传入的Activity对象从栈中移除
     *
     * @param task
     */
    fun removeTask(task: WeakReference<Activity>?) {
        activitys.remove(task)
    }

    /**
     * 根据指定位置从栈中移除Activity
     *
     * @param taskIndex Activity栈索引
     */
    fun removeTask(taskIndex: Int) {
        if (activitys.size > taskIndex) activitys.removeAt(taskIndex)
    }

    /**
     * 将栈中Activity移除至栈顶
     */
    fun removeToTop() {
        val end = activitys.size
        val start = 1
        for (i in end - 1 downTo start) {
            val activity = activitys[i].get()
            if (null != activity && !activity.isFinishing) {
                activity.finish()
            }
        }
    }

    /**
     * 移除全部（用于整个应用退出）
     */
    fun removeAll() {
        for (task in activitys) {
            val activity = task.get()
            if (null != activity && !activity.isFinishing) {
                activity.finish()
            }
        }
    }

}
