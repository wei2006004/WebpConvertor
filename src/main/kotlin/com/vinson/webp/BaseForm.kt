package com.vinson.webp

import java.awt.Toolkit
import java.io.File
import javax.swing.JFileChooser
import javax.swing.JOptionPane
import javax.swing.filechooser.FileFilter
import javax.swing.text.AttributeSet
import javax.swing.text.PlainDocument

/**
 * Created by Vinson on 2018/8/8.
 * e-mail: wei2006004@foxmail.com
 */
open class BaseForm {

    companion object {
        val SUFFIX_IMG = listOf(".png", ".jpg", ".jpeg", ".JPG", ".JPEG", ".PNG")
        val SUFFIX_WEBP = listOf(".webp")
        val SUFFIX_IMG_WITH_WEBP = SUFFIX_IMG.toMutableList().apply { add(".webp") }.toList()

        fun isPlainImage(file: String) = isAcceptSuffix(SUFFIX_IMG, file)
        fun isImage(file: String) = isAcceptSuffix(SUFFIX_IMG_WITH_WEBP, file)
        fun isWebp(file: String) = file.contains(".webp")

        fun isAcceptSuffix(suffixs: List<String>, file: String): Boolean {
            suffixs.forEach {
                if (file.contains(it)) {
                    return true
                }
            }
            return false
        }
    }

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

    class SimpleFilter(private val suffixs: List<String>) : FileFilter() {

        override fun accept(f: File): Boolean {
            if (f.isDirectory) return true
            return isAcceptSuffix(suffixs, f.absolutePath)
        }

        override fun getDescription() = buildString {
            suffixs.forEach {
                append("*$it;")
            }
        }
    }

    protected val fileChooser = JFileChooser()

    protected fun chooseDirOrFile(isDir: Boolean, filters: List<String> = emptyList(), done: (Boolean, String) -> Unit) = chooseDirOrFileWithFilter(isDir, done, listOf(SimpleFilter(filters)))

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

    protected fun saveFileDialog(isDir: Boolean, filters: List<String> = emptyList(), done: (Boolean, String) -> Unit) = saveFileDialogWithFilter(isDir, done, listOf(SimpleFilter(filters)))

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