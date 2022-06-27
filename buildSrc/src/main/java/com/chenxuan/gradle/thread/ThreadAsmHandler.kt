package com.chenxuan.gradle.thread

import com.chenxuan.gradle.base.AbsAsmHandler
import org.objectweb.asm.ClassWriter

class ThreadAsmHandler : AbsAsmHandler() {

    override fun getClassVisitor(classWriter: ClassWriter) = ThreadClassVisitor(classWriter)
}