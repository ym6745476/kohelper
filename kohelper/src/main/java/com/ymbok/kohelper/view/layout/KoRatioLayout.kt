package com.ymbok.kohelper.view.layout

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View.MeasureSpec
import android.widget.RelativeLayout
import com.ymbok.kohelper.R

/**
 * Copyright ymbok.com
 * Author 还如一梦中
 * Date 2018/6/4 09:13
 * Email 396196516@qq.com
 * Info 有比例的Layout
 */
class KoRatioLayout : RelativeLayout {

    private val defaultWidthRatio = 3
    private val defaultHeightRatio = 2

    private var widthRatio = 0
    private var heightRatio = 0

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) :this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        val a = context.obtainStyledAttributes(attrs, R.styleable.KoAspectRatioImageView)
        try {
            widthRatio = a.getInteger(R.styleable.KoAspectRatioImageView_width_ratio, 0)
            heightRatio = a.getInteger(R.styleable.KoAspectRatioImageView_height_ratio, 0)
            if (widthRatio == 0 || heightRatio == 0) {
                widthRatio = defaultWidthRatio
                heightRatio = defaultHeightRatio
            }
        } finally {
            a.recycle()
        }
    }


    fun setWidthRatio(widthRatio: Int) {
        this.widthRatio = widthRatio
    }

    fun setHeightRatio(heightRatio: Int) {
        this.heightRatio = heightRatio
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = MeasureSpec.getSize(widthMeasureSpec) * heightRatio / widthRatio
        val heightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, heightSpec)
    }
}