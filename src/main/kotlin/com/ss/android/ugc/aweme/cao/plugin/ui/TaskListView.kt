package com.ss.android.ugc.aweme.cao.plugin.ui

import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBList
import com.ss.android.ugc.aweme.cao.plugin.TaskModel
import java.awt.*
import javax.swing.*
import javax.swing.border.Border

class TaskListModel : DefaultListModel<TaskListItemView>() {

}

class TaskListView : JBList<TaskListItemView>() {

    init {
        model = TaskListModel()
        cellRenderer = TaskCellRenderer()
        selectionMode = ListSelectionModel.SINGLE_SELECTION

    }

    fun updateTasks(list: List<TaskModel>) {
        (model as DefaultListModel).clear()
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


class TaskListItemView(task: TaskModel) : JComponent() {
    init {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        this.border = object: Border {
            override fun paintBorder(c: Component?, g: Graphics?, x: Int, y: Int, width: Int, height: Int) {

            }

            override fun getBorderInsets(c: Component?): Insets {
                return Insets(5, 10, 5, 16)
            }

            override fun isBorderOpaque(): Boolean {
                return true
            }

        }
        add(
            JBLabel(task.title).apply {
                font = Font(this.font.name, Font.BOLD, 15)
            }
        )
        add(
            JBLabel(task.subTitle).apply {
                font = Font(this.font.name, Font.PLAIN, 12)
            }
        )
    }
}