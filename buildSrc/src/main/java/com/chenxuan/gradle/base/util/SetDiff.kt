package com.chenxuan.gradle.base.util

import java.util.*
import java.util.function.Consumer

class SetDiff<T>(beforeList: Set<T>, afterList: Set<T>?) {
    private val addedList: MutableList<T> = ArrayList()
    private val unchangedList: MutableList<T> = ArrayList()
    private val removedList: MutableList<T> = ArrayList()

    fun getAddedList(): List<T> {
        return addedList
    }

    fun getUnchangedList(): List<T> {
        return unchangedList
    }

    fun getRemovedList(): List<T> {
        return removedList
    }

    init {
        addedList.addAll(afterList!!)
        beforeList.forEach(Consumer { e: T ->
            val b = if (addedList.remove(e)) unchangedList.add(e) else removedList.add(e)
        })
    }
}