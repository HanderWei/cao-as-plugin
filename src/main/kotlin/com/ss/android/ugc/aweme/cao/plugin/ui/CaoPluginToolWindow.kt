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
            override fun onTaskLoad(tasks: List<TaskModel>) {
                // 填充UI
                tableModel.updateData(tasks)
                updateTableMaxWidth()
            }

            override fun onInputParam(task: TaskModel) {
                val dialog = InputParamDialog(project, task, taskManager)
                dialog.showAndGet()
            }
        }
    }

    private val tableModel  = TaskTableModel()
    private val table = TaskTable(tableModel)

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
        queryField.addKeyListener(QueryKeyListener(this, queryField, taskManager, tableModel))
        queryField.preferredSize = Dimension(panel.width, 30)

        val taskPanel = SimpleToolWindowPanel(true, true)

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
        table.tableHeader?.reorderingAllowed = false
        table.rowSelectionAllowed = true
        table.fillsViewportHeight = true
        table.setRowHeight(0, 200)
        table.addMouseListener(TaskMouseListener(this, taskManager))
        table.addKeyListener(TaskKeyListener(this, taskManager))
        panel.add(JBScrollPane(table, JBScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JBScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER)
        updateTableMaxWidth()

        taskPanel.toolbar = queryField
        taskPanel.setContent(table)
        setContent(taskPanel)
    }

    fun updateTableMaxWidth() {
        table.columnModel?.getColumn(0)?.maxWidth = 30
//        table.columnModel?.getColumn(2)?.maxWidth = 60
    }

    fun getSelectedTask(): TaskModel? {
        val row = table.selectedRow
        if (row < 0 || taskManager.tasks.isEmpty() || row >= taskManager.tasks.size ) {
            return null
        }
        return taskManager.tasks[row]
    }
}