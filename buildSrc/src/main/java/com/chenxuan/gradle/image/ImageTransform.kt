package com.chenxuan.gradle.image

import com.chenxuan.gradle.base.AbsTransform

class ImageTransform : AbsTransform() {

    override fun getName() = "ImageTransform"

    override fun getAsmHandler() = ImageAsmHandler()
}