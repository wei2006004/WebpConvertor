package com.vinson.webp

import java.awt.Toolkit
import java.io.File
import javax.swing.JFileChooser
import javax.swing.JOptionPane
import javax.swing.filechooser.FileFilter

/**
 * Created by Vinson on 2018/8/8.
 * e-mail: wei2006004@foxmail.com
 */
open class BaseForm {

    class SimpleFilter(private val surfix: String): FileFilter() {

        override fun accept(f: File): Boolean {
            if (f.isDirectory) return true
            return f.absolutePath.contains(surfix)
        }

        override fun getDescription(): String {
            return "*.$surfix"
        }
    }

    protected fun sufixToFilters(surfixs: List<String>) = surfixs.map { SimpleFilter(it) }

    protected val fileChooser = JFileChooser()

    protected fun chooseDirOrFile(isDir: Boolean, filters: List<String> = emptyList(), done: (Boolean, String) -> Unit) = chooseDirOrFileWithFilter(isDir, done, sufixToFilters(filters))

    protected fun chooseDirOrFileWithFilter(isDir: Boolean, done: (Boolean, String) -> Unit, filters: List<FileFilter>) {
        setFileChooser(isDir, filters)
        fileChooser.showOpenDialog(null).let {
            if (it != 1) {
                val file = fileChooser.selectedFile
                done(true, file.absolutePath)
            } else {
                done(false, "")
            }
        }
    }

    private fun setFileChooser(isDir: Boolean, filters: List<FileFilter>) {
        fileChooser.fileSelectionMode = if (isDir) JFileChooser.DIRECTORIES_ONLY else JFileChooser.FILES_ONLY
        fileChooser.resetChoosableFileFilters()
        fileChooser.isAcceptAllFileFilterUsed = true
        if (!isDir) {
            filters.forEach {
                fileChooser.addChoosableFileFilter(it)
            }
            if (filters.isNotEmpty()) {
                fileChooser.isAcceptAllFileFilterUsed = false
            }
        }
    }

    protected fun saveFileDialog(isDir: Boolean, filters: List<String> = emptyList(), done: (Boolean, String) -> Unit) = saveFileDialogWithFilter(isDir, done, sufixToFilters(filters))

    protected fun saveFileDialogWithFilter(isDir: Boolean, done: (Boolean, String) -> Unit, filters: List<FileFilter>) {
        setFileChooser(isDir, filters)
        fileChooser.showSaveDialog(null).let {
            if (it != 1) {
                val file = fileChooser.selectedFile
                done(true, file.absolutePath)
            } else {
                done(false, "")
            }
        }
    }

    protected fun toast(message: String, title: String = "") {
        Toolkit.getDefaultToolkit().beep()
        JOptionPane.showMessageDialog(null, message, if (title.isEmpty()) "警告" else title, JOptionPane.INFORMATION_MESSAGE)
    }
}