package com.ss.android.ugc.aweme.cao.plugin.ui

import com.intellij.ide.DataManager
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.content.ContentFactory
import com.ss.android.ugc.aweme.cao.plugin.PluginConstants
import java.util.concurrent.atomic.AtomicReference

class CaoPluginToolWindowFactory: ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val window = CaoPluginToolWindow(project)
        val contentFactory = ContentFactory.SERVICE.getInstance()
        val content = contentFactory.createContent(window.content, "", false)
        toolWindow.contentManager.addContent(content);
    }

    companion object {
        fun getDataContext(project: Project): DataContext {
            val dataContext = AtomicReference<DataContext>()
            ApplicationManager.getApplication().invokeAndWait {
                val window = ToolWindowManager.getInstance(project).getToolWindow(PluginConstants.TOOL_WINDOW_ID)
                dataContext.set(DataManager.getInstance().getDataContext(window?.contentManager?.getContent(0)?.component))
            }
            return dataContext.get()
        }
    }
}