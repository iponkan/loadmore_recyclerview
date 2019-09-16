package com.iponkan.loadmore_recyclerview

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LoadMoreRecyclerView : RecyclerView {

    private var lastVisibleItem = 0
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var iLoadMore: ILoadMore

    internal lateinit var loadMoreAdapter: LoadMoreAdapter

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
        loadMoreAdapter = adapter as LoadMoreAdapter
    }

    inner class LoadMoreScrollListener : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == SCROLL_STATE_IDLE) {
                if (!loadMoreAdapter.isFadeTips && lastVisibleItem + 1 == loadMoreAdapter.itemCount) {
                    iLoadMore.loadDataStartFrom(loadMoreAdapter.realLastPosition)
                }

                if (loadMoreAdapter.isFadeTips && lastVisibleItem + 2 == loadMoreAdapter.itemCount) {
                    iLoadMore.loadDataStartFrom(loadMoreAdapter.realLastPosition)
                }
            }
        }
    }

    fun updateRecyclerView(newDatas: List<String>) {
        if (newDatas.isNotEmpty()) {
            loadMoreAdapter.updateList(newDatas, true)
        } else {
            loadMoreAdapter.updateList(null, false)
        }
    }

    fun setILoadMore(iLoadMore: ILoadMore) {
        this.iLoadMore = iLoadMore
    }
}