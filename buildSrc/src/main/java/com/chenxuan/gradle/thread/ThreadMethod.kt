package com.chenxuan.gradle.thread

import org.objectweb.asm.Opcodes

data class ThreadMethod(
    var opcode: Int = Opcodes.INVOKESTATIC,
    var owner: String?,
    var name: String?,
    var descriptor: String?,
    var isInterface: Boolean = false,
    var tag: String = ""
) {
    fun equalThreadMethod(
        opcode: Int,
        owner: String?,
        name: String?,
        descriptor: String?
    ) =
        this.opcode == opcode && this.owner == owner && this.name == name && this.descriptor == descriptor
}

internal val threadMethods = mutableListOf<ThreadMethod>().apply {
    add(
        ThreadMethod(
            owner = "java/util/concurrent/Executors",
            name = "newFixedThreadPool",
            descriptor = "(I)Ljava/util/concurrent/ExecutorService;",
            tag = "fix"
        )
    )
    add(
        ThreadMethod(
            owner = "java/util/concurrent/Executors",
            name = "newSingleThreadExecutor",
            descriptor = "()Ljava/util/concurrent/ExecutorService;",
            tag = "single"
        )
    )
    add(
        ThreadMethod(
            owner = "java/util/concurrent/Executors",
            name = "newCachedThreadPool",
            descriptor = "()Ljava/util/concurrent/ExecutorService;",
            tag = "cache"
        )
    )
    add(
        ThreadMethod(
            owner = "java/util/concurrent/Executors",
            name = "newScheduledThreadPool",
            descriptor = "(I)Ljava/util/concurrent/ScheduledExecutorService;",
            tag = "scheduled"
        )
    )
}

internal val realThreadMethods = mapOf(
    "fix" to ThreadMethod(
        owner = "com/chenxuan/hook/ThreadUtil",
        name = "fixThreadPool",
        descriptor = "()Ljava/util/concurrent/ExecutorService;"
    ),
    "single" to ThreadMethod(
        owner = "com/chenxuan/hook/ThreadUtil",
        name = "singleThreadPool",
        descriptor = "()Ljava/util/concurrent/ExecutorService;"
    ),
    "cache" to ThreadMethod(
        owner = "com/chenxuan/hook/ThreadUtil",
        name = "cacheThreadPool",
        descriptor = "()Ljava/util/concurrent/ExecutorService;"
    ),
    "scheduled" to ThreadMethod(
        owner = "com/chenxuan/hook/ThreadUtil",
        name = "scheduledThreadPool",
        descriptor = "()Ljava/util/concurrent/ScheduledExecutorService;"
    )
)
