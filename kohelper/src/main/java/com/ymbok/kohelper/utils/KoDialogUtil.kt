package com.ymbok.kohelper.utils
import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import androidx.appcompat.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.view.WindowManager
import android.widget.*
import com.ymbok.kohelper.R
import kotlinx.coroutines.Job


/**
 * 弹窗类。
 * @author ym6745476
 * @since  2021/10/14
 */

object KoDialogUtil {

    interface KoDialogOnClickListener {
        fun onClick(dialog:AlertDialog,view: View)
    }

    /**
     * 显示弹出框
     */
    fun showAlertDialog(context: Context, title: String?, message: String?, cancelText:String, okText:String, cancelListener:KoDialogOnClickListener, okListener: KoDialogOnClickListener):AlertDialog {
        val view = View.inflate(context, R.layout.ko_view_dialog, null)

        val dialog:AlertDialog = AlertDialog.Builder(context).setView(view).create()

        title?.apply {
            val titleLayout = view.findViewById<LinearLayout>(R.id.dialog_title_layout)
            titleLayout.visibility = View.VISIBLE
            val titleView = view.findViewById<TextView>(R.id.dialog_title_text)
            titleView.text = title
        }

        message?.apply {
            val messageLayout = view.findViewById<ScrollView>(R.id.dialog_message_layout)
            messageLayout.visibility = View.VISIBLE
            val messageView = view.findViewById<TextView>(R.id.dialog_message_text)
            messageView.text = message
        }

        val cancelBtn = view.findViewById<Button>(R.id.dialog_button_cancel)
        val okBtn = view.findViewById<Button>(R.id.dialog_button_ok)
        cancelBtn.text = cancelText
        okBtn.text = okText
        cancelBtn.setOnClickListener {
            dialog.dismiss()
            cancelListener.onClick(dialog,it)
        }
        okBtn.setOnClickListener {
            okListener.onClick(dialog,it)
        }

        dialog.show()

        return dialog

    }

    /**
     * 显示弹出框
     */
    fun showViewDialog(context: Context, view: View):AlertDialog {
        val dialog = AlertDialog.Builder(context).setView(view).create()
        dialog.show()
        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout((KoAppUtil.getDisplayMetrics(context).widthPixels * 0.80).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)

        }
        return dialog
    }

    /**
     * 显示全屏层
     */
    fun showLayer(context: Context, view: View):AlertDialog {
        val dialog = AlertDialog.Builder(context).create()
        dialog.show()
        val window: Window? = dialog.window
        window?.apply {
            val attributes = attributes
            attributes.gravity = Gravity.BOTTOM
            decorView.setPadding( 0 , 0 , 0 , 0 )
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            window.setBackgroundDrawableResource(android.R.color.transparent)
            window.setContentView(view)

        }
        return dialog
    }

    /**
     * 显示确认框
     */
    fun showConfirmDialog(context: Context, title: String?, message: String?,okListener: KoDialogOnClickListener):AlertDialog {
        val view = View.inflate(context, R.layout.ko_view_dialog, null)

        val dialog:AlertDialog = AlertDialog.Builder(context).setView(view).create()

        title?.apply {
            val titleLayout = view.findViewById<LinearLayout>(R.id.dialog_title_layout)
            titleLayout.visibility = View.VISIBLE
            val titleView = view.findViewById<TextView>(R.id.dialog_title_text)
            titleView.text = title
        }

        message?.apply {
            val messageLayout = view.findViewById<ScrollView>(R.id.dialog_message_layout)
            messageLayout.visibility = View.VISIBLE
            val messageView = view.findViewById<TextView>(R.id.dialog_message_text)
            messageView.text = message
        }


        (view.findViewById<Button>(R.id.dialog_button_cancel).parent as LinearLayout).visibility = View.GONE

        view.findViewById<Button>(R.id.dialog_button_ok).setOnClickListener {
            okListener.onClick(dialog,it)
        }

        dialog.show()

        return dialog

    }

    /**
     * 在点击的View下方显示层
     */
    fun showDropDownWindow(context: Context, contentView: View, anchorView:View):PopupWindow {
        val popupWindow = PopupWindow(contentView,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        // 设置背景
        popupWindow.setBackgroundDrawable(ColorDrawable())
        // 外部点击事件
        popupWindow.isOutsideTouchable = true


        val appScreenLocation = IntArray(2)
        val appRootView: View = anchorView.rootView
        appRootView.getLocationOnScreen(appScreenLocation)

        val screenLocation = IntArray(2)
        anchorView.getLocationOnScreen(screenLocation)

        val drawingLocation = IntArray(2)
        drawingLocation[0] = screenLocation[0] - appScreenLocation[0]
        drawingLocation[1] = screenLocation[1] - appScreenLocation[1]
        var x = drawingLocation[0]
        val y = drawingLocation[1] + anchorView.height
        logI("showDropDownWindow", "dropDown Y:${y.toString()}")

        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val lp = WindowManager.LayoutParams()
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        // 不设置这个弹出框的透明遮罩显示为黑色
        lp.format = PixelFormat.TRANSLUCENT
        // 该Type描述的是形成的窗口的层级关系
        lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL
        // 获取当前Activity中的View中的token,来依附Activity
        lp.token = contentView.windowToken
        //val maskView = View(context)
        //maskView.setBackgroundColor(0x7f000000)

        val maskView = View.inflate(context, R.layout.ko_view_mask, null)
        maskView.findViewById<LinearLayout>(R.id.mask_top_view).layoutParams.height = KoUnitUtil.dip2px(context,y.toFloat()).toInt()

        maskView.fitsSystemWindows = false
        /* maskView.setOnKeyListener(object:View.OnKeyListener{
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean{
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    windowManager.removeViewImmediate(maskView);
                    return true
                }
               return false
            }
        })*/

        windowManager.addView(maskView, lp)

        popupWindow.setOnDismissListener {
            windowManager.removeViewImmediate(maskView);
        }

        popupWindow.showAsDropDown(anchorView,0, 10,Gravity.BOTTOM)

        return popupWindow

    }
}