package com.iponkan.loadmore_recyclerview

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sonicers.commonlib.component.BaseActivity
import java.util.*

class MainActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener, ILoadMore {
    private var refreshLayout: SwipeRefreshLayout? = null
    private var recyclerView: LoadMoreRecyclerView? = null
    private var list: MutableList<String>? = null

    private val PAGE_COUNT = 10//分页大小
    private var adapter: LoadMoreAdapter? = null
    private val mHandler = Handler(Looper.getMainLooper())
    var requestData = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findView()
        initData()
        initRefreshLayout()
    }

    private fun initData() {
        list = ArrayList()
        for (i in 1..40) {
            list!!.add("条目$i")
        }
        requestData(true, 0)
    }

    private fun requestData(init: Boolean, startIndex: Int) {
        if (requestData) {
            return
        }
        if (init) {
            showLoadingDialog()
        }
        requestData = true
        val resList = ArrayList<String>()
        val runnable = Runnable {
            run {
                for (i in startIndex until startIndex + PAGE_COUNT) {
                    if (i < list!!.size) {
                        resList.add(list!![i])
                    }
                }
                Thread.sleep(500)
                runOnUiThread {
                    dismissLoadingDialog()
                    requestData = false
                    if (init) {
                        adapter = LoadMoreAdapter(resList, this, true)
                        recyclerView!!.adapter = adapter
                        recyclerView!!.setILoadMore(this)
                    } else {
                        if (startIndex == 0) {
                            adapter!!.resetDatas()
                        }
                        recyclerView!!.updateRecyclerView(resList)
                    }
                }
            }
        }
        Thread(runnable).start()

    }

    private fun findView() {
        refreshLayout = findViewById(R.id.refreshLayout)
        recyclerView = findViewById(R.id.recyclerView)
    }

    private fun initRefreshLayout() {
        refreshLayout!!.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light)
        refreshLayout!!.setOnRefreshListener(this)
    }

    override fun loadDataStartFrom(startIndex: Int) {
        requestData(false, startIndex)
    }

    override fun onRefresh() {
        refreshLayout!!.isRefreshing = true
        requestData(false, 0)
        mHandler.postDelayed({ refreshLayout!!.isRefreshing = false }, 1000)
    }
}
