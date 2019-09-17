package com.iponkan.loadmore_recyclerview

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iponkan.loadmore_recyclerview.i.ILoadMore
import com.iponkan.loadmore_recyclerview.i.ILoadMoreAdapter

/**
 * 实现上拉加载更多的RecyclerView
 *
 */
class LoadMoreRecyclerView : RecyclerView {

    private var lastVisibleItem = 0
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var iLoadMore: ILoadMore
    private lateinit var iLoadMoreAdapter: ILoadMoreAdapter
    var isLoadMore = false

    constructor(context: Context) : this(context, null) {
        //
    }

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0) {
        //
    }

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        init()
    }


    private fun init() {
        linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = VERTICAL
        layoutManager = linearLayoutManager
        itemAnimator = DefaultItemAnimator()
        addOnScrollListener(LoadMoreScrollListener())
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
        iLoadMoreAdapter = adapter as ILoadMoreAdapter
    }

    inner class LoadMoreScrollListener : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == SCROLL_STATE_IDLE) {
                if (isLoadMore) {
                    return
                }
                if ((iLoadMoreAdapter.hasMore() && lastVisibleItem + 1 == adapter!!.itemCount)) {
                    iLoadMore.loadDataStartFrom(false, iLoadMoreAdapter.realLastPosition())
                }
            }
        }
    }

    fun setILoadMore(iLoadMore: ILoadMore) {
        this.iLoadMore = iLoadMore
    }
}