package com.iponkan.demo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iponkan.loadmore_recyclerview.LoadMoreAdapter
import com.iponkan.loadmore_recyclerview.R

class DemoAdapter(datas: MutableList<String>?, context: Context, hasMore: Boolean) : LoadMoreAdapter<String>(datas, context, hasMore) {

    override fun onCreateNormalViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NormalHolder(LayoutInflater.from(context).inflate(R.layout.item, parent, false))
    }

    override fun onBindNormalViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NormalHolder) {
            holder.textView.text = datas!![position]
        }
    }

    internal inner class NormalHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.tv)

    }
}