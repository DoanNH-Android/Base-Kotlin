package com.circleVi.basekotlin.common.widget


import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

abstract class EndlessScrollListener(private val layoutManager: LinearLayoutManager,
                                     private val visibleThreshold: Int = 1) : RecyclerView.OnScrollListener() {
    private var isLoading = true

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        onScrolled(dx,dy)

        if (dy <= 0) {
            return
        }

        val totalItemCount = layoutManager.itemCount
        val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
        if (!isLoading && totalItemCount <= lastVisibleItem + visibleThreshold && totalItemCount > visibleThreshold) {
            onLoadMore()
            isLoading = true
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        onStateChanged(newState)
    }

    fun setLoaded() {
        isLoading = false
    }

    open fun onScrolled(dx: Int, dy: Int) {
        //implement
    }

    open fun onStateChanged(newState: Int) {
        //implement
    }

    open fun onLoadMore() {
        //implement
    }
}