package com.circleVi.basekotlin.utils

import android.graphics.Rect
import android.view.TouchDelegate
import android.view.View

/**
 * The method to expand click area of view
 */
fun View.expandClickArea(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
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