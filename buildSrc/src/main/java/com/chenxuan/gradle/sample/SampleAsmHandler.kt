package com.chenxuan.gradle.sample

import com.chenxuan.gradle.base.AbsAsmHandler
import org.objectweb.asm.ClassWriter

class SampleAsmHandler : AbsAsmHandler() {

    override fun getClassVisitor(classWriter: ClassWriter) = SampleClassVisitor(classWriter)
}