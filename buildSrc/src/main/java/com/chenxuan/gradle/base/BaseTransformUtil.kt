package com.chenxuan.gradle.base

import com.android.build.api.transform.TransformInvocation
import com.chenxuan.gradle.base.util.ClassUtils

object BaseTransformUtil {
    operator fun invoke(transformInvocation: TransformInvocation?, asmHelper: AsmHandler) =
        BaseTransform(transformInvocation) { className, classBytes ->
            var result: ByteArray? = null
            if (ClassUtils.checkClassName(className)) {
                try {
                    result = classBytes?.run { asmHelper.modifyClass(this) }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            result
        }.apply {
            startTransform()
        }
}