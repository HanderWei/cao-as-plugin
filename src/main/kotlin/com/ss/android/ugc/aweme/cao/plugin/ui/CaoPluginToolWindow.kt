package com.ss.android.ugc.aweme.cao.plugin.ui

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextField
import com.ss.android.ugc.aweme.cao.plugin.CaoTaskManager
import com.ss.android.ugc.aweme.cao.plugin.TaskLoadListener
import com.ss.android.ugc.aweme.cao.plugin.TaskModel
import com.ss.android.ugc.aweme.cao.plugin.listener.QueryKeyListener
import com.ss.android.ugc.aweme.cao.plugin.listener.TaskKeyListener
import com.ss.android.ugc.aweme.cao.plugin.listener.TaskMouseListener
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.BoxLayout
import javax.swing.JPanel
import javax.swing.ListSelectionModel

class CaoPluginToolWindow(val project: Project): SimpleToolWindowPanel(true) {

    private val taskManager: CaoTaskManager by lazy { CaoTaskManager(taskLoadListener, project) }
    private val taskLoadListener by lazy {
        object : TaskLoadListener {
            override fun showTasks(tasks: List<TaskModel>) {
                updateList(tasks)
            }

            override fun onInputParam(task: TaskModel) {
                val dialog = InputParamDialog(project, task, taskManager)
                dialog.showAndGet()
            }
        }
    }

    fun updateList(tasks: List<TaskModel>) {
        taskList.updateTasks(tasks)
        taskManager.currentTasks = tasks as MutableList<TaskModel>
    }

    private val taskList = TaskList()

    init {
        showPanel()
        // 获取数据
        ApplicationManager.getApplication().invokeLater {
            taskManager.loadData()
        }
    }

    private fun showPanel() {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        val queryField = JBTextField()
        queryField.toolTipText = "Enter Search"
        queryField.setTextToTriggerEmptyTextStatus("Enter Search")
        queryField.addKeyListener(QueryKeyListener(queryField, taskManager, this))
        queryField.preferredSize = Dimension(panel.width, 30)

        val taskPanel = SimpleToolWindowPanel(true, true)
        taskList.addKeyListener(TaskKeyListener(this, taskManager))
        taskList.addMouseListener(TaskMouseListener(this, taskManager))
        taskPanel.setContent(taskList)
        taskPanel.toolbar = queryField

        panel.add(taskPanel)
        setContent(panel)
    }

    fun getSelectedTask(): TaskModel? {
        val row = taskList.selectedIndex
        if (row < 0 || taskManager.currentTasks.isEmpty() || row >= taskManager.currentTasks.size ) {
            return null
        }
        return taskManager.currentTasks[row]
    }
}