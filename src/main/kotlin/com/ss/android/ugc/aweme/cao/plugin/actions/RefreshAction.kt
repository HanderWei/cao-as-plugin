package com.ss.android.ugc.aweme.cao.plugin.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.ss.android.ugc.aweme.cao.plugin.CaoInstance

class RefreshAction: BaseAction() {

    override fun actionPerformed(e: AnActionEvent) {
        e.project?.let {
            CaoInstance.refreshTasks()
        }
    }

}