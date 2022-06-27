package com.chenxuan.gradle.catch

import com.chenxuan.hook.Catch
import org.objectweb.asm.*
import org.objectweb.asm.commons.AdviceAdapter

object CatchMethodVisitor {

    operator fun invoke(
        mv: MethodVisitor,
        access: Int,
        name: String?,
        descriptor: String?,
    ): MethodVisitor {
        return object : AdviceAdapter(Opcodes.ASM9, mv, access, name, descriptor) {
            private var needTrack = false
            private val start = Label()
            private val end = Label()
            private val catch = Label()

            override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
                if (Type.getDescriptor(Catch::class.java) == descriptor) needTrack = true
                return super.visitAnnotation(descriptor, visible)
            }

            override fun onMethodEnter() {
                if (needTrack) {
                    visitLabel(start)
                    visitTryCatchBlock(start, end, catch, "java/lang/Exception")
                }
                super.onMethodEnter()
            }

            override fun visitMaxs(maxStack: Int, maxLocals: Int) {
                if (needTrack) {
                    mv.visitLabel(end)
                    mv.visitLabel(catch)
                    mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, arrayOf<Any>("java/lang/Exception"))
                    val local = newLocal(Type.LONG_TYPE);
                    mv.visitVarInsn(ASTORE, local)
                    mv.visitVarInsn(ALOAD, local)
                    mv.visitMethodInsn(
                        INVOKEVIRTUAL,
                        "java/lang/Exception",
                        "printStackTrace",
                        "()V",
                        false
                    )
                    mv.visitVarInsn(ALOAD, local)
                    mv.visitInsn(ATHROW)
                }
                super.visitMaxs(maxStack, maxLocals)
            }
        }
    }
}