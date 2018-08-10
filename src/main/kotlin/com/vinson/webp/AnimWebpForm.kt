package com.vinson.webp

import java.io.File
import kotlin.concurrent.thread

/**
 * Created by Vinson on 2018/8/9.
 * e-mail: wei2006004@foxmail.com
 */
class AnimWebpForm  : MainForm() {

    init {
        saveLabel.text = "保存文件"

        webpButton.addActionListener {
            chooseDirOrFile(true) { success, file ->
                if (success) {
                    webpText.text = file
                }
            }
        }
        saveButton.addActionListener {
            saveFileDialog(false, SUFFIX_WEBP) { success, file ->
                if (success) {
                    saveText.text = file
                }
            }
        }
        qualityText.document = TextDocument()
        qualityText.text = "90"
        alphaText.document = TextDocument()
        alphaText.text = "50"
        durationText.document = TextDocument()
        durationText.text = "200"
        loopText.document = TextDocument()
        loopText.text = "3"
        startButton.addActionListener {
            val orgin = webpText.text
            val save = saveText.text
            val quality = qualityText.text.toInt()
            val alpha = alphaText.text.toInt()
            val duration = durationText.text.toInt()
            val loop = loopText.text.toInt()
            if (quality !in (1..100) || alpha !in (1..100)) {
                toast("quality和alpha必须在1-100之间")
                return@addActionListener
            }
            if (orgin.isEmpty() || save.isEmpty()) {
                toast("请选择文件夹")
                return@addActionListener
            }
            if (!File(orgin).exists()) {
                toast("文件夹不存在")
                return@addActionListener
            }
            startConvert(orgin, save, duration, loop, quality, alpha)
        }
    }

    private fun startConvert(orgin: String, save: String, duration: Int, loop: Int, quality: Int, alpha: Int) {
        startButton.isEnabled = false
        thread {
            val list = File(orgin).listFiles().map { it.absolutePath }.filter { isImage(it) }.sorted()
            buildString {
                append("Handle file: ${list.size}\n")
                val files = list.map {
                    append("$it\n")
                    msgText.text = toString()
                    msgText.caretPosition = msgText.document.length
                    if (isWebp(it)) {
                        println("webp skip: $it")
                        File(it)
                    } else {
                        // 非webp时先转换为webp
                        val temp = File.createTempFile(System.currentTimeMillis().toString(), ".webp")
                        println("create temp: ${temp.absolutePath}")
                        WebpUtils.webpConvert(it, temp.absolutePath, quality, alpha)
                        temp
                    }
                }
                msgText.text = toString()
                msgText.caretPosition = msgText.document.length

                val out = WebpUtils.webpAnim(files.map { it.absolutePath }, save, duration, loop, quality, alpha)
                append(out)
                if (File(save).exists()) {
                    append("\nFinish: $save\n")
                }
                msgText.text = toString()
                msgText.caretPosition = msgText.document.length
            }
            startButton.isEnabled = true
        }
    }
}