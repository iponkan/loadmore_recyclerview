package com.iponkan.loadmore_recyclerview

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LoadMoreAdapter(private var datas: MutableList<String>?, private val context: Context, private var hasMore: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val normalType = 0
    private val footType = 1
    var isFadeTips = false
    private val mHandler = Handler(Looper.getMainLooper())

    val realLastPosition: Int
        get() = datas!!.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == normalType) {
            NormalHolder(LayoutInflater.from(context).inflate(R.layout.item, parent, false))
        } else {
            FootHolder(LayoutInflater.from(context).inflate(R.layout.footview, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NormalHolder) {
            holder.textView.text = datas!![position]
        } else {
            (holder as FootHolder).tips.visibility = View.VISIBLE
            if (hasMore) {
                isFadeTips = false
                if (datas!!.size > 0) {
                    holder.tips.text = "正在加载更多... (๑•̀ㅂ•́)و✧"
                }
            } else {
                if (datas!!.size > 0) {
                    holder.tips.text = "没有更多数据了( ˃᷄˶˶̫˶˂᷅ ) "
                    mHandler.postDelayed({
                        holder.tips.visibility = View.GONE
                        isFadeTips = true
                        hasMore = true
                    }, 500)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return datas!!.size + 1
    }

    fun updateList(newDatas: List<String>?, hasMore: Boolean) {
        if (newDatas != null) {
            datas!!.addAll(newDatas)
        }
        this.hasMore = hasMore
        notifyDataSetChanged()
    }

    internal inner class NormalHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.tv)

    }

    internal inner class FootHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tips: TextView = itemView.findViewById(R.id.tips)

    }

    fun resetDatas() {
        datas!!.clear()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) {
            footType
        } else {
            normalType
        }
    }
}
