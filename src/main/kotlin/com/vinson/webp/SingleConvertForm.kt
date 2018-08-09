package com.vinson.webp

import java.io.File
import kotlin.concurrent.thread

/**
 * Created by Vinson on 2018/8/9.
 * e-mail: wei2006004@foxmail.com
 */
class SingleConvertForm : MainForm() {

    init {
        animPanel.isVisible = false

        webpLabel.text = "webp文件"
        saveLabel.text = "保存文件"

        webpButton.addActionListener {
            chooseDirOrFile(false, SUFFIX_IMG) { success, file ->
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
                toast("请选择webp文件")
                return@addActionListener
            }
            if (!File(orgin).exists()) {
                toast("文件不存在")
                return@addActionListener
            }
            startConvert(orgin, save, quality, alpha)
        }
    }

    private fun startConvert(orgin: String, save: String, quality: Int, alpha: Int) {
        startButton.isEnabled = false
        thread {
            msgText.text = WebpUtils.webpConvert(orgin, save, quality, alpha)
            startButton.isEnabled = true
        }
    }
}