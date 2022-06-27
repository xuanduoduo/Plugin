package com.chenxuan.gradle.sample

import com.chenxuan.gradle.base.AbsTransform

class SampleTransform : AbsTransform() {

    override fun getName() = "SampleTransform"

    override fun getAsmHandler() = SampleAsmHandler()
}