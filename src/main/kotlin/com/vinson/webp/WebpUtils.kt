package com.vinson.webp

import java.io.BufferedReader
import java.io.InputStreamReader


/**
 * Created by Vinson on 2018/8/8.
 * e-mail: wei2006004@foxmail.com
 */
object WebpUtils {

    const val WEBP_WINDOWS = "./bin/cwebp.exe"
    const val WEBP_MAC = "./bin/cwebp.exe"

    private val sysName = System.getProperties().getProperty("os.name")

    private val isWindow = sysName.toUpperCase().contains("WINDOWS")

    private val webpBin = if (isWindow) WEBP_WINDOWS else WEBP_MAC

    fun webpConvert(origin: String, dest: String, quality: Int, alpha: Int): String {
        val args = arrayOf(webpBin, "-q", quality.toString(), "-alpha_q", alpha.toString(), origin, "-o", dest)
        val input = Runtime.getRuntime().exec(args).errorStream
        val reader = BufferedReader(InputStreamReader(input))
        return buildString {
            do {
                val line = reader.readLine()
                line?.let {
                    append(it)
                    append('\n')
                }
            } while (line != null)
        }
    }
}