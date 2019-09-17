package com.iponkan.demo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.iponkan.loadmore_recyclerview.ItemTouchListener
import com.iponkan.loadmore_recyclerview.LoadMoreRecyclerView
import com.iponkan.loadmore_recyclerview.R
import com.iponkan.loadmore_recyclerview.i.ILoadMore
import com.sonicers.commonlib.component.BaseActivity
import com.sonicers.commonlib.util.ToastUtil
import java.util.*

class MainActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener, ILoadMore {
    private var refreshLayout: SwipeRefreshLayout? = null
    private var recyclerView: LoadMoreRecyclerView? = null
    private var list: MutableList<String>? = null

    private val PAGE_COUNT = 10//page size
    private var adapter: DemoAdapter? = null
    private val mHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findView()
        initData()
        initRefreshLayout()
    }

    private fun initData() {
        // init fake data
        list = ArrayList()
        for (i in 1..40) {
            list!!.add("条目$i")
        }
        loadDataStartFrom(true, 0)
    }

    private fun findView() {
        refreshLayout = findViewById(R.id.refreshLayout)
        recyclerView = findViewById(R.id.recyclerView)
        val resList = ArrayList<String>()
        adapter = DemoAdapter(resList, this, true)
        recyclerView!!.adapter = adapter
        recyclerView!!.setILoadMore(this)
        recyclerView!!.addOnItemTouchListener(ItemTouchListener(recyclerView!!, object : ItemTouchListener.SimpleOnItemClickListener() {
            override fun onItemClick(view: View?, position: Int) {
                ToastUtil.showToastShort(this@MainActivity, "position: $position")
            }
        }))
    }

    private fun initRefreshLayout() {
        refreshLayout!!.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light)
        refreshLayout!!.setOnRefreshListener(this)
    }

    /**
     * 请求数据
     *
     * @param init 是否是页面初始化
     * @param startIndex 请求数据起始位置
     */
    override fun loadDataStartFrom(init: Boolean, startIndex: Int) {
        recyclerView!!.isLoadMore = true
        if (init) {
            showLoadingDialog()
        }
        val resList = ArrayList<String>()
        val runnable = Runnable {
            run {
                for (i in startIndex until startIndex + PAGE_COUNT) {
                    if (i < list!!.size) {
                        resList.add(list!![i])
                    }
                }
                Thread.sleep(500)//模拟网络返回时间
                runOnUiThread {
                    dismissLoadingDialog()
                    recyclerView!!.isLoadMore = false
                    if (startIndex == 0) {// 若是下拉刷新，需要清空数据
                        adapter!!.resetDatas()
                    }
                    adapter!!.updateList(resList)
                }
            }
        }
        Thread(runnable).start()
    }

    /**
     * 下拉刷新
     */
    override fun onRefresh() {
        refreshLayout!!.isRefreshing = true
        loadDataStartFrom(false, 0)
        mHandler.postDelayed({ refreshLayout!!.isRefreshing = false }, 1000)
    }
}
