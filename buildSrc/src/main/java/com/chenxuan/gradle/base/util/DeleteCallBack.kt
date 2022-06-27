package com.chenxuan.gradle.base.util

fun interface DeleteCallBack {
    fun delete(className: String, classBytes: ByteArray)
}