package com.circleVi.basekotlin.common.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.media.MediaMetadataRetriever
import android.net.Uri
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

object ImageUtils {

    fun getThumbnailVideo(context: Context, uri: Uri): Bitmap? {
        var retriever: MediaMetadataRetriever? = null
        val bitmap: Bitmap?

        try {
            retriever = MediaMetadataRetriever()
            retriever.setDataSource(context, uri)

            bitmap = retriever.getFrameAtTime(0)
        } finally {
            retriever?.release()
        }

        return bitmap
    }

    /**
     * https://developer.android.com/topic/performance/graphics/load-bitmap.html
     * to use exif interface with API lower than 24
     *     compile "com.android.support:exifinterface:25.3.1"
     */
    @SuppressLint("NewApi")
    fun createBitmap(context: Context, uri: Uri, maxHeight: Int, maxWidth: Int, png: Boolean): Bitmap {
        // オリジナル画像サイズを取得
        var stream = openInputStream(uri, context)
        val boundsOpts = BitmapFactory.Options()
        boundsOpts.inJustDecodeBounds = true
        BitmapFactory.decodeStream(stream, null, boundsOpts)
        stream.close()

        // 目的のサイズより大きめの値を設定
        // デコードの高速化のため2のべき乗を指定
        var inSampleSize = 1
        while ((boundsOpts.outWidth / (inSampleSize * 2)) > maxWidth || (boundsOpts.outHeight / (inSampleSize * 2)) > maxHeight) {
            inSampleSize *= 2
        }

        // 目的サイズより大きめのBitmapを生成
        val opts = BitmapFactory.Options()
        opts.inSampleSize = inSampleSize
        opts.inJustDecodeBounds = false
        opts.inPreferredConfig = if (png) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565

        stream = openInputStream(uri, context)
        val bis = BufferedInputStream(stream)
        val tmp1 = BitmapFactory.decodeStream(bis, null, opts)
        bis.close()
        stream.close()

        // 目的のサイズに変換
        val ratio = Math.max(tmp1.width.toDouble() / maxWidth.toDouble(), tmp1.height.toDouble() / maxWidth.toDouble())
        val desiredWidth = (tmp1.width / ratio).toInt()
        val desiredHeight = (tmp1.height / ratio).toInt()

        val tmp2 = Bitmap.createScaledBitmap(tmp1, desiredWidth, desiredHeight, true)
        tmp1.recycle()

        //画像の回転を補正
        stream = openInputStream(uri, context)
        val exif = ExifInterface(BufferedInputStream(stream))
        val exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        stream.close()

        var degrees = 0f
        when (exifOrientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> degrees = 90f
            ExifInterface.ORIENTATION_ROTATE_180 -> degrees = 180f
            ExifInterface.ORIENTATION_ROTATE_270 -> degrees = 270f
        }

        val ret = rotateBitmap(tmp2, degrees)
        tmp2.recycle()

        return ret
    }

    private fun openInputStream(uri: Uri, context: Context): InputStream {
        return when {
            uri.scheme == "content" -> {
                context.contentResolver.openInputStream(uri)
            }
            uri.scheme == "file" -> {
                val file = File(uri.path)
                FileInputStream(file)
            }
            else -> {
                throw IllegalArgumentException()
            }
        }
    }

    fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}