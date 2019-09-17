package com.iponkan.loadmore_recyclerview

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iponkan.loadmore_recyclerview.i.ILoadMoreAdapter

abstract class LoadMoreAdapter<T>(protected var datas: MutableList<T>?, protected val context: Context, protected var hasMore: Boolean)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(), ILoadMoreAdapter {
    protected val normalType = 0
    protected val footType = 1
    var isFadeTips = false
    private val mHandler = Handler(Looper.getMainLooper())

    override fun realLastPosition(): Int {
        return datas!!.size
    }

    override fun fadeTips(): Boolean {
        return isFadeTips
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == normalType) {
            return onCreateNormalViewHolder(parent, viewType)
        } else {
            FootHolder(LayoutInflater.from(context).inflate(R.layout.footview, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == normalType) {
            onBindNormalViewHolder(holder, position)
        } else {
            (holder as LoadMoreAdapter<*>.FootHolder).tips.visibility = View.VISIBLE
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

    abstract fun onCreateNormalViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    abstract fun onBindNormalViewHolder(holder: RecyclerView.ViewHolder, position: Int)

    override fun getItemCount(): Int {
        return datas!!.size + 1
    }

    fun updateList(newDatas: List<T>?, hasMore: Boolean) {
        if (newDatas != null) {
            datas!!.addAll(newDatas)
        }
        this.hasMore = hasMore
        notifyDataSetChanged()
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
