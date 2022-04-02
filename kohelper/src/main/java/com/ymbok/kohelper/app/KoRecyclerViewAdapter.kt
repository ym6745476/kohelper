package com.ymbok.kohelper.app

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ymbok.kohelper.component.KoDataBingRecyclerViewHolder

/**
 * Copyright ymbok.com
 * Author 还如一梦中
 * Date 2022/4/2 09:13
 * Email 396196516@qq.com
 * Info RecyclerView.Adapter 支持增加头和尾
 */
abstract class KoRecyclerViewAdapter<T>(context: Context, var list: MutableList<T>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var headerViewHolder: KoDataBingRecyclerViewHolder? = null
    private var footerViewHolder: KoDataBingRecyclerViewHolder? = null
    val TYPE_HEADER = 1
    val TYPE_NORMAL = 2
    val TYPE_FOOTER = 3
    var hasHeader = false
    var hasFooter = false

    /**
     * 需要重写的
     * @param parent
     * @param viewType
     * @return
     */
    abstract fun onCreateRecyclerViewHolder(parent: ViewGroup, viewType: Int): KoDataBingRecyclerViewHolder
    abstract fun onBindRecyclerViewHolder(holder: KoDataBingRecyclerViewHolder, position: Int)

    open fun onCreateHeaderViewHolder(parent: ViewGroup, viewType: Int): KoDataBingRecyclerViewHolder?{
        return null
    }
    open fun onBindHeaderRecyclerViewHolder(holder: KoDataBingRecyclerViewHolder, position: Int) {}

    open fun onCreateFooterViewHolder(parent: ViewGroup, viewType: Int): KoDataBingRecyclerViewHolder?{
        return null
    }
    open fun onBindFooterRecyclerViewHolder(holder: KoDataBingRecyclerViewHolder, position: Int) {}

    /**
     * 可选择重写的
     * @param position
     */
    private fun getRecyclerItemViewType(position: Int): Int {
        return TYPE_NORMAL
    }

    override  fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
        if (viewType == TYPE_HEADER) {
            headerViewHolder = onCreateHeaderViewHolder(parent,viewType)
            return headerViewHolder as RecyclerView.ViewHolder
        } else if (viewType == TYPE_FOOTER) {
            footerViewHolder = onCreateFooterViewHolder(parent,viewType)
            return footerViewHolder as RecyclerView.ViewHolder
        } else {
            return onCreateRecyclerViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as KoDataBingRecyclerViewHolder
        val type = getItemViewType(position)
        if (type == TYPE_HEADER) {
            onBindHeaderRecyclerViewHolder(holder, position)
        } else if (type == TYPE_FOOTER) {
            onBindFooterRecyclerViewHolder(holder, position)
        } else {
            var index = position
            if (headerViewHolder != null) {
                index = position - 1
            }
            onBindRecyclerViewHolder(holder, index)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0 && hasHeader) {
            return TYPE_HEADER
        } else if (position == itemCount - 1 && hasFooter) {
            return TYPE_FOOTER
        }
        var index = position
        if (hasHeader) {
            index = position - 1
        }
        return getRecyclerItemViewType(index)
    }

    override fun getItemCount(): Int {
        var count: Int = list.size
        if (hasHeader) {
            count++
        }
        if (hasFooter) {
            count++
        }
        return count
    }

    fun removeItem(index: Int) {
        list.removeAt(index)
        notifyDataSetChanged()
    }

    fun removeFooter() {
        hasFooter = false
        notifyDataSetChanged()
    }

}