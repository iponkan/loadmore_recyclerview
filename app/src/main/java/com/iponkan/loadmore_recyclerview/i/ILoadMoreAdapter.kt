package com.iponkan.loadmore_recyclerview.i

/**
 * Adapter需实现的控制接口
 */
interface ILoadMoreAdapter {
    fun hasMore(): Boolean
    fun realLastPosition(): Int
}
