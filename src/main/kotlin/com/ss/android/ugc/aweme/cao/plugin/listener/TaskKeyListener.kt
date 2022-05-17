package com.ss.android.ugc.aweme.cao.plugin.listener

import com.ss.android.ugc.aweme.cao.plugin.ui.CaoPluginToolWindow
import com.ss.android.ugc.aweme.cao.plugin.CaoTaskManager
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent

class TaskKeyListener(val window: CaoPluginToolWindow, val taskManager: CaoTaskManager): KeyAdapter() {

    override fun keyTyped(e: KeyEvent) {
        val task = window.getSelectedTask()
        task?.let {
            if (e.keyChar == KeyEvent.VK_ENTER.toChar()) {
                taskManager.executeTask(task)
            }
        }
    }
}