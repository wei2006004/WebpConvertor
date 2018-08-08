package com.vinson.webp

import java.io.File

/**
 * Created by Vinson on 2018/6/8.
 * e-mail: wei2006004@foxmail.com
 */
object FileUtils {
    fun mkDir(dir: String) = mkDir(File(dir))

    fun mkDir(dir: File) {
        if (dir.exists()) return
        if (dir.parentFile.exists()) {
            dir.mkdir()
        } else {
            mkDir(dir.parentFile)
            dir.mkdir()
        }
    }

    fun listToFile(list: Collection<String>, file: File) {
        val build = StringBuilder()
        list.forEach {
            build.append("$it\n")
        }
        file.writeText(build.toString())
    }

    fun listToFile(list: Collection<String>, file: String) = listToFile(list, File(file))
}