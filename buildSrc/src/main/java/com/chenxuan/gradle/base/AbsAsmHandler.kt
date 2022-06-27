package com.chenxuan.gradle.base

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

abstract class AbsAsmHandler : AsmHandler {
    override fun modifyClass(srcClass: ByteArray): ByteArray {
        val classReader = ClassReader(srcClass)
        val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
        val cv: ClassVisitor = getClassVisitor(classWriter)
        classReader.accept(cv, ClassReader.EXPAND_FRAMES)
        return classWriter.toByteArray()
    }

    abstract fun getClassVisitor(classWriter: ClassWriter): ClassVisitor
}