package com.circleVi.basekotlin.view.base

import android.content.Context
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import com.circleVi.basekotlin.utils.DebugLog
import com.circleVi.basekotlin.utils.permission.HandleResultFragment
import com.circleVi.basekotlin.utils.permission.Permission
import com.circleVi.basekotlin.utils.permission.PermissionUtil
import photoprinter.canon.com.photoprinter.scenes.base.BaseFragment
import photoprinter.canon.com.photoprinter.scenes.base.BaseTransitionFragment

abstract class BaseActivity : AppCompatActivity(), BaseView, BaseTransitionFragment {

    companion object {
        const private val MIN_BACK_STACK_ANIMATION = 4

        const private val MIN_NON_BACK_STACK_ANIMATION = 4
    }

    private lateinit var permissionUtil: PermissionUtil

    private var drawerLayout: DrawerLayout? = null

    private var drawer: NavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onAttachView()
        permissionUtil = PermissionUtil(this)
        setContentView(getLayoutId())
        initHandleResultFragment()
        initView()
        initData()
    }

    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(context)
        //TODO: attach locale base context
    }

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

    override fun requestPermissions(vararg permissions: String, callback: (areGrantedAll: Boolean, deniedPermissions: List<Permission>) -> Unit) {
        permissionUtil.request(*permissions, callback = callback)
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

    override fun onDestroy() {
        onDetachView()
        super.onDestroy()
    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun onError(throwable: Throwable) {

    }

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

    open fun openNavigationDrawer() {
        if (drawerLayout?.isDrawerOpen(drawer) == false) {
            drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            drawerLayout?.openDrawer(drawer)
        }
    }

    open fun closeNavigationDrawer() {
        drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN)
        drawerLayout?.closeDrawer(drawer)
    }

    open fun isNavigationDrawerOpened(): Boolean = false

    abstract fun getLayoutId(): Int

    open fun getContainerView() = 0

    open fun getDrawerLayoutId() = 0

    open fun getDrawerId() = 0

    open fun initView() {
        drawerLayout = findViewById(getDrawerLayoutId())
        drawer = findViewById(getDrawerId())
    }

    abstract fun initData()

    abstract fun onAttachView()

    abstract fun onDetachView()
}