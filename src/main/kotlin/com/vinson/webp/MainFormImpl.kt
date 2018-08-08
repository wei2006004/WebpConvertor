package com.vinson.webp

import javax.swing.JFileChooser

/**
 * Created by Vinson on 2018/8/8.
 * e-mail: wei2006004@foxmail.com
 */

class MainFormImpl: MainForm() {
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