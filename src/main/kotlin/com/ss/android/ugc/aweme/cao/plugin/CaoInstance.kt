package com.ss.android.ugc.aweme.cao.plugin

import com.ss.android.ugc.aweme.cao.plugin.ui.CaoPluginToolWindow

object CaoInstance {

    var taskManager: CaoTaskManager? = null
    var window: CaoPluginToolWindow? = null

    fun showAllTasks() {
        window?.showAllTasks()
    }

    fun refreshTasks() {
        taskManager?.loadData()
    }
}