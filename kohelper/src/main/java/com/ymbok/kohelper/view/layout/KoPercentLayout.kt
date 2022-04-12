package com.ymbok.kohelper.view.layout

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.ymbok.kohelper.R
import com.ymbok.kohelper.utils.KoAppUtil

/**
 * Copyright ymbok.com
 * Author 还如一梦中
 * Date 2022/4/1 09:13
 * Email 396196516@qq.com
 * Info 设置屏幕百分比Layout
 */
class KoPercentLayout : RelativeLayout {

    private var widthPercent = 0
    private var heightPercent = 0

    private var widthSpec = 100
    private var heightSpec = 100

    constructor(context: Context) : this(context,null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle){
        val a = context.obtainStyledAttributes(attrs, R.styleable.KoPercentLayout)
        try {
            widthPercent = a.getInteger(R.styleable.KoPercentLayout_width_percent, 0)
            heightPercent = a.getInteger(R.styleable.KoPercentLayout_height_percent, 0)
        } finally {
            a.recycle()
        }
        val dm = KoAppUtil.getDisplayMetrics(context)
        widthSpec = dm.widthPixels * widthPercent/100
        heightSpec = dm.heightPixels * heightPercent/100
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthMeasureSpec = widthMeasureSpec
        var heightMeasureSpec = heightMeasureSpec
        if(widthSpec > 0){
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSpec, MeasureSpec.EXACTLY)
        }else{
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSpec, MeasureSpec.EXACTLY)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}