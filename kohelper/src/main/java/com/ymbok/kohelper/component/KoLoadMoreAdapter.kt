package com.ymbok.kohelper.component

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ymbok.kohelper.databinding.KoItemMoreBinding

/**
 * Copyright ymbok.com
 * Author 还如一梦中
 * Date 2022/4/2 09:13
 * Email 396196516@qq.com
 * Info RecyclerView LoadStateAdapter加载更多
 */
class KoLoadMoreAdapter(private val context : Context): LoadStateAdapter<KoLoadMoreAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val binding = KoItemMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(binding: KoItemMoreBinding) : RecyclerView.ViewHolder(binding.root) {
        val loadText: TextView = binding.itemLoadingText
    }
}