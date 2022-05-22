package com.ss.android.ugc.aweme.cao.plugin

data class TaskModel(
        val key: String = "",
        val shortKey: String = "",
        val title: String,
        val subTitle: String = "",
        val ownerName: String = "",
        val emailPrefix: String = "",
        val outputType: Int = 0,
        val options: List<TaskModel> = emptyList(),
        val method: ExecutableMethod? = null,
        val forceInputParams: Boolean = false
)

data class ExecutableMethod(
        val clazzName: String,
        val methodName: String,
        val params: MutableList<String> = mutableListOf(),
        val paramsType: List<String> = mutableListOf(),
        val paramsHint: List<String> = mutableListOf(),
        val returnType: String = "",
) {

    companion object {
        fun getListFormatString(list: List<String>?) : String {
            val sb = java.lang.StringBuilder()
            sb.append('[')
            try {
                list?.forEach {
                    sb.append(it).append(',')
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return ""
            }
            sb.deleteCharAt(sb.length - 1)
            sb.append(']')
            return sb.toString()
        }
    }
}

enum class OutputType(val value: Int) {
    CLIPBOARD(0), LIST(1), FILE(2)
}

fun TaskModel.match(search: String): Boolean {
    return key.contains(search, true)
            || shortKey.contains(search, true)
            || title.contains(search, true)
            || subTitle.contains(search, true)
            || ownerName.contains(search, true)
            || emailPrefix.contains(search, true)
}