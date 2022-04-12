package com.ymbok.kohelper.component

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.ymbok.kohelper.utils.KoUnitUtil

class KoRecyclerViewGridItemDecoration: ItemDecoration {

    private var context: Context? = null
    private var space = 0F
    private var column = 0
    private var hasHeader = false
    private var hasFooter = false

    /** 第1个和最后一个要不要边距 */
    private var leftRightPadding = false

    constructor(context: Context, space: Int, column: Int) {
        this.context = context
        this.space = KoUnitUtil.dip2px(context, space.toFloat())
        this.column = column
        hasHeader = false
        hasFooter = false
    }

    constructor(context: Context, space: Int, column: Int, hasHeader: Boolean, hasFooter: Boolean) {
        this.context = context
        this.space = KoUnitUtil.dip2px(context, space.toFloat())
        this.column = column
        this.hasHeader = hasHeader
        this.hasFooter = hasFooter
    }

    override fun getItemOffsets(outRect: Rect, view: View,parent: RecyclerView, state: RecyclerView.State) {

        outRect.top = (space / 2).toInt()
        outRect.bottom = (space / 2).toInt()
        val position = parent.getChildLayoutPosition(view)
        if (hasHeader) {
            //第一个是Header
            if (position == 0) {
                outRect.top = 0
                outRect.left = 0
                outRect.right = 0
                outRect.bottom = 0
            } else {
                if (leftRightPadding) {
                    if ((position - 1) % column == 0) {
                        outRect.left = space.toInt()
                        outRect.right = (space / 2).toInt()
                    } else if ((position - 1) % column == column - 1) {
                        outRect.left = (space / 2).toInt()
                        outRect.right = space.toInt()
                    } else {
                        outRect.left = (space / 2).toInt()
                        outRect.right = (space / 2).toInt()
                    }
                } else {
                    outRect.left = (space / 2).toInt()
                    outRect.right = (space / 2).toInt()
                }
            }
        } else {
            if (leftRightPadding) {
                if (position % column == 0) {
                    outRect.left = space.toInt()
                    outRect.right = (space / 2).toInt()
                } else if (position % column == column - 1) {
                    outRect.left = (space / 2).toInt()
                    outRect.right = space.toInt()
                } else {
                    outRect.left = (space / 2).toInt()
                    outRect.right = (space / 2).toInt()
                }
            } else {
                outRect.left = (space / 2).toInt()
                outRect.right = (space / 2).toInt()
            }
        }

        //最后一个是footer
        if (hasFooter) {
            outRect.left = 0
            outRect.right = 0
            outRect.bottom = 0

        }
    }

    fun setLeftRightPadding(leftRightPadding: Boolean) {
        this.leftRightPadding = leftRightPadding
    }
}