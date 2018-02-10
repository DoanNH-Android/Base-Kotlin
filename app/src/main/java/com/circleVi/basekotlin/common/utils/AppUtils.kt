package com.circleVi.basekotlin.common.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager

@SuppressLint("StaticFieldLeak")
object AppUtils {

    private lateinit var context: Context

    fun setup(context: Context) {
        this.context = context
    }

    fun packageName(): String {
        return context.packageName
    }

    fun versionCode(): Int {
        val mgr = context.packageManager
        val info = mgr.getPackageInfo(context.packageName, PackageManager.GET_META_DATA)
        return info.versionCode
    }

    fun versionName(): String {
        val mgr = context.packageManager
        val info = mgr.getPackageInfo(context.packageName, PackageManager.GET_META_DATA)
        return info.versionName
    }

    fun getString(resId: Int) : String {
        return context.getString(resId)
    }

    fun getInteger(resId: Int) : Int {
        return context.resources.getInteger(resId)
    }
}
