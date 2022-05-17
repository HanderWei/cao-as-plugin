package com.ss.android.ugc.aweme.cao.plugin.ui

import com.intellij.ui.table.JBTable
import java.awt.Point
import java.awt.event.MouseEvent
import javax.swing.table.JTableHeader
import javax.swing.table.TableColumnModel

import com.ss.android.ugc.aweme.cao.plugin.TaskModel
import javax.swing.table.DefaultTableModel

class TaskTableModel : DefaultTableModel(arrayOf<Any>("info"), 1) {

    companion object {
        val columnName = arrayOf("ID", "Title", "Author")
    }

    init {
        val dataVector = Array(1) { arrayOfNulls<Any>(columnName.size) }
        setDataVector(dataVector, columnName)
    }

    fun updateData(list: List<TaskModel>) {
        val dataVector = Array(list.size) { arrayOfNulls<Any>(columnName.size) }
        for (i in list.indices) {
            val line = arrayOfNulls<Any>(columnName.size)
            line[0] = i + 1
            line[1] = list[i].title
            line[2] = list[i].subTitle
            dataVector[i] = line
        }
        setDataVector(dataVector, columnName)
    }

    override fun isCellEditable(row: Int, column: Int): Boolean {
        return false
    }
}

class TaskTable(tableModel: TaskTableModel): JBTable(tableModel) {

    override fun createDefaultTableHeader(): JTableHeader {
        return TableHeader(columnModel)
    }
}

class TableHeader(columnModel: TableColumnModel): JTableHeader(columnModel) {

    override fun getToolTipText(e: MouseEvent): String {
        val p: Point = e.point
        val index = columnModel.getColumnIndexAtX(p.x)
        val realIndex = columnModel.getColumn(index).modelIndex
        return TaskTableModel.columnName[realIndex]
    }

}