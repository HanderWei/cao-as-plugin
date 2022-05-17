package com.ss.android.ugc.aweme.cao.plugin.ui

import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBList
import com.ss.android.ugc.aweme.cao.plugin.TaskModel
import java.awt.Component
import java.awt.FlowLayout
import javax.swing.*

class TaskListModel: DefaultListModel<TaskListItemView>() {

}

class TaskList : JBList<TaskListItemView>() {

    init {
        model = TaskListModel()
        cellRenderer = TaskCellRenderer()
        selectionMode = ListSelectionModel.SINGLE_SELECTION

    }

    fun updateTasks(list: List<TaskModel>) {
        list.forEach {
            (model as DefaultListModel).addElement(TaskListItemView(it))
        }
    }

}

class TaskCellRenderer() : ListCellRenderer<TaskListItemView> {

    override fun getListCellRendererComponent(list: JList<out TaskListItemView>, value: TaskListItemView, index: Int, isSelected: Boolean, cellHasFocus: Boolean): Component {
        return value
    }

}


class TaskListItemView(task: TaskModel): JComponent() {
    init {
        layout = FlowLayout()
        add(JBLabel(task.title))
        add(JBLabel(task.subTitle))
    }
}