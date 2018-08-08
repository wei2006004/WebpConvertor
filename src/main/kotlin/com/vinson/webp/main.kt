package com.vinson.webp

import javax.swing.JFrame

/**
 * Created by Vinson on 2018/8/7.
 * e-mail: wei2006004@foxmail.com
 */

fun main(args: Array<String>) {
    val frame = JFrame("Webp调整")
    val form = MainFormImpl()

    frame.contentPane = form.panel
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

    frame.setSize(500, 400)
    frame.isVisible = true
}