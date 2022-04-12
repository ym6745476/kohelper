package com.ymbok.kohelper.component

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.ymbok.kohelper.utils.KoUnitUtil

class KoRecyclerViewHorizontalDecoration: ItemDecoration {

    private var context: Context? = null
    private var space = 0F
    private var topBottomPadding = 0

    constructor(context: Context, space: Int) {
        this.context = context
        this.space = KoUnitUtil.dip2px(context, space.toFloat())
    }

    override fun getItemOffsets(outRect: Rect, view: View,parent: RecyclerView, state: RecyclerView.State) {
        outRect.left = (space / 2).toInt()
        outRect.right = (space / 2).toInt()
        if (topBottomPadding == 0) {
            outRect.top = (space / 2).toInt()
            outRect.bottom = (space / 2).toInt()
        } else {
            outRect.top = topBottomPadding
            outRect.bottom = topBottomPadding
        }
    }

}