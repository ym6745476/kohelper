package com.ymbok.kohelper.view.image_view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet

/**
 * Copyright ymbok.com
 * Author 还如一梦中
 * Date 2022/4/1 09:13
 * Email 396196516@qq.com
 * Info 圆形ImageView
 */
class KoCircleImageView : KoFilterImageView {

    private var width = 0f
    private var height = 0f
    private var radius = 0f

    constructor(context: Context) : this(context,null)

    constructor(context: Context, attrs: AttributeSet?) : this(context,attrs,0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        width = getWidth().toFloat()
        height = getHeight().toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        radius = if(width < height){
            width/2
        }else{
            height/2
        }

        val path = Path()
        path.addCircle(width/2,height/2,radius, Path.Direction.CW)
        canvas.clipPath(path)

        super.onDraw(canvas)
    }


}