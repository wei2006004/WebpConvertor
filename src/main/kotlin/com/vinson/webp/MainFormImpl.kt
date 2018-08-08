package com.vinson.webp

import java.io.File
import javax.swing.text.AttributeSet
import javax.swing.text.PlainDocument
import kotlin.concurrent.thread

/**
 * Created by Vinson on 2018/8/8.
 * e-mail: wei2006004@foxmail.com
 */

class MainFormImpl(private val multi: Boolean) : MainForm() {

    class TextDocument: PlainDocument() {
        override fun insertString(offs: Int, str: String?, a: AttributeSet?) {
            if (str == null) return
            try {
                str.toInt()
                super.insertString(offs, str, a)
            } catch (e: Exception) {
            }
        }
    }

    private val suffixs = arrayOf(".png", ".jpg", ".jpeg", ".JPG", ".JPEG", ".PNG")

    private fun isAcceptImage(file: String): Boolean {
        suffixs.forEach {
            if (file.contains(it)) {
                return true
            }
        }
        return false
    }

    init {
        if (!multi) {
            webpLabel.text = "webp文件"
            saveLabel.text = "保存文件"
        }

        webpButton.addActionListener {
            chooseDirOrFile(multi, listOf("png", "jpg")) { success, file ->
                if (success) {
                    webpText.text = file
                }
            }
        }
        saveButton.addActionListener {
            saveFileDialog(multi) { success, file ->
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
                toast(if (multi) "请选择文件夹" else "请选择webp文件")
                return@addActionListener
            }
            if (!File(orgin).exists()) {
                toast(if (multi) "文件夹不存在" else "文件不存在")
                return@addActionListener
            }
            if (multi) {
                startConverts(orgin, save, quality, alpha)
            } else {
                startConvert(orgin, save, quality, alpha)
            }
        }
    }

    private fun startConvert(orgin: String, save: String, quality: Int, alpha: Int) {
        startButton.isEnabled = false
        thread {
            msgText.text = WebpUtils.webpConvert(orgin, save, quality, alpha)
            startButton.isEnabled = true
        }
    }

    private fun startConverts(orgin: String, save: String, quality: Int, alpha: Int) {
        startButton.isEnabled = false
        thread {
            FileUtils.mkDir(save)
            buildString {
                val dir = File(orgin)
                dir.list().filter { isAcceptImage(it) }.forEach {
                    val name = it.substring(0, it.lastIndexOf('.'))
                    val from = File(orgin, it).absolutePath
                    val to = File(save, "$name.webp").absolutePath
                    val out = WebpUtils.webpConvert(from, to, quality, alpha)
                    append(out)
                    msgText.text = toString()
                }
            }
            startButton.isEnabled = true
        }
    }
}