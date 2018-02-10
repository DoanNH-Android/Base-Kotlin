package com.circleVi.basekotlin.common.extensions

import android.content.Context
import android.graphics.Rect
import android.net.Uri
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.TouchDelegate
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.circleVi.basekotlin.common.widget.EndlessScrollListener

/**
 * The method to expand click area of view
 */
fun View.expandClickArea(left: Int = 0,
                         top: Int = 0,
                         right: Int = 0,
                         bottom: Int = 0) {

    if (left == 0 && top == 0 && right == 0 && bottom == 0) {
        return
    }
    (this.parent as? View)?.post {
        val rect = Rect()
        this.getHitRect(rect)
        rect.left += left
        rect.top += top
        rect.right += right
        rect.bottom += bottom

        val touchDelegate = TouchDelegate(rect, this)
        if (View::class.java.isInstance(this.parent)) {
            (this.parent as View).touchDelegate = touchDelegate
        }
    }
}

fun RecyclerView.setupLoadEndless(onScrolled: ((dx: Int, dy: Int) -> Unit)? = null,
                                  onStateChanged: ((newState: Int) -> Unit)? = null,
                                  onLoadMore: (() -> Unit)? = null): EndlessScrollListener {

    val layoutManger = layoutManager as LinearLayoutManager
    val onScrollListener = object : EndlessScrollListener(layoutManger) {
        override fun onScrolled(dx: Int, dy: Int) {
            onScrolled?.invoke(dx, dy)
        }

        override fun onStateChanged(newState: Int) {
            onStateChanged?.invoke(newState)
        }

        override fun onLoadMore() {
            onLoadMore?.invoke()
        }
    }

    addOnScrollListener(onScrollListener)

    return onScrollListener
}

fun ImageView.loadImage(uri: Uri?, placeHolder: Int, error: Int, fallback: Int) {
    Glide.with(this.context)
            .load(uri)
            .apply(RequestOptions()
                    .placeholder(placeHolder)
                    .error(error)
                    .fallback(fallback))
            .into(this)
}

fun ImageView.loadImage(path: String?, placeHolder: Int, error: Int, fallback: Int) {
    Glide.with(this.context)
            .load(path)
            .apply(RequestOptions()
                    .placeholder(placeHolder)
                    .error(error)
                    .fallback(fallback))
            .into(this)
}