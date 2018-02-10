package com.circleVi.basekotlin.view.base

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.circleVi.basekotlin.R
import com.circleVi.basekotlin.common.utils.permission.Permission
import kotlinx.android.synthetic.main.fragment_base.*

abstract class BaseFragment : Fragment(), BaseView, BaseTransitionFragment {

    protected val activityParent: BaseActivity?
        get() = activity as? BaseActivity

    //region implement Fragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        onAttachView()

        val baseView = inflater.inflate(R.layout.fragment_base, container, false)
        val contentView = inflater.inflate(getLayoutId(), null)
        baseView.findViewById<FrameLayout>(R.id.containerBase).addView(contentView)

        return baseView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBaseView()
        initView()
        initData(arguments)
    }

    override fun onDestroyView() {
        onDetachView()
        super.onDestroyView()
    }
    //endregion implement Fragment

    //region implement BaseView
    override fun showLoading() {
        activityParent?.showLoading()
    }

    override fun hideLoading() {
        activityParent?.hideLoading()
    }

    override fun onError(throwable: Throwable) {
        activityParent?.onError(throwable)
    }

    override fun showSoftKeyboard() {
        activityParent?.showSoftKeyboard()
    }

    override fun hideSoftKeyboard() {
        activityParent?.hideSoftKeyboard()
    }

    override fun requestPermissions(vararg permissions: String, callback: (areGrantedAll: Boolean, deniedPermissions: List<Permission>) -> Unit) {
        activityParent?.requestPermissions(*permissions, callback = callback)
    }
    //endregion implement BaseView

    //region implement BaseTransitionFragment
    override fun popBackStack() {
        activityParent?.popBackStack()
    }

    override fun pushFragment(fragment: Fragment, withAnimation: Boolean, animations: IntArray?) {
        activityParent?.pushFragment(fragment, withAnimation, animations)
    }

    override fun replaceFragment(fragment: Fragment, withAnimation: Boolean, animations: IntArray?) {
        activityParent?.replaceFragment(fragment, withAnimation, animations)
    }
    //endregion implement BaseTransitionFragment

    //region implement private/public method
    private fun initBaseView() {
        setupSwipeRefresh()
        val visibleToolbar = setupToolbar()
        toolbarBase.visibility = if (visibleToolbar) View.VISIBLE else View.GONE
    }

    private fun setupSwipeRefresh() {
        swipeRefreshBase.isEnabled = isRefreshable()
        if (swipeRefreshBase.isEnabled) {
            swipeRefreshBase.setOnRefreshListener {
                onPullRefresh()
            }
        }
    }

    private fun setupToolbar(): Boolean {
        val title = getTitleScreen()
        when (title) {
            is String -> toolbarBase.title = title
            is Int -> toolbarBase.setTitle(title)
            null -> toolbarBase.title = ""
        }

        val icon = getNavigationIcon()

        if (icon != null) {
            toolbarBase.setNavigationIcon(icon)
        }

        val menu = getToolbarMenu()
        if (menu != null) {
            toolbarBase.inflateMenu(menu)
            toolbarBase.setOnMenuItemClickListener { menuItem ->
                return@setOnMenuItemClickListener onMenuItemClicked(menuItem)
            }
        }

        toolbarBase.setNavigationOnClickListener { onNavigationIconClicked() }

        toolbarBase.setBackgroundColor(getToolbarBackground())
        return isShowToolbar()
    }
    //endregion implement private/public method

    //region implement override-able method
    open fun onBackPressed(): Boolean = false

    open fun isRefreshable(): Boolean = false

    open fun onPullRefresh() {}

    open fun isShowToolbar(): Boolean = true

    open fun onMenuItemClicked(item: MenuItem): Boolean = false

    open fun onNavigationIconClicked() {}

    open fun getTitleScreen(): Any? = null

    open fun getNavigationIcon(): Int? = null

    open fun getToolbarMenu(): Int? = null

    open fun getToolbarBackground(): Int = Color.WHITE

    abstract fun getLayoutId(): Int

    abstract fun initView()

    abstract fun initData(argument: Bundle?)

    abstract fun onAttachView()

    abstract fun onDetachView()
    //endregion implement private/public method
}