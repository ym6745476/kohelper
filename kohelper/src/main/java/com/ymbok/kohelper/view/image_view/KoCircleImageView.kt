package com.ymbok.kohelper.view.image_view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.ymbok.kohelper.R
import com.ymbok.kohelper.utils.logI
import java.lang.Exception

/**
 * Copyright ymbok.com
 * Author 还如一梦中
 * Date 2022/4/1 09:13
 * Email 396196516@qq.com
 * Info 圆形ImageView
 */
class KoCircleImageView : KoFilterImageView {
    private val TAG:String = this.javaClass.simpleName
    private val drawableRect = RectF()
    private val borderRect = RectF()
    private val shaderMatrix = Matrix()
    private val bitmapPaint: Paint = Paint()
    private val borderPaint = Paint()
    private val fillPaint = Paint()
    private var borderColor = DEFAULT_BORDER_COLOR
    private var borderWidth = DEFAULT_BORDER_WIDTH
    private var fillColor = DEFAULT_FILL_COLOR
    private var bitmap: Bitmap? = null
    private var bitmapShader: BitmapShader? = null
    private var bitmapWidth = 0
    private var bitmapHeight = 0
    private var drawableRadius = 0f
    private var borderRadius = 0f
    private var colorFilter: ColorFilter? = null
    private var ready = false
    private var setupPending = false
    private var borderOverlay = false
    private var disableCircularTransformation = false

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int = 0) : super(context, attrs, defStyle) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.KoCircleImageView, defStyle, 0)
        borderWidth = a.getDimensionPixelSize(R.styleable.KoCircleImageView_border_width, DEFAULT_BORDER_WIDTH)
        borderColor = a.getColor(R.styleable.KoCircleImageView_border_color, DEFAULT_BORDER_COLOR)
        borderOverlay = a.getBoolean(R.styleable.KoCircleImageView_border_overlay, DEFAULT_BORDER_OVERLAY)
        fillColor = a.getColor(R.styleable.KoCircleImageView_fill_color, DEFAULT_FILL_COLOR)
        a.recycle()
        initView()
    }

    private fun initView() {
        super.setScaleType(SCALE_TYPE)
        ready = true
        if (setupPending) {
            setup()
            setupPending = false
        }
    }

    override fun getScaleType(): ScaleType? {
        return SCALE_TYPE
    }

    override fun setScaleType(scaleType: ScaleType) {
        if(scaleType != SCALE_TYPE){
            throw IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType));
        }
    }

    override fun setAdjustViewBounds(adjustViewBounds: Boolean) {
        if (adjustViewBounds) {
            throw IllegalArgumentException("adjustViewBounds not supported.");
        }
    }

    protected override fun onDraw(canvas: Canvas) {
        if (disableCircularTransformation) {
            super.onDraw(canvas)
            return
        }
        if (bitmap == null) {
            return
        }
        if (fillColor != Color.TRANSPARENT) {
            canvas.drawCircle(drawableRect.centerX(), drawableRect.centerY(), drawableRadius, fillPaint)
        }
        canvas.drawCircle(drawableRect.centerX(), drawableRect.centerY(), drawableRadius, bitmapPaint!!)
        if (borderWidth > 0) {
            canvas.drawCircle(borderRect.centerX(), borderRect.centerY(), borderRadius, borderPaint)
        }
    }

    protected override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setup()
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)
        setup()
    }

    override fun setPaddingRelative(start: Int, top: Int, end: Int, bottom: Int) {
        super.setPaddingRelative(start, top, end, bottom)
        setup()
    }

    fun getBorderColor(): Int {
        return borderColor
    }

    fun setBorderColor(@ColorInt borderColor: Int) {
        if (this.borderColor == borderColor) {
            return
        }
        this.borderColor = borderColor
        borderPaint.color = borderColor
        invalidate()
    }

    fun getBorderWidth(): Int {
        return borderWidth
    }

    fun setBorderWidth(borderWidth: Int) {
        if (this.borderWidth == borderWidth) {
            return
        }
        this.borderWidth = borderWidth
        setup()
    }

    fun isBorderOverlay(): Boolean {
        return borderOverlay
    }

    fun setBorderOverlay(borderOverlay: Boolean) {
        if (this.borderOverlay == borderOverlay) {
            return
        }
        this.borderOverlay = borderOverlay
        setup()
    }

    fun isDisableCircularTransformation(): Boolean {
        return disableCircularTransformation
    }

    fun setDisableCircularTransformation(disableCircularTransformation: Boolean) {
        if (this.disableCircularTransformation == disableCircularTransformation) {
            return
        }
        this.disableCircularTransformation = disableCircularTransformation
        initializeBitmap()
    }

    override fun setImageBitmap(bm: Bitmap) {
        super.setImageBitmap(bm)
        initializeBitmap()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        initializeBitmap()
    }

    override fun setImageResource(@DrawableRes resId: Int) {
        super.setImageResource(resId)
        initializeBitmap()
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        initializeBitmap()
    }

    override fun setColorFilter(cf: ColorFilter) {
        if (cf === colorFilter) {
            return
        }
        colorFilter = cf
        applyColorFilter()
        invalidate()
    }

    override fun getColorFilter(): ColorFilter {
        return colorFilter!!
    }

    private fun applyColorFilter() {
        if (bitmapPaint != null) {
            bitmapPaint.colorFilter = colorFilter
        }
    }

    private fun initializeBitmap() {
        bitmap = if (disableCircularTransformation) {
            null
        } else {
            getBitmapFromDrawable(drawable)
        }
        setup()
    }

    private fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        if (drawable == null) {
            logI(TAG,"drawable is null")
            return null
        }
        return if (drawable is BitmapDrawable) {
            drawable.bitmap
        } else try {
            val bitmap: Bitmap = if (drawable is ColorDrawable) {
                Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG)
            } else {
                Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, BITMAP_CONFIG)
            }
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun setup() {
        if (!ready) {
            setupPending = true
            return
        }
        if (width == 0 && height == 0) {
            return
        }
        if (bitmap == null) {
            invalidate()
            return
        }
        bitmapShader = BitmapShader(bitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        bitmapPaint.isAntiAlias = true
        bitmapPaint.shader = bitmapShader
        borderPaint.style = Paint.Style.STROKE
        borderPaint.isAntiAlias = true
        borderPaint.color = borderColor
        borderPaint.strokeWidth = borderWidth.toFloat()
        fillPaint.style = Paint.Style.FILL
        fillPaint.isAntiAlias = true
        fillPaint.color = fillColor
        bitmapHeight = bitmap!!.height
        bitmapWidth = bitmap!!.width
        borderRect.set(calculateBounds())
        borderRadius = Math.min((borderRect.height() - borderWidth) / 2.0f, (borderRect.width() - borderWidth) / 2.0f)
        drawableRect.set(borderRect)
        if (!borderOverlay && borderWidth > 0) {
            drawableRect.inset(borderWidth - 1.0f, borderWidth - 1.0f)
        }
        drawableRadius = Math.min(drawableRect.height() / 2.0f, drawableRect.width() / 2.0f)
        applyColorFilter()
        updateShaderMatrix()
        invalidate()
    }

    private fun calculateBounds(): RectF {
        val availableWidth: Int = width - paddingLeft - paddingRight
        val availableHeight: Int = height - paddingTop - paddingBottom
        val sideLength = Math.min(availableWidth, availableHeight)
        val left: Float = paddingLeft + (availableWidth - sideLength) / 2f
        val top: Float = paddingTop + (availableHeight - sideLength) / 2f
        return RectF(left, top, left + sideLength, top + sideLength)
    }

    private fun updateShaderMatrix() {
        val scale: Float
        var dx = 0f
        var dy = 0f
        shaderMatrix.set(null)
        if (bitmapWidth * drawableRect.height() > drawableRect.width() * bitmapHeight) {
            scale = drawableRect.height() / bitmapHeight.toFloat()
            dx = (drawableRect.width() - bitmapWidth * scale) * 0.5f
        } else {
            scale = drawableRect.width() / bitmapWidth.toFloat()
            dy = (drawableRect.height() - bitmapHeight * scale) * 0.5f
        }
        shaderMatrix.setScale(scale, scale)
        shaderMatrix.postTranslate((dx + 0.5f).toInt() + drawableRect.left, (dy + 0.5f).toInt() + drawableRect.top)
        bitmapShader!!.setLocalMatrix(shaderMatrix)
    }

    companion object {
        private val SCALE_TYPE = ScaleType.CENTER_CROP
        private val BITMAP_CONFIG = Bitmap.Config.ARGB_8888
        private const val COLORDRAWABLE_DIMENSION = 2
        private const val DEFAULT_BORDER_WIDTH = 0
        private const val DEFAULT_BORDER_COLOR = Color.BLACK
        private const val DEFAULT_FILL_COLOR = Color.TRANSPARENT
        private const val DEFAULT_BORDER_OVERLAY = false
    }
}