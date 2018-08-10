package com.vinson.webp

import java.io.File
import kotlin.concurrent.thread

/**
 * Created by Vinson on 2018/8/9.
 * e-mail: wei2006004@foxmail.com
 */
class MutliConvertForm : MainForm() {

    init {
        animPanel.isVisible = false
        webpButton.addActionListener {
            chooseDirOrFile(true) { success, file ->
                if (success) {
                    webpText.text = file
                }
            }
        }
        saveButton.addActionListener {
            saveFileDialog(true) { success, file ->
                if (success) {
                    saveText.text = file
                }
            }
        }
        qualityText.document = TextDocument()
        qualityText.text = "90"
        alphaText.document = TextDocument()
        alphaText.text = "50"
        startButton.addActionListener {
            val orgin = webpText.text
            val save = saveText.text
            val quality = qualityText.text.toInt()
            val alpha = alphaText.text.toInt()
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
            startConverts(orgin, save, quality, alpha)
        }
    }

    private fun startConverts(orgin: String, save: String, quality: Int, alpha: Int) {
        startButton.isEnabled = false
        thread {
            FileUtils.mkDir(save)
            buildString {
                val dir = File(orgin)
                dir.list().filter { isPlainImage(it) }.forEach {
                    val name = it.substring(0, it.lastIndexOf('.'))
                    val from = File(orgin, it).absolutePath
                    val to = File(save, "$name.webp").absolutePath
                    append("Compress: $from\n")
                    val out = WebpUtils.webpConvert(from, to, quality, alpha)
                    println(out)
                    append("Finish: $to\n")
                    msgText.text = toString()
                    msgText.caretPosition = msgText.document.length
                }
            }
            startButton.isEnabled = true
        }
    }
}