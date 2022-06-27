package com.chenxuan.gradle.click

import com.chenxuan.gradle.base.AbsAsmHandler
import org.objectweb.asm.ClassWriter

class ClickAsmHandler : AbsAsmHandler() {

    override fun getClassVisitor(classWriter: ClassWriter) = ClickClassVisitor(classWriter)
}