package com.chenxuan.gradle.base

import java.io.IOException

interface AsmHandler {
    @Throws(IOException::class)
    fun modifyClass(srcClass: ByteArray): ByteArray
}