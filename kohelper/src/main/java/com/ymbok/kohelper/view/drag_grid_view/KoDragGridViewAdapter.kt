package com.ymbok.kohelper.view.drag_grid_view

import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter

abstract class KoDragGridViewAdapter : BaseAdapter() {

    var hidePosition = AdapterView.INVALID_POSITION

    abstract fun swapView(draggedPos: Int, destPos: Int)

    override fun getCount(): Int {
        return 0
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        return null
    }


    fun hideView(pos: Int) {
        hidePosition = pos
        notifyDataSetChanged()
    }

    fun showHideView() {
        hidePosition = AdapterView.INVALID_POSITION
        notifyDataSetChanged()
    }
}