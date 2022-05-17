package com.ss.android.ugc.aweme.cao.plugin.listener

import com.intellij.ui.components.JBTextField
import com.ss.android.ugc.aweme.cao.plugin.ui.CaoPluginToolWindow
import com.ss.android.ugc.aweme.cao.plugin.CaoTaskManager
import com.ss.android.ugc.aweme.cao.plugin.ui.TaskTableModel
import com.ss.android.ugc.aweme.cao.plugin.match
import java.awt.event.KeyEvent
import java.awt.event.KeyListener

class QueryKeyListener(val window: CaoPluginToolWindow, val textField: JBTextField, val taskManager: CaoTaskManager, val tableModel: TaskTableModel): KeyListener {

    override fun keyTyped(e: KeyEvent?) {

    }

    override fun keyPressed(e: KeyEvent?) {
        // 处理搜索
        val search = textField.text
        val filterList = taskManager.tasks.filter { it.match(search) }
        tableModel.updateData(filterList)
        window.updateTableMaxWidth()
    }

    override fun keyReleased(e: KeyEvent?) {

    }
}