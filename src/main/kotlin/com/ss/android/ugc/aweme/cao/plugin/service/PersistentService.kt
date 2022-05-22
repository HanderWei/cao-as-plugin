package com.ss.android.ugc.aweme.cao.plugin.service

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.util.xmlb.XmlSerializerUtil
import com.ss.android.ugc.aweme.cao.plugin.TaskModel
import kotlin.collections.ArrayList

@State(name = "CaoPersistent.xml")
object PersistentService: PersistentStateComponent<PersistentService> {

    const val MAX_ITEM_CACHE_SIZE = 10

    val cacheData: MutableMap<String, ArrayList<TaskModel>> = mutableMapOf()

    override fun getState(): PersistentService {
        return this
    }

    override fun loadState(state: PersistentService) {
        XmlSerializerUtil.copyBean(state, this)
    }

    fun putData(taskModel: TaskModel) {
        if (cacheData.containsKey(taskModel.key)) {
            val oldList = cacheData[taskModel.key]!!
            var hasSame = false
            oldList.forEach {
                if (it.method?.params == taskModel.method?.params) {
                    hasSame = true
                }
            }
            if (!hasSame) {
                oldList.add(0, taskModel)
            }
            for (i in (oldList.size - 1) downTo MAX_ITEM_CACHE_SIZE ) {
                oldList.removeAt(i)
            }
        } else {
            val newList = ArrayList<TaskModel>()
            newList.add(taskModel)
            cacheData[taskModel.key] = newList
        }
    }

    fun getData(taskModel: TaskModel): List<TaskModel>? {
        return cacheData[taskModel.key]
    }

}