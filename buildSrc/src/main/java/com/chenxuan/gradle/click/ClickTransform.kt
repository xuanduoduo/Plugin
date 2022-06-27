package com.chenxuan.gradle.click

import com.chenxuan.gradle.base.AbsTransform

class ClickTransform : AbsTransform() {

    override fun getName() = "ClickTransform"

    override fun getAsmHandler() = ClickAsmHandler()
}