package com.circleVi.basekotlin.view.base

import android.content.Context
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import com.circleVi.basekotlin.common.utils.DebugLog
import com.circleVi.basekotlin.common.utils.permission.HandleResultFragment
import com.circleVi.basekotlin.common.utils.permission.Permission
import com.circleVi.basekotlin.common.utils.permission.PermissionUtil

abstract class BaseActivity : AppCompatActivity(), BaseView, BaseTransitionFragment {

    companion object {
        const private val MIN_BACK_STACK_ANIMATION = 4
        const private val MIN_NON_BACK_STACK_ANIMATION = 4
    }

    private val permissionUtil: PermissionUtil by lazy {
        PermissionUtil(this)
    }
    private var drawerLayout: DrawerLayout? = null
    private var drawer: NavigationView? = null

    //region implement AppCompatActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onAttachView()
        setContentView(getLayoutId())
        initHandleResultFragment()
        initView()
        initData()
    }

    @Suppress("RedundantOverride")
    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(context)
        //TODO: attach locale base context
    }

    override fun onDestroy() {
        onDetachView()
        super.onDestroy()
    }

    override fun onBackPressed() {
        val fm = supportFragmentManager
        if (fm.backStackEntryCount > 0) {
            fm.fragments.forEach { childFragment ->
                if (childFragment is BaseFragment) {
                    if (childFragment.onBackPressed()) {
                        return
                    }
                }
            }
            fm.popBackStack()
        } else {
            finish()
        }
    }
    //endregion implement AppCompatActivity

    //region implement BaseView
    override fun showLoading() {
        //do nothing
    }

    override fun hideLoading() {
        //do nothing
    }

    override fun onError(throwable: Throwable) {
        //do nothing
    }

    override fun showSoftKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    override fun hideSoftKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (currentFocus != null) {
            imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }

    override fun requestPermissions(vararg permissions: String, callback: (areGrantedAll: Boolean, deniedPermissions: List<Permission>) -> Unit) {
        permissionUtil.request(*permissions, callback = callback)
    }
    //endregion implement BaseView

    //region implement BaseTransitionFragment
    override fun popBackStack() {
        val fm = supportFragmentManager
        if (fm.backStackEntryCount > 0) {
            fm.popBackStack()
        } else {
            finish()
        }
    }

    override fun pushFragment(fragment: Fragment, withAnimation: Boolean, animations: IntArray?) {
        closeNavigationDrawer()
        showFragment(fragment, true, withAnimation, animations)
    }

    override fun replaceFragment(fragment: Fragment, withAnimation: Boolean, animations: IntArray?) {
        closeNavigationDrawer()
        showFragment(fragment, false, withAnimation, animations)
    }
    //endregion implement BaseTransitionFragment

    //region implement private/public method
    private fun initHandleResultFragment() {
        var fragment = supportFragmentManager.findFragmentByTag(HandleResultFragment.TAG)
        if (fragment == null) {
            fragment = HandleResultFragment()
            supportFragmentManager
                    .beginTransaction()
                    .add(fragment, HandleResultFragment.TAG)
                    .commitAllowingStateLoss()
        }
    }

    private fun showFragment(fragment: Fragment, hasAddBackStack: Boolean, withAnimation: Boolean, animations: IntArray?) {
        if (getContainerView() == 0) {
            DebugLog.e("The activity don't provide container view to store fragment")
            return
        }

        val transaction = supportFragmentManager
                .beginTransaction()
                .replace(getContainerView(), fragment)
        if (hasAddBackStack) {
            transaction.addToBackStack(null)
        }

        if (withAnimation && animations != null) {
            if (hasAddBackStack) {
                if (animations.size < MIN_BACK_STACK_ANIMATION) throw IllegalArgumentException("You must provide at least 4 animation")
                transaction.setCustomAnimations(animations[0], animations[1], animations[2], animations[3])
            } else {
                if (animations.size < MIN_NON_BACK_STACK_ANIMATION) throw IllegalArgumentException("You must provide at least 2 animation")
                transaction.setCustomAnimations(animations[0], animations[1])
            }
        }

        transaction.commitAllowingStateLoss()
    }
    //endregion implement private/public method

    //region implement override-able method
    open fun openNavigationDrawer() {
        if (drawer != null && drawerLayout?.isDrawerOpen(drawer!!) == false) {
            drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            drawerLayout?.openDrawer(drawer!!)
        }
    }

    open fun closeNavigationDrawer() {
        drawer?.apply {
            drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN)
            drawerLayout?.closeDrawer(drawer!!)
        }
    }

    open fun initData() {
        //do nothing
    }

    open fun isNavigationDrawerOpened(): Boolean = false

    open fun getContainerView() = 0

    open fun getDrawerLayoutId() = 0

    open fun getDrawerId() = 0

    open fun initView() {
        drawerLayout = findViewById(getDrawerLayoutId())
        drawer = findViewById(getDrawerId())
    }

    abstract fun getLayoutId(): Int

    abstract fun onAttachView()

    abstract fun onDetachView()
    //endregion implement override-able method
}