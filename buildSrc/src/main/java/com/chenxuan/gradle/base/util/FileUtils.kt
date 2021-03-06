package com.chenxuan.gradle.base.util

import java.io.File

fun File.filterTest(nameReg: String): Array<File>? {
    return listFiles { file -> file?.name == nameReg }
}

fun File.deleteAll() {
    delFolder(path)
}

private fun delAllFile(path: String): Boolean {
    var flag = false
    val file = File(path)
    if (!file.exists()) {
        return flag
    }
    if (!file.isDirectory) {
        return flag
    }
    val tempList = file.list()
    tempList ?: return flag
    var temp: File?
    for (i in tempList.indices) {
        temp = if (path.endsWith(File.separator)) {
            File(path + tempList[i])
        } else {
            File(path + File.separator + tempList[i])
        }
        if (temp.isFile) {
            temp.delete()
        }
        if (temp.isDirectory) {
            delAllFile(path + "/" + tempList[i])
            delFolder(path + "/" + tempList[i])
            flag = true
        }
    }
    return flag
}

private fun delFolder(folderPath: String) {
    try {
        delAllFile(folderPath)
        val myFilePath = File(folderPath)
        myFilePath.delete()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
