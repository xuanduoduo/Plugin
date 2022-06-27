package com.chenxuan.gradle.catch

import com.chenxuan.gradle.base.AbsTransform

class CatchTransform : AbsTransform() {

    override fun getName() = "CatchTransform"

    override fun getAsmHandler() = CatchAsmHandler()
}