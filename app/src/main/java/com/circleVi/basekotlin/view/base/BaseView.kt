package com.circleVi.basekotlin.view.base

import com.circleVi.basekotlin.common.utils.permission.Permission

interface BaseView {

    fun showLoading()

    fun hideLoading()

    fun onError(throwable: Throwable)

    fun showSoftKeyboard()

    fun hideSoftKeyboard()

    fun requestPermissions(vararg permissions: String, callback: (areGrantedAll: Boolean, deniedPermissions: List<Permission>) -> Unit)
}