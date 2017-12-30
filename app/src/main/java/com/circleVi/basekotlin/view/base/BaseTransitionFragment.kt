package photoprinter.canon.com.photoprinter.scenes.base

import android.support.v4.app.Fragment


interface BaseTransitionFragment {
    fun popBackStack()

    fun pushFragment(fragment: Fragment, withAnimation: Boolean = false, animations: IntArray? = null)

    fun replaceFragment(fragment: Fragment, withAnimation: Boolean = false, animations: IntArray? = null)
}