package com.chenxuan.gradle.image

import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

object ImageMethodVisitor {

    operator fun invoke(
        mv: MethodVisitor,
        access: Int,
        name: String?,
        descriptor: String?,
    ): MethodVisitor {
        return object : AdviceAdapter(Opcodes.ASM9, mv, access, name, descriptor) {

            override fun onMethodExit(opcode: Int) {
                mv.visitVarInsn(ALOAD, 0)
                mv.visitFieldInsn(
                    GETFIELD,
                    "com/bumptech/glide/request/SingleRequest",
                    "requestListeners",
                    "Ljava/util/List;"
                )
                val notNull = Label()
                mv.visitJumpInsn(IFNONNULL, notNull)
                mv.visitVarInsn(ALOAD, 0)
                mv.visitTypeInsn(NEW, "java/util/ArrayList")
                mv.visitInsn(DUP)
                mv.visitMethodInsn(INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V", false)
                mv.visitFieldInsn(
                    PUTFIELD,
                    "com/bumptech/glide/request/SingleRequest",
                    "requestListeners",
                    "Ljava/util/List;"
                )
                mv.visitLabel(notNull)
                mv.visitVarInsn(ALOAD, 0)
                mv.visitFieldInsn(
                    GETFIELD,
                    "com/bumptech/glide/request/SingleRequest",
                    "requestListeners",
                    "Ljava/util/List;"
                )
                mv.visitMethodInsn(
                    INVOKESTATIC,
                    "com/chenxuan/hook/GlideUtil",
                    "addListener",
                    "(Ljava/util/List;)V",
                    false
                )
                super.onMethodExit(opcode)
            }
        }
    }
}