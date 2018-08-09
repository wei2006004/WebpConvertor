package com.vinson.webp

import java.io.File
import kotlin.concurrent.thread

/**
 * Created by Vinson on 2018/8/9.
 * e-mail: wei2006004@foxmail.com
 */
class WebpInfoForm: InfoForm() {
    init {
        selectBtn.addActionListener {
            chooseDirOrFile(false, SUFFIX_WEBP) { success, file ->
                if (success) {
                    webpText.text = file
                }
            }
        }
        startBtn.addActionListener {
            val orgin = webpText.text
            if (orgin.isEmpty()) {
                toast("请选择webp文件")
                return@addActionListener
            }
            if (!File(orgin).exists()) {
                toast("文件不存在")
                return@addActionListener
            }
            getWebpInfo(orgin)
        }
    }

    private fun getWebpInfo(orgin: String) {
        startBtn.isEnabled = false
        thread {
            msgText.text = WebpUtils.webpInfo(orgin)
            startBtn.isEnabled = true
        }
    }
}