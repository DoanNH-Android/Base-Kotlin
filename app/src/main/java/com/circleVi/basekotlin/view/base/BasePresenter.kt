package com.circleVi.basekotlin.view.base

import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference


abstract class BasePresenter<V> {

    private val isViewAttached: Boolean
        get() = weakView != null && weakView?.get() != null

    internal val view: V?
        get() = weakView?.get()

    private var weakView: WeakReference<V>? = null
    private val disposables: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    fun attachView(view: V) {
        if (!isViewAttached) {
            weakView = WeakReference(view)
        }
    }

    open fun detachView() {
        disposables.clear()
        weakView?.clear()
        weakView = null
    }
}