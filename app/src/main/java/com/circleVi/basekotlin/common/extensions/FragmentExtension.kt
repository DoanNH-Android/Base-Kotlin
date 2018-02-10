package com.circleVi.basekotlin.common.extensions

import android.content.Intent
import android.provider.MediaStore
import android.support.v4.app.Fragment

/**
 * Open gallery to choice only display image or only display video
 * @param isOpenVideo true open video, false open Image
 * @param requestCode code requestCode
 */
fun Fragment.openGallery(isOpenVideo: Boolean = false, requestCode: Int) {
    val intent: Intent
    if (isOpenVideo) {
        intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        intent.type = "video/*"
    } else {
        intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
    }
    if (intent.resolveActivity(this.activity?.packageManager) != null) {
        startActivityForResult(intent, requestCode)
    }
}