package com.chenxuan.gradle.base.util

interface ClassNameFilter {
    fun filter(className: String): Boolean
}