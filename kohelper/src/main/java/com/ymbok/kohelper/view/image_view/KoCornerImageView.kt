package com.ymbok.kohelper.view.image_view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Path
import android.os.Build
import android.util.AttributeSet
import com.ymbok.kohelper.R
import com.ymbok.kohelper.utils.KoUnitUtil

/**
 * Copyright ymbok.com
 * Author 还如一梦中
 * Date 2016/12/13 09:13
 * Email 396196516@qq.com
 * Info 圆角ImageView
 */
class KoCornerImageView : KoFilterImageView {

    private var width = 0f
    private var height = 0f
    private var radius = 0f

    constructor(context: Context) : this(context,null)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int = 0) : super(context, attrs, defStyle) {
        if (Build.VERSION.SDK_INT < 18) {
            setLayerType(LAYER_TYPE_SOFTWARE, null)
        }
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.KoRoundImageView)
        radius = try {
            val radiusDip = typedArray.getInteger(R.styleable.KoRoundImageView_radius, 0)
            KoUnitUtil.dip2px(context, radiusDip.toFloat())
        } finally {
            typedArray.recycle()
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        width = getWidth().toFloat()
        height = getHeight().toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        if (width > radius && height > radius) {
            val path = Path()
            path.moveTo(radius, 0f)
            path.lineTo(width - radius, 0f)
            path.quadTo(width, 0f, width, radius)
            path.lineTo(width, height - radius)
            path.quadTo(width, height.toFloat(), width - radius, height.toFloat())
            path.lineTo(radius, height.toFloat())
            path.quadTo(0f, height.toFloat(), 0f, height - radius)
            path.lineTo(0f, radius)
            path.quadTo(0f, 0f, radius, 0f)
            canvas.clipPath(path)
        }
        super.onDraw(canvas)
    }


}