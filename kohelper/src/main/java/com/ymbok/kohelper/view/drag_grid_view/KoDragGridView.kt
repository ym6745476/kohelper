package com.ymbok.kohelper.view.drag_grid_view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.*
import android.widget.AdapterView.OnItemLongClickListener

class KoDragGridView : GridView, AbsListView.OnScrollListener {
    private var dragImageView: ImageView? = null
    private var dragImageViewParams: WindowManager.LayoutParams? = null
    private var windowManager: WindowManager? = null
    private var isViewOnDrag = false

    /**previous dragged over position */
    private var preDraggedOverPositon = INVALID_POSITION
    private var downRawX = 0
    private var downRawY = 0

    /** The parent scroll view.  */
    var parentScrollView: ScrollView? = null
    private val onLongClickListener = OnItemLongClickListener { parent, view, position, id ->

        //长按item开始拖动
        setParentScrollAble(false)

        //记录长按item位置
        preDraggedOverPositon = position

        //获取被长按item的drawing cache
        view.destroyDrawingCache()
        view.isDrawingCacheEnabled = true
        //通过被长按item，获取拖动item的bitmap
        val dragBitmap = Bitmap.createBitmap(view.drawingCache)

        //设置拖动item的参数
        dragImageViewParams!!.gravity = Gravity.TOP or Gravity.LEFT
        //设置拖动item为原item 1.2倍
        dragImageViewParams!!.width = (AMP_FACTOR * dragBitmap.width).toInt()
        dragImageViewParams!!.height = (AMP_FACTOR * dragBitmap.height).toInt()
        //设置触摸点为绘制拖动item的中心
        dragImageViewParams!!.x = downRawX - dragImageViewParams!!.width / 2
        dragImageViewParams!!.y = downRawY - dragImageViewParams!!.height / 2
        dragImageViewParams!!.flags = (WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
        dragImageViewParams!!.format = PixelFormat.TRANSLUCENT
        dragImageViewParams!!.windowAnimations = 0

        //dragImageView为被拖动item的容器，清空上一次的显示
        if (dragImageView!!.tag as Int == DRAG_IMG_SHOW) {
            windowManager!!.removeView(dragImageView)
            dragImageView!!.tag = DRAG_IMG_NOT_SHOW
        }

        //设置本次被长按的item
        dragImageView!!.setImageBitmap(dragBitmap)

        //添加拖动item到屏幕
        windowManager!!.addView(dragImageView, dragImageViewParams)
        dragImageView!!.tag = DRAG_IMG_SHOW
        isViewOnDrag = true

        //设置被长按item不显示
        (adapter as KoDragGridViewAdapter).hideView(position)
        true
    }

    constructor(context: Context?) : super(context) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    fun initView() {
        setOnScrollListener(this)
        onItemLongClickListener = onLongClickListener
        //初始化显示被拖动item的image view
        dragImageView = ImageView(context)
        dragImageView!!.tag = DRAG_IMG_NOT_SHOW
        //初始化用于设置dragImageView的参数对象
        dragImageViewParams = WindowManager.LayoutParams()
        //获取窗口管理对象，用于后面向窗口中添加dragImageView
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {

        //被按下时记录按下的坐标
        if (ev.action == MotionEvent.ACTION_DOWN) {
            //获取触摸点相对于屏幕的坐标
            downRawX = ev.rawX.toInt()
            downRawY = ev.rawY.toInt()
        } else if (ev.action == MotionEvent.ACTION_MOVE && isViewOnDrag) {

            //Log.i(LOG_TAG, "" + ev.getRawX() + " " + ev.getRawY());
            //设置触摸点为dragImageView中心
            dragImageViewParams!!.x = (ev.rawX - dragImageView!!.width / 2).toInt()
            dragImageViewParams!!.y = (ev.rawY - dragImageView!!.height / 2).toInt()
            //更新窗口显示
            windowManager!!.updateViewLayout(dragImageView, dragImageViewParams)
            //获取当前触摸点的item position
            val currDraggedPosition = pointToPosition(ev.x.toInt(), ev.y.toInt())
            //如果当前停留位置item不等于上次停留位置的item，交换本次和上次停留的item
            if (currDraggedPosition != INVALID_POSITION && currDraggedPosition != preDraggedOverPositon) {
                (adapter as KoDragGridViewAdapter).swapView(preDraggedOverPositon, currDraggedPosition)
                preDraggedOverPositon = currDraggedPosition
            }
        } else if (ev.action == MotionEvent.ACTION_UP && isViewOnDrag) {
            (adapter as KoDragGridViewAdapter).showHideView()
            if (dragImageView!!.tag as Int == DRAG_IMG_SHOW) {
                windowManager!!.removeView(dragImageView)
                dragImageView!!.tag = DRAG_IMG_NOT_SHOW
            }
            isViewOnDrag = false
            setParentScrollAble(true)
        }
        return super.onTouchEvent(ev)
    }

    override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int,
                          totalItemCount: Int) {
        if (firstVisibleItem + visibleItemCount == totalItemCount) {
            //底部
        } else {
            //中部
        }
        //顶部
        if (firstVisibleItem == 0) {
        }
    }

    override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
        when (scrollState) {
            OnScrollListener.SCROLL_STATE_IDLE -> {}
            OnScrollListener.SCROLL_STATE_TOUCH_SCROLL -> {}
            OnScrollListener.SCROLL_STATE_FLING -> {}
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {}
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {}
            else -> {}
        }
        return super.onInterceptTouchEvent(ev)
    }

    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val expandSpec = MeasureSpec.makeMeasureSpec(
                Int.MAX_VALUE shr 2, MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, expandSpec)
    }

    private fun setParentScrollAble(flag: Boolean) {
        if (parentScrollView != null) {
            parentScrollView!!.requestDisallowInterceptTouchEvent(!flag)
        }
    }

    companion object {
        private const val DRAG_IMG_SHOW = 1
        private const val DRAG_IMG_NOT_SHOW = 0
        private const val AMP_FACTOR = 1.2f
    }
}