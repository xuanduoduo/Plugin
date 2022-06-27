package com.chenxuan.gradle.image

import com.chenxuan.gradle.base.AbsAsmHandler
import org.objectweb.asm.ClassWriter

class ImageAsmHandler : AbsAsmHandler() {

    override fun getClassVisitor(classWriter: ClassWriter) = ImageClassVisitor(classWriter)
}