# 摘要 #

一个RecyclerView实现上拉加载和下拉加载功能，参考了 [ryanlijianchang](https://github.com/ryanlijianchang/PullToLoadData-RecyclerView)

## 特点

- 支持上拉加载和下拉加载
- 下拉加载可选，添加`SwipeRefreshLayout`即可支持

- Kotlin
- 兼容首页数据加载
- 基本Adapter剥离数据结构
- 上拉加载防止重复请求

# 使用方法

## 布局

```xml
    <androidx.swiperefreshlayout.widget.SwipeRefreshLayout
        android:id="@+id/refreshLayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <com.iponkan.loadmore_recyclerview.LoadMoreRecyclerView
            android:id="@+id/recyclerView"
            android:layout_width="match_parent"
            android:layout_height="match_parent"/>

    </androidx.swiperefreshlayout.widget.SwipeRefreshLayout>
```

如果需要下拉刷新需添加SwipeRefreshLayout，不使用可以自行去掉

## 代码部分

### 实现`ILoadMore`接口

```kotlin
interface ILoadMore {
    fun loadDataStartFrom(init: Boolean, startIndex: Int)
}
```

### `loadDataStartFrom`方法

`loadDataStartFrom`是请求数据的方法(通常是网络请求)，看官们可以自己定义。

通常的话第一次进入页面也需要请求一次首页数据，详细可以参见demo

```kotlin
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
                    updateData(resList)
                }
            }
        }
        Thread(runnable).start()
    }
```

### 继承LoadMoreAdapter

```kotlin
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
```

详细使用可以参见[demo](https://github.com/iponkan/loadmore_recyclerview/tree/master/app/src/main/java/com/iponkan/demo)



# License

```
MIT License

Copyright (c) 2019 sonicers

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```