package com.ymbok.kohelper.component

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.ymbok.kohelper.utils.KoViewUtil

/**
 * Copyright ymbok.com
 * Author 还如一梦中
 * Date 2017/6/16 13:27
 * Email 396196516@qq.com
 * Info  垂直 RecyclerView 分隔线设置
 */
class KoRecyclerViewVerticalDecoration : ItemDecoration {
    private var context: Context? = null
    private var space = 0
    private var leftRightPadding = 0
    private var paint: Paint? = null
    private var hasHeader = false
    private var hasFooter = false

    constructor(context: Context, space: Int, spaceColor: Int) {
        this.context = context
        hasHeader = false
        hasFooter = false
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        if (space == 1) {
            this.space = 1
            paint!!.color = spaceColor
        } else if (space > 0) {
            this.space = KoViewUtil.dip2px(context, space.toFloat()).toInt()
            paint!!.color = spaceColor
        }
        paint!!.style = Paint.Style.FILL
    }

    constructor(context: Context, space: Int, spaceColor: Int, hasHeader: Boolean, hasFooter: Boolean) {
        this.context = context
        this.hasHeader = hasHeader
        this.hasFooter = hasFooter
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        if (space == 1) {
            this.space = 1
            paint!!.color = spaceColor
        } else if (space > 0) {
            this.space = KoViewUtil.dip2px(context, space.toFloat()).toInt()
            paint!!.color = spaceColor
        }
        paint!!.style = Paint.Style.FILL
    }

    override fun getItemOffsets(outRect: Rect, view: View,parent: RecyclerView, state: RecyclerView.State) {
        outRect.left = leftRightPadding
        outRect.right = leftRightPadding
        if (space > 2) {
            outRect.top = (space / 2F).toInt()
            outRect.bottom = (space / 2F).toInt()
        }else if (space == 1) {
            outRect.top = 0
            outRect.bottom = 1
        }else{
            outRect.top = 0
            outRect.bottom = 0
        }
    }

    /**
     * 绘制分割线
     */
    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(canvas, parent, state)
        if (space > 0) {
            drawSpaceLine(canvas, parent)
        }
    }

    private fun drawSpaceLine(canvas: Canvas, parent: RecyclerView) {
        val left = leftRightPadding
        val right = parent.measuredWidth - leftRightPadding
        val childSize = parent.childCount
        for (i in 0 until childSize) {
            val child = parent.getChildAt(i)
            val top = child.bottom
            val bottom = top + space
            canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint!!)
        }
    }

    fun setLeftRightPadding(leftRightPadding: Int) {
        context?.also {
            this.leftRightPadding = KoViewUtil.dip2px(it, leftRightPadding.toFloat()).toInt()
        }

    }
}