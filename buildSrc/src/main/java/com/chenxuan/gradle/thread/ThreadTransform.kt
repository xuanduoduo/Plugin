package com.chenxuan.gradle.thread

import com.chenxuan.gradle.base.AbsTransform

class ThreadTransform : AbsTransform() {

    override fun getName() = "ThreadTransform"

    override fun getAsmHandler() = ThreadAsmHandler()
}