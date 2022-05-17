package com.ss.android.ugc.aweme.cao.plugin.listener

import com.intellij.ui.components.JBTextField
import com.ss.android.ugc.aweme.cao.plugin.ui.CaoPluginToolWindow
import com.ss.android.ugc.aweme.cao.plugin.CaoTaskManager
import com.ss.android.ugc.aweme.cao.plugin.ui.TaskTableModel
import com.ss.android.ugc.aweme.cao.plugin.match
import com.ss.android.ugc.aweme.cao.plugin.ui.TaskList
import com.ss.android.ugc.aweme.cao.plugin.ui.TaskListModel
import java.awt.event.KeyEvent
import java.awt.event.KeyListener

class QueryKeyListener(val textField: JBTextField, val taskManager: CaoTaskManager, val window: CaoPluginToolWindow): KeyListener {

    override fun keyTyped(e: KeyEvent?) {

    }

    override fun keyPressed(e: KeyEvent?) {
        // 处理搜索
        val search = textField.text
        val filterList = taskManager.tasks.filter { it.match(search) }
        window.updateList(filterList)
    }

    override fun keyReleased(e: KeyEvent?) {

    }
}