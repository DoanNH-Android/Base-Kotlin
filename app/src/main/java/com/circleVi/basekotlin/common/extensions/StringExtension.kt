package com.circleVi.basekotlin.common.extensions

import java.security.MessageDigest

fun String.sha256() : String {
    val digest = MessageDigest.getInstance("SHA-256")
    val result = digest.digest(toByteArray())

    val sb = StringBuilder(2 * result.size)
    for (b in result) {
        sb.append(String.format("%02x", b))
    }

    return sb.toString()
}