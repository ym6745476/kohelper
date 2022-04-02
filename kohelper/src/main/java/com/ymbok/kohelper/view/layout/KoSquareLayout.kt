package com.ymbok.kohelper.view.layout

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout

/**
 * Copyright ymbok.com
 * Author 还如一梦中
 * Date 2022/4/1 09:13
 * Email 396196516@qq.com
 * Info 正方形的Layout
 */
class KoSquareLayout : RelativeLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthMeasureSpec = widthMeasureSpec
        var heightMeasureSpec = heightMeasureSpec
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec))
        val childWidthSize = measuredWidth
        val childHeightSize = measuredHeight
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY)
        heightMeasureSpec = widthMeasureSpec
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}