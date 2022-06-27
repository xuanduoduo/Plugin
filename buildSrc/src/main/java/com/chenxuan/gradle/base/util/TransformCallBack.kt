package com.chenxuan.gradle.base.util

fun interface TransformCallBack {
    fun process(className: String, classBytes: ByteArray?): ByteArray?
}