package com.vinson.webp

import javax.swing.JFrame
import javax.swing.JPanel

/**
 * Created by Vinson on 2018/8/7.
 * e-mail: wei2006004@foxmail.com
 */

fun main(args: Array<String>) {
    val frame = JFrame("WordFrom")
    val from = JPanel()

    frame.contentPane = from
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

    frame.setSize(700, 600)
    frame.isVisible = true
}