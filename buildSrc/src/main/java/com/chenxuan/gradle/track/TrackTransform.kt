package com.chenxuan.gradle.track

import com.chenxuan.gradle.base.AbsTransform

class TrackTransform : AbsTransform() {

    override fun getName() = "TrackTransform"

    override fun getAsmHandler() = TrackAsmHandler()
}