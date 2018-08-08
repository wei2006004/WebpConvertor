package com.vinson.webp

import java.awt.Toolkit
import javax.swing.JFrame

/**
 * Created by Vinson on 2018/8/7.
 * e-mail: wei2006004@foxmail.com
 */

fun main(args: Array<String>) {
    val width = Toolkit.getDefaultToolkit().screenSize.width
    val height = Toolkit.getDefaultToolkit().screenSize.height
    val windowsWidth = 500
    val windowsHeight = 400
    
    val frame = JFrame("Webp调整")
    val form = MainFormImpl()

    frame.contentPane = form.panel
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

    frame.setBounds(
            (width - windowsWidth) / 2,
            (height - windowsHeight) / 2,
            windowsWidth,
            windowsHeight)
    frame.isVisible = true
}