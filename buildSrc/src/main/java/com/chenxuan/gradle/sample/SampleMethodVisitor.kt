package com.chenxuan.gradle.sample

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

object SampleMethodVisitor {

    operator fun invoke(
            mv: MethodVisitor,
            access: Int,
            name: String?,
            descriptor: String?,
    ): MethodVisitor {
        return object : AdviceAdapter(Opcodes.ASM9, mv, access, name, descriptor) {

            override fun visitMethodInsn(
                    opcode: Int,
                    owner: String?,
                    name: String?,
                    descriptor: String?,
                    isInterface: Boolean
            ) {
                if (containsSample(opcode, owner, name, descriptor, isInterface)) {
                    super.visitMethodInsn(Opcodes.INVOKESTATIC, "com/chenxuan/hook/sample/SampleHelper", "handle", "()V", false)
                } else {
                    super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
                }
            }
        }
    }

    private fun containsSample(
            opcode: Int,
            owner: String?,
            name: String?,
            descriptor: String?,
            isInterface: Boolean
    ): Boolean {
        return opcode == Opcodes.INVOKESTATIC
                && owner == "com/chenxuan/hook/sample/SampleUtil"
                && name == "sample"
                && descriptor == "()V" && !isInterface
    }
}