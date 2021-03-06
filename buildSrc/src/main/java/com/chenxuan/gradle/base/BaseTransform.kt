package com.chenxuan.gradle.base

import com.android.build.api.transform.*
import com.chenxuan.gradle.base.util.*
import com.google.common.io.Files
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.ForkJoinPool

class BaseTransform(
    transformInvocation: TransformInvocation?,
    callBack: TransformCallBack,
) {
    private var mCallBack: TransformCallBack? = callBack
    private var context: Context? = null
    private var inputs: Collection<TransformInput>? = null
    private var outputProvider: TransformOutputProvider? = null
    private var isIncremental = false
    private var deleteCallBack: DeleteCallBack? = null
    private var simpleScan = false
    private var filter: ClassNameFilter? = null
    private val executor: ExecutorService = ForkJoinPool.commonPool()
    private val tasks: MutableList<Callable<Void?>> = ArrayList()
    private val destFiles = mutableListOf<File>()

    init {
        context = transformInvocation?.context
        inputs = transformInvocation?.inputs
        outputProvider = transformInvocation?.outputProvider
        isIncremental = transformInvocation?.isIncremental ?: false
    }

    fun openSimpleScan() {
        simpleScan = true
    }

    fun setDeleteCallBack(deleteCallBack: DeleteCallBack?) {
        this.deleteCallBack = deleteCallBack
    }

    fun startTransform() {
        try {
            if (!isIncremental) {
                outputProvider!!.deleteAll()
            }
            inputs?.forEach { input ->
                for (jarInput in input.jarInputs) {
                    val status = jarInput.status
                    var destName = jarInput.file.name
                    /* 重命名输出文件,因为可能同名,会覆盖*/
                    val hexName = DigestUtils.md5Hex(jarInput.file.absolutePath).substring(0, 8)
                    if (destName.endsWith(".jar")) {
                        destName = destName.substring(0, destName.length - 4)
                    }
                    /*获得输出文件*/
                    val dest = outputProvider!!.getContentLocation(
                        destName + "_" + hexName,
                        jarInput.contentTypes, jarInput.scopes, Format.JAR
                    )
                    if (isIncremental) {
                        when (status) {
                            Status.ADDED -> foreachJar(dest, jarInput)
                            Status.CHANGED -> diffJar(dest, jarInput)
                            Status.REMOVED -> try {
                                deleteScan(dest)
                                if (dest.exists()) {
                                    FileUtils.forceDelete(dest)
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            else -> {

                            }
                        }
                    } else {
                        foreachJar(dest, jarInput)
                    }
                }
                for (directoryInput in input.directoryInputs) {
                    foreachClass(directoryInput)
                }
            }
            executor.invokeAll(tasks)
            destFiles.forEach {
                it.filterTest("temp")?.forEach { file ->
                    file.deleteAll()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    private fun foreachClass(directoryInput: DirectoryInput) {
        val dest = outputProvider!!.getContentLocation(
            directoryInput.name, directoryInput.contentTypes,
            directoryInput.scopes, Format.DIRECTORY
        )
        destFiles.add(dest)
        val map = directoryInput.changedFiles
        val dir = directoryInput.file
        if (isIncremental) {
            for ((file, status) in map) {
                val destFilePath = file.absolutePath.replace(dir.absolutePath, dest.absolutePath)
                val destFile = File(destFilePath)
                when (status) {
                    Status.ADDED, Status.CHANGED -> {
                        val callable = Callable<Void?> {
                            try {
                                FileUtils.touch(destFile)
                            } catch (ignored: Exception) {
                                Files.createParentDirs(destFile)
                            }
                            modifySingleFile(dir, file, destFile)
                            null
                        }
                        tasks.add(callable)
                        executor.submit(callable)
                    }
                    Status.REMOVED -> deleteDirectory(destFile, dest)
                    else -> {
                    }
                }
            }
        } else {
            changeFile(dir, dest)
        }
    }

    private fun deleteDirectory(destFile: File, dest: File) {
        try {
            if (destFile.isDirectory) {
                for (classFile in com.android.utils.FileUtils.getAllFiles(destFile)) {
                    deleteSingle(classFile, dest)
                }
            } else {
                deleteSingle(destFile, dest)
            }
        } catch (ignored: Exception) {
        }
        try {
            if (destFile.exists()) {
                FileUtils.forceDelete(destFile)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun deleteSingle(classFile: File, dest: File) {
        try {
            if (classFile.name.endsWith(".class")) {
                val absolutePath = classFile.absolutePath.replace(
                    dest.absolutePath +
                            File.separator, ""
                )
                val className = ClassUtils.path2Classname(absolutePath)
                val bytes = IOUtils.toByteArray(FileInputStream(classFile))
                deleteCallBack?.delete(className, bytes)

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    private fun modifySingleFile(dir: File, file: File, dest: File) {
        try {
            val absolutePath = file.absolutePath.replace(
                dir.absolutePath +
                        File.separator, ""
            )
            val className = ClassUtils.path2Classname(absolutePath)
            if (absolutePath.endsWith(".class")) {
                var modifiedBytes: ByteArray?
                val bytes = IOUtils.toByteArray(FileInputStream(file))
                modifiedBytes = if (!simpleScan) {
                    process(className, bytes)
                } else {
                    process(className, null)
                }
                if (modifiedBytes == null) {
                    modifiedBytes = bytes
                }
                ClassUtils.saveFile(dest, modifiedBytes)
            } else {
                if (!file.isDirectory) {
                    FileUtils.copyFile(file, dest)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun process(className: String, classBytes: ByteArray?): ByteArray? {
        try {
            if (filter == null) {
                filter = DefaultClassNameFilter()
            }
            if (filter?.filter(className) == false) {
                return mCallBack?.process(className, classBytes)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @Throws(IOException::class)
    private fun changeFile(dir: File, dest: File) {
        if (dir.isDirectory) {
            FileUtils.copyDirectory(dir, dest)
            for (classFile in com.android.utils.FileUtils.getAllFiles(dir)) {
                if (classFile.name.endsWith(".class")) {
                    val task = Callable<Void?> {
                        val absolutePath = classFile.absolutePath.replace(
                            dir.absolutePath + File.separator, ""
                        )
                        val className = ClassUtils.path2Classname(absolutePath)
                        if (!simpleScan) {
                            val bytes = IOUtils.toByteArray(FileInputStream(classFile))
                            val modifiedBytes = process(className, bytes)
                            modifiedBytes?.let { saveClassFile(it, dest, absolutePath) }
                        } else {
                            process(className, null)
                        }
                        null
                    }
                    tasks.add(task)
                    executor.submit(task)
                }
            }
        }
    }

    @Throws(Exception::class)
    private fun saveClassFile(modifiedBytes: ByteArray, dest: File, absolutePath: String) {
        val tempDir = File(dest, "/temp")
        val tempFile = File(tempDir, absolutePath)
        tempFile.mkdirs()
        val modified = ClassUtils.saveFile(tempFile, modifiedBytes)
        //key为相对路径
        val target = File(dest, absolutePath)
        if (target.exists()) {
            target.delete()
        }
        FileUtils.copyFile(modified, target)
        tempFile.delete()
    }

    private fun foreachJar(dest: File, jarInput: JarInput) {
        try {
            if (!simpleScan) {
                val modifiedJar = JarUtils.modifyJarFile(jarInput.file, context?.temporaryDir, this)
                FileUtils.copyFile(modifiedJar, dest)
            } else {
                val jarFile = jarInput.file
                val classNames = JarUtils.scanJarFile(jarFile)
                for (className in classNames) {
                    process(className, null)
                }
                FileUtils.copyFile(jarFile, dest)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun diffJar(dest: File, jarInput: JarInput) {
        try {
            val oldJarFileName = JarUtils.scanJarFile(dest)
            val newJarFileName = JarUtils.scanJarFile(jarInput.file)
            val diff =
                SetDiff(oldJarFileName, newJarFileName)
            val removeList = diff.getRemovedList()
            if (removeList.isNotEmpty()) {
                JarUtils.deleteJarScan(dest, removeList, deleteCallBack)
            }
            foreachJar(dest, jarInput)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun deleteScan(dest: File) {
        try {
            JarUtils.deleteJarScan(dest, deleteCallBack)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}