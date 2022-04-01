package com.ymbok.kohelper.component

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ymbok.kohelper.databinding.KoItemMoreBinding

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