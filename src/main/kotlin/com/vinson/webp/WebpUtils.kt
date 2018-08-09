package com.vinson.webp

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader


/**
 * Created by Vinson on 2018/8/8.
 * e-mail: wei2006004@foxmail.com
 */
object WebpUtils {

    enum class Platform(
            dir: String,
            cwebp: String,
            webpinfo: String,
            img2webp: String) {
        WINDOWS("./bin/windows","cwebp.exe", "webpinfo.exe", "img2webp.exe"),
        MAC("./bin/mac","cwebp", "webpinfo", "img2webp");

        val cwebp = "$dir/$cwebp"
        val webpinfo = "$dir/$webpinfo"
        val img2webp = "$dir/$img2webp"
    }

    private val sysName = System.getProperties().getProperty("os.name")
    private val isWindow = sysName.toUpperCase().contains("WINDOWS")
    private val currentPlatform get() = if (isWindow) Platform.WINDOWS else Platform.MAC

    fun webpConvert(origin: String, dest: String, quality: Int, alpha: Int)
            = execArgs(listOf(currentPlatform.cwebp, "-q", quality.toString(), "-alpha_q", alpha.toString(), origin, "-o", dest), true)

    fun webpInfo(file: String)
            = execArgs(listOf(currentPlatform.webpinfo, "-bitstream_info", file), false)

    fun webpAnim(list: List<String>, dest: String, duration: Int, loop: Int, quality: Int, alpha: Int): String {
        val args = mutableListOf(currentPlatform.img2webp, "-loop", loop.toString())
        list.forEach {
            args.add(it)
            args.add("-lossy")
            args.add("-d")
            args.add(duration.toString())
            args.add("-q")
            args.add(quality.toString())
        }
        args.add("-o")
        args.add(dest)
        return execArgs(args, true)
    }

    private fun execArgs(args: List<String>, errorOuput: Boolean = false): String {
        val input = Runtime.getRuntime().exec(args.toTypedArray()).let {
            if (errorOuput) {
                it.errorStream
            } else {
                it.inputStream
            }
        }
        return outputToString(input)
    }

    private fun outputToString(input: InputStream) = BufferedReader(InputStreamReader(input)).let {
        buildString {
            do {
                val line = it.readLine()
                line?.let {
                    append(it)
                    append('\n')
                }
            } while (line != null)
        }
    }
}