package com.chenxuan.gradle.sample

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

object SampleImplMethodVisitor {

    operator fun invoke(
        mv: MethodVisitor,
        access: Int,
        name: String?,
        descriptor: String?,
    ): MethodVisitor {
        return object : AdviceAdapter(Opcodes.ASM9, mv, access, name, descriptor) {

            override fun onMethodExit(opcode: Int) {
                mv.visitMethodInsn(
                    INVOKESTATIC,
                    "com/chenxuan/hook/sample/SampleUtil",
                    "sample",
                    "()V",
                    false
                )
                super.onMethodExit(opcode)
            }
        }
    }

}