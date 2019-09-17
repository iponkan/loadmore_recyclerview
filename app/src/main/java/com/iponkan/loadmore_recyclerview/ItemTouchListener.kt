package com.iponkan.loadmore_recyclerview

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemTouchListener(recyclerView: RecyclerView,
                        private val clickListener: OnItemClickListener?) : RecyclerView.SimpleOnItemTouchListener() {
    private val gestureDetector: GestureDetector

    interface OnItemClickListener {

        fun onItemClick(view: View?, position: Int)

        fun onItemLongClick(view: View?, position: Int)
    }

    init {
        gestureDetector = GestureDetector(recyclerView.context,
                object : GestureDetector.SimpleOnGestureListener() {
                    //click
                    override fun onSingleTapUp(e: MotionEvent): Boolean {
                        val childView = recyclerView.findChildViewUnder(e.x, e.y)
                        if (childView != null && clickListener != null) {
                            clickListener.onItemClick(childView, recyclerView.getChildLayoutPosition(childView))
                            return true
                        }
                        return false
                    }

                    //long click
                    override fun onLongPress(e: MotionEvent) {
                        val childView = recyclerView.findChildViewUnder(e.x, e.y)
                        if (childView != null && clickListener != null) {
                            clickListener.onItemLongClick(childView, recyclerView.getChildLayoutPosition(childView))
                        }
                    }
                })
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(e)
        return false
    }

    open class SimpleOnItemClickListener : OnItemClickListener {

        override fun onItemClick(view: View?, position: Int) {
            //
        }

        override fun onItemLongClick(view: View?, position: Int) {
            //
        }
    }
}
