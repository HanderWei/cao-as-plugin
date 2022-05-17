package com.ss.android.ugc.aweme.cao.plugin.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.ss.android.ugc.aweme.cao.plugin.CaoTaskManager
import com.ss.android.ugc.aweme.cao.plugin.TaskModel
import java.awt.GridLayout
import java.awt.event.ActionEvent
import javax.swing.*

class InputParamDialog(project: Project, val task: TaskModel, val taskManager: CaoTaskManager): DialogWrapper(project, true) {

    private val panel: JPanel = JPanel()
    private val fields: MutableList<JTextField> = mutableListOf()

    init {
        task.method?.paramsHint?.let { hints ->
            val gridLayout =  GridLayout(hints.size, 2, 3, 3)
            panel.layout = gridLayout
            for (hint in hints) {
                val label = JLabel(hint, JLabel.TRAILING)
                panel.add(label)
                val textField = JTextField(10)
                fields.add(textField)
                label.labelFor = textField
                panel.add(textField)
            }
        }


        isModal = true
        init()
        title = "Input Params"
    }

    override fun createCenterPanel(): JComponent? {
        return panel
    }

    override fun createActions(): Array<Action> {
        return arrayOf(okAction)
    }

    override fun getOKAction(): Action {
        val action = object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent?) {
                task.method?.params?.addAll(fields.map { it.text })
                taskManager.executeTask(task)
                this@InputParamDialog.disposeIfNeeded()
            }
        }
        action.putValue(Action.NAME, "RUN")
        return action
    }
}