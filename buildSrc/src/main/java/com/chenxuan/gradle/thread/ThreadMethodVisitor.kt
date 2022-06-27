package com.chenxuan.gradle.thread

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

object ThreadMethodVisitor {

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
                if (containsThread(opcode, owner, name, descriptor) is ThreadMethod) {
                    val threadMethod =
                        containsThread(opcode, owner, name, descriptor) as ThreadMethod
                    realThreadMethods[threadMethod.tag]?.run {
                        super.visitMethodInsn(
                            this.opcode,
                            this.owner,
                            this.name,
                            this.descriptor,
                            this.isInterface
                        )
                    }
                } else {
                    super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
                }
            }
        }
    }

    private fun containsThread(
        opcode: Int,
        owner: String?,
        name: String?,
        descriptor: String?
    ): ThreadMethod? {
        threadMethods.forEach {
            if (it.equalThreadMethod(opcode, owner, name, descriptor)) {
                return it
            }
        }
        return null
    }
}