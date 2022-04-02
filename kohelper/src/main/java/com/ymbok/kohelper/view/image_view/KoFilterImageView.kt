package com.ymbok.kohelper.view.image_view

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import com.ymbok.kohelper.R
import com.ymbok.kohelper.utils.logI

/**
 * Copyright ymbok.com
 * Author 还如一梦中
 * Date 2022/4/1 09:13
 * Email 396196516@qq.com
 * Info 滤镜效果的ImageView，获取焦点产生滤镜效果
 */
open class KoFilterImageView : AppCompatImageView {
    private val TAG:String = this.javaClass.simpleName
    private var touchFlag = 0
    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            if (msg.what == 0) {
                setFilter()
            } else {
                removeFilter()
            }
        }
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        this.isClickable = true
        this.isFocusable = true
    }

    /**
     * 设置滤镜
     */
    private fun setFilter() {
        var drawableFilter = background

        if (drawableFilter == null) {
            drawableFilter = drawable
            if (drawableFilter != null) {
                //设置滤镜
                drawableFilter.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY)
                setImageDrawable(drawableFilter)
            }
        } else {
            drawableFilter.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY)
            setBackgroundDrawable(drawableFilter)
        }
    }

    /**
     * 清除滤镜
     */
    private fun removeFilter() {
        var drawableFilter = drawable
        if (drawableFilter == null) {
            drawableFilter = background
            if (drawableFilter != null) {
                //清除滤镜
                drawableFilter.clearColorFilter()
                setBackgroundDrawable(drawableFilter)
            }
        } else {
            //清除滤镜
            drawableFilter.clearColorFilter()
            setImageDrawable(drawableFilter)
        }
    }

    override fun onFocusChanged(gainFocus: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        if (gainFocus) {
            handler.sendEmptyMessage(0)
        } else {
            handler.sendEmptyMessage(1)
        }
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (touchFlag == 1) {
            return true
        }
        touchFlag = 1
        if (event.action == MotionEvent.ACTION_DOWN) {
            handler.sendEmptyMessage(0)
        } else if (event.action == MotionEvent.ACTION_UP) {
            handler.sendEmptyMessage(1)
        } else if (event.action == MotionEvent.ACTION_CANCEL) {
            handler.sendEmptyMessage(1)
        }
        touchFlag = 0
        return super.onTouchEvent(event)
    }
}
