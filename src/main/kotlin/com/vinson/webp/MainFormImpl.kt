package com.vinson.webp

import java.awt.Toolkit
import java.io.File
import javax.swing.JFileChooser
import javax.swing.JOptionPane
import javax.swing.text.AttributeSet
import javax.swing.text.PlainDocument
import kotlin.concurrent.thread

/**
 * Created by Vinson on 2018/8/8.
 * e-mail: wei2006004@foxmail.com
 */

class MainFormImpl : MainForm() {

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

    private val fileChooser = JFileChooser()

    init {
        fileChooser.fileSelectionMode = 1
        webpButton.addActionListener {
            chooseDir { success, file ->
                if (success) {
                    webpText.text = file
                }
            }
        }
        saveButton.addActionListener {
            chooseDir { success, file ->
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
            if (quality !in (1..99) || alpha !in (1..99)) {
                toast("quality和alpha必须在1-99之间")
                return@addActionListener
            }
            if (orgin.isEmpty() || save.isEmpty()) {
                toast("请选择文件夹")
                return@addActionListener
            }
            if (!File(orgin).exists() || !File(save).exists()) {
                toast("文件夹不存在")
                return@addActionListener
            }
            startConvert(orgin, save, quality, alpha)
        }
    }

    private fun startConvert(orgin: String, save: String, quality: Int, alpha: Int) {
        startButton.isEnabled = false
        thread {
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
        }
    }

    private fun toast(message: String, title: String = "") {
        Toolkit.getDefaultToolkit().beep()
        JOptionPane.showMessageDialog(null, message, if (title.isEmpty()) "警告" else title, JOptionPane.INFORMATION_MESSAGE)
    }

    private fun chooseDir(done: (Boolean, String) -> Unit) {
        fileChooser.showOpenDialog(null).let {
            if (it != 1) {
                val file = fileChooser.selectedFile
                done(true, file.absolutePath)
            } else {
                done(false, "")
            }
        }
    }
}