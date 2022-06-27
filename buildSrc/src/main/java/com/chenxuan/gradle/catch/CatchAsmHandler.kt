package com.chenxuan.gradle.catch

import com.chenxuan.gradle.base.AbsAsmHandler
import org.objectweb.asm.ClassWriter

class CatchAsmHandler : AbsAsmHandler() {

    override fun getClassVisitor(classWriter: ClassWriter) = CatchClassVisitor(classWriter)
}