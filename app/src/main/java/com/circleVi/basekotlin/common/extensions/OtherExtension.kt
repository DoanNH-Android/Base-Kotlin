package com.circleVi.basekotlin.common.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.webkit.MimeTypeMap

fun Context.isConnected(): Boolean {
    val cm: ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val netInfo: NetworkInfo? = cm.activeNetworkInfo
    return netInfo != null && netInfo.isConnected
}

fun Uri.mimeType(context: Context): String {
    var ret: String? = null

    if (this.scheme == "content") {
        ret = context.contentResolver.getType(this)
    } else if (this.scheme == "file") {
        ret = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(this.path).toLowerCase())
    }

    return ret ?: "*/*"
}