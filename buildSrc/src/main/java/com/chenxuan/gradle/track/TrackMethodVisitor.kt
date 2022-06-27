package com.chenxuan.gradle.track

import com.chenxuan.hook.Track
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.commons.AdviceAdapter

object TrackMethodVisitor {

    operator fun invoke(
        mv: MethodVisitor,
        access: Int,
        name: String?,
        descriptor: String?,
    ): MethodVisitor {
        return object : AdviceAdapter(Opcodes.ASM9, mv, access, name, descriptor) {
            var needTrack = false
            var startTimeMills = -1

            override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
                if (Type.getDescriptor(Track::class.java) == descriptor) needTrack = true
                return super.visitAnnotation(descriptor, visible)
            }

            override fun onMethodEnter() {
                if (needTrack) {
                    startTimeMills = newLocal(Type.LONG_TYPE)
                    mv.visitMethodInsn(
                        INVOKESTATIC,
                        "java/lang/System",
                        "currentTimeMillis",
                        "()J",
                        false
                    )
                    mv.visitIntInsn(LSTORE, startTimeMills)
                }
                super.onMethodEnter()
            }

            override fun onMethodExit(opcode: Int) {
                if (needTrack) {
                    val durationTimeMills = newLocal(Type.LONG_TYPE)
                    mv.visitMethodInsn(
                        INVOKESTATIC,
                        "java/lang/System",
                        "currentTimeMillis",
                        "()J",
                        false
                    )
                    mv.visitVarInsn(LLOAD, startTimeMills)
                    mv.visitInsn(LSUB)
                    mv.visitVarInsn(LSTORE, durationTimeMills)
                    mv.visitLdcInsn("chenxuan----->")
                    mv.visitTypeInsn(NEW, "java/lang/StringBuilder")
                    mv.visitInsn(DUP)
                    mv.visitMethodInsn(
                        INVOKESPECIAL,
                        "java/lang/StringBuilder",
                        "<init>",
                        "()V",
                        false
                    )
                    mv.visitLdcInsn("Method name: $name, Total time: ")
                    mv.visitMethodInsn(
                        INVOKEVIRTUAL,
                        "java/lang/StringBuilder",
                        "append",
                        "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
                        false
                    )
                    mv.visitVarInsn(LLOAD, durationTimeMills)
                    mv.visitMethodInsn(
                        INVOKEVIRTUAL,
                        "java/lang/StringBuilder",
                        "append",
                        "(J)Ljava/lang/StringBuilder;",
                        false
                    )
                    mv.visitLdcInsn("ms")
                    mv.visitMethodInsn(
                        INVOKEVIRTUAL,
                        "java/lang/StringBuilder",
                        "append",
                        "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
                        false
                    )
                    mv.visitMethodInsn(
                        INVOKEVIRTUAL,
                        "java/lang/StringBuilder",
                        "toString",
                        "()Ljava/lang/String;",
                        false
                    )
                    mv.visitMethodInsn(
                        INVOKESTATIC,
                        "android/util/Log",
                        "d",
                        "(Ljava/lang/String;Ljava/lang/String;)I",
                        false
                    )
                    mv.visitInsn(POP)
                }
                super.onMethodExit(opcode)
            }
        }
    }
}