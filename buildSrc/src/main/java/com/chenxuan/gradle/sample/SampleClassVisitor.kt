package com.chenxuan.gradle.sample

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class SampleClassVisitor(classVisitor: ClassVisitor) : ClassVisitor(Opcodes.ASM9, classVisitor) {

    private var className: String? = null

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        className = name
        super.visit(version, access, name, signature, superName, interfaces)
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val mv = cv.visitMethod(access, name, descriptor, signature, exceptions)
        if (className == "com/chenxuan/hook/sample/SampleImpl" && name == "something" && descriptor == "()V") {
            //接口方法插入原方法
            return SampleImplMethodVisitor(mv, access, name, descriptor)
        }
        //方法调用处替换为接口方法
        return SampleMethodVisitor(mv, access, name, descriptor)
    }
}