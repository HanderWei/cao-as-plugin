package com.ss.android.ugc.aweme.cao.plugin.listener

import com.ss.android.ugc.aweme.cao.plugin.ui.CaoPluginToolWindow
import com.ss.android.ugc.aweme.cao.plugin.CaoTaskManager
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

class TaskMouseListener(val window: CaoPluginToolWindow, val taskManager: CaoTaskManager): MouseAdapter() {

    override fun mouseClicked(e: MouseEvent) {
        window.getSelectedTask()?.let { task ->
            // 双击
            if (e.button == MouseEvent.BUTTON1 && e.clickCount == 2) {
                taskManager.executeTask(task)
            }
        }
    }
}