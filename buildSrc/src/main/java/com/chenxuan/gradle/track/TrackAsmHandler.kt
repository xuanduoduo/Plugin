package com.chenxuan.gradle.track

import com.chenxuan.gradle.base.AsmHandler
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

class TrackAsmHandler : AsmHandler {

    override fun modifyClass(srcClass: ByteArray): ByteArray {
        val classReader = ClassReader(srcClass)
        val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
        val cv: ClassVisitor = TrackClassVisitor(classWriter)
        classReader.accept(cv, ClassReader.EXPAND_FRAMES)
        return classWriter.toByteArray()
    }
}