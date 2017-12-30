package com.circleVi.basekotlin.utils.permission

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment


class HandleResultFragment : Fragment() {
    companion object {
        private const val PERMISSION_REQUEST_CODE = 1
        val TAG = HandleResultFragment.toString()
    }

    private val mRequestSubjects: HashMap<String, (Permission) -> Unit> = HashMap()

    var shareResultCallback: ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun request(permissions: Array<out String>) {
        requestPermissions(permissions, PERMISSION_REQUEST_CODE)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            permissions.forEachIndexed { index, permission ->
                val permissionSubject = mRequestSubjects[permission]
                val permissionResult = if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                    Permission(permission, true, false)
                } else {
                    Permission(permission, false, !shouldShowRequestPermissionRationale(permission))
                }
                permissionSubject?.invoke(permissionResult)
                mRequestSubjects.remove(permission)
            }
        }
    }

    fun addPermissionSubject(permission: String, subject: (Permission) -> Unit) {
        mRequestSubjects.put(permission, subject)
    }

    fun getPermissionSubject(permission: String): ((Permission) -> Unit)? = mRequestSubjects[permission]

    @RequiresApi(Build.VERSION_CODES.M)
    fun isGranted(permission: String): Boolean =
            activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        shareResultCallback?.invoke(requestCode, resultCode, data)
    }
}