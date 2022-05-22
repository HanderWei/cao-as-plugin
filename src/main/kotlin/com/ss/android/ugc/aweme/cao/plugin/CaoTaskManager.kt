package com.ss.android.ugc.aweme.cao.plugin

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.ss.android.ugc.aweme.cao.plugin.ExecutableMethod.Companion.getListFormatString
import com.ss.android.ugc.aweme.cao.plugin.service.PersistentService
import okio.buffer
import okio.sink
import okio.source
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.ClipboardOwner
import java.awt.datatransfer.StringSelection
import java.awt.datatransfer.Transferable
import java.io.File
import java.lang.reflect.Type
import java.net.Socket
import java.text.SimpleDateFormat
import java.util.*


interface TaskLoadListener {
    fun showTasks(tasks: List<TaskModel>)
    fun onInputParam(task: TaskModel)
}

class CaoTaskManager(private val listener: TaskLoadListener, private val project: Project) {

    companion object {
        const val HOST = "localhost"
        const val PC_PORT = 19091
        const val TASK_TAG = "Zmlyc3QgdGFzaw=="
    }

    val tasks = mutableListOf<TaskModel>()
    val tempResult = mutableListOf<TaskModel>()

    var currentTasks = mutableListOf<TaskModel>()

    fun loadData() {
        // 创建端口转发
        createADBForward()

        // 从服务端获取数据
        getTaskDataFromServer()
    }

    // 创建端口转发
    private fun createADBForward() {
        Runtime.getRuntime().exec(arrayOf("adb", "forward", "tcp:19091", "tcp:19191"))
    }

    private fun getTaskDataFromServer() {
        try {
            Thread.sleep(300)
            println("getTaskDataFromServer")
            val socket = Socket(HOST, PC_PORT)
            val sink = socket.sink().buffer()
            val source = socket.source().buffer()
            sink.writeUtf8("\n").flush()
            val content = source.readUtf8Line()
            println("content: $content")
            val listOfTaskObject: Type = object : TypeToken<ArrayList<TaskModel?>?>() {}.type
            tasks.clear()
            tasks.addAll(Gson().fromJson(content, listOfTaskObject))

            println("tasks size ${tasks.size}")

            listener.showTasks(tasks)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun executeTask(task: TaskModel) {
        if (task.method != null && task.method.params.size != task.method.paramsType.size) {
            if (!task.forceInputParams && PersistentService.getData(task)?.isNotEmpty() == true) {
                val newTask = task.copy(forceInputParams = true, title = "重新输入参数", subTitle = "")
                val oldParamsList = PersistentService.getData(task) ?: emptyList()
                val newList = mutableListOf<TaskModel>()
                newList.add(newTask)
                oldParamsList.forEach {
                    newList.add(it.copy(title = getListFormatString(it.method?.params), subTitle = getListFormatString(it.method?.paramsHint)))
                }
                listener.showTasks(newList)
            } else {
                listener.onInputParam(task)
            }
            return
        }
        if (task.options.isEmpty()) {
            val socket = Socket(HOST, PC_PORT)
            val sink = socket.sink().buffer()
            val source = socket.source().buffer()
            sink.writeUtf8(TASK_TAG).writeUtf8("\n").flush()
            sink.writeUtf8(Gson().toJson(task)).flush()
            sink.writeUtf8("\n").flush()
            val content = source.readUtf8Line()

            if (task.method?.params?.isNotEmpty() == true) {
                PersistentService.putData(task)
            }

            when (task.outputType) {
                OutputType.CLIPBOARD.value -> {
                    writeToClipboard(content, null)
                    showNotification(content)
                    CaoInstance.refreshTasks()
                }
                OutputType.FILE.value -> {
                    saveToFile(content)
                    showNotification(content)
                    CaoInstance.refreshTasks()
                }
                OutputType.LIST.value -> {
                    val listOfTaskObject: Type = object : TypeToken<ArrayList<TaskModel?>?>() {}.type
                    tempResult.clear()
                    tempResult.addAll(Gson().fromJson(content, listOfTaskObject))
                    listener.showTasks(tempResult)
                }
            }
        } else {
            listener.showTasks(task.options)
        }
    }

    private fun writeToClipboard(s: String?, owner: ClipboardOwner?) {
        val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
        val transferable: Transferable = StringSelection(s)
        clipboard.setContents(transferable, owner)
    }

    private fun saveToFile(content: String?) {
        val home = System.getProperty("user.home")
        val fileName: String = SimpleDateFormat("yyyy-MM-dd-HH_mm").format(Date())
        val file = File("$home/Downloads/$fileName.json")
        file.sink().buffer().writeUtf8(content ?: "NO DATA").flush()
        LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file)?.let {
            FileEditorManager.getInstance(project).openFile(it, true)
        }
    }

    private fun showNotification(content: String?) {
        NotificationGroupManager.getInstance()
                .getNotificationGroup("Cao")
                .createNotification(content ?: "任务完成", NotificationType.INFORMATION)
                .notify(project)
    }

}