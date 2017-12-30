package photoprinter.canon.com.photoprinter.scenes.base

import java.lang.ref.WeakReference


abstract class BasePresenter<V> {
    private val isViewAttached: Boolean
        get() = weakView != null && weakView?.get() != null

    internal val view: V?
        get() = weakView?.get()

    private var weakView: WeakReference<V>? = null

    fun attachView(view: V) {
        if (!isViewAttached) {
            weakView = WeakReference(view)
        }
    }

    open fun detachView() {
        weakView?.clear()
        weakView = null
    }
}