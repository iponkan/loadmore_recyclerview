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
    private val mHandler = Handler(Looper.getMainLooper())
    private var hasDrag = false

    override fun realLastPosition(): Int {
        return datas!!.size
    }

    override fun hasMore(): Boolean {
        if (!hasDrag) {
            hasDrag = true
        }
        return hasMore
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
            val footHolder = holder as LoadMoreAdapter<*>.FootHolder
            if (hasMore) {
                if (datas!!.size > 0) {
                    footHolder.tips.text = "正在加载更多... (๑•̀ㅂ•́)و✧"
                    footHolder.tips.visibility = View.VISIBLE
                }
            } else {
                if (datas!!.size > 0) {
                    if (hasDrag) {
                        footHolder.tips.text = "没有更多数据了( ˃᷄˶˶̫˶˂᷅ ) "
                        footHolder.tips.visibility = View.VISIBLE
                        mHandler.postDelayed({
                            footHolder.tips.visibility = View.GONE
                        }, 500)

                    }
                }
            }
        }
    }

    abstract fun onCreateNormalViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    abstract fun onBindNormalViewHolder(holder: RecyclerView.ViewHolder, position: Int)

    override fun getItemCount(): Int {
        return datas!!.size + 1
    }

    /**
     * @param hasMore 更新数据后是否还支持上拉加载
     */
    fun updateList(newDatas: List<T>?, hasMore: Boolean) {
        if (newDatas != null) {
            datas!!.addAll(newDatas)
        }
        this.hasMore = hasMore
        notifyDataSetChanged()
    }

    /**
     * @param newDatas 若列表为空自动认为没有更多数据
     */
    fun updateList(newDatas: List<T>) {
        if (newDatas.isNotEmpty()) {
            updateList(newDatas, true)
        } else {
            updateList(null, false)
        }
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
