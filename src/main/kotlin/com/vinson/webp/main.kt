package com.vinson.webp

import javax.swing.JFileChooser
import javax.swing.JFrame

/**
 * Created by Vinson on 2018/8/7.
 * e-mail: wei2006004@foxmail.com
 */
val fileChooser = JFileChooser()


fun main(args: Array<String>) {
    val frame = JFrame("WordFrom")
    val form = MainForm()

    fileChooser.fileSelectionMode = 1
    form.webpButton.addActionListener {
        chooseDir { success, file ->
            if (success) {
                form.webpText.text = file
            }
        }
    }
    form.saveButton.addActionListener {
        chooseDir { success, file ->
            if (success) {
                form.saveText.text = file
            }
        }
    }

    frame.contentPane = form.panel
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

    frame.setSize(500, 400)
    frame.isVisible = true
}

fun chooseDir(done: (Boolean, String) -> Unit) {
    fileChooser.showOpenDialog(null).let {
        if (it != 1) {
            val file = fileChooser.selectedFile
            done(true, file.absolutePath)
        } else {
            done(false, "")
        }
    }
}
