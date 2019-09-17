package com.iponkan.loadmore_recyclerview.i

/**
 * 加载更多数据接口
 */
interface ILoadMore {
    fun loadDataStartFrom(init: Boolean, startIndex: Int)
}