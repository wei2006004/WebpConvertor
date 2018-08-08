package com.vinson.webp

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader


/**
 * Created by Vinson on 2018/8/8.
 * e-mail: wei2006004@foxmail.com
 */
object WebpUtils {

    const val WIN_CWEBP = "./bin/cwebp.exe"
    const val MAC_CWEBP = "./bin/cwebp.exe"

    const val WIN_ANIM = "./bin/img2webp.exe"
    const val MAC_ANIM = "./bin/img2webp.exe"

    private val sysName = System.getProperties().getProperty("os.name")

    private val isWindow = sysName.toUpperCase().contains("WINDOWS")

    private val webpBin = if (isWindow) WIN_CWEBP else MAC_CWEBP
    private val webpAnimBin = if (isWindow) WIN_ANIM else MAC_ANIM

    fun webpConvert(origin: String, dest: String, quality: Int, alpha: Int): String {
        val args = arrayOf(webpBin, "-q", quality.toString(), "-alpha_q", alpha.toString(), origin, "-o", dest)
        val input = Runtime.getRuntime().exec(args).errorStream
        return outputToString(input)
    }

    fun webpAnim(list: List<String>, duration: Int, quality: Int, alpha: Int): String {
        val args = listOf(webpAnimBin)
        val input = Runtime.getRuntime().exec(args.toTypedArray()).errorStream
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