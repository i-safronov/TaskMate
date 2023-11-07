package safronov.apps.domain.model.task

import java.io.Serializable

sealed class Task {

    data class TaskText(
        var title: String?,
        var text: String?,
        var date: String?,
        var taskCategoryId: Long?,
        val taskType: TaskType?,
        var isPinned: Boolean?,
        var id: Long? = null
    ): Task(), Serializable

    data class TaskList(
        var title: String?,
        var list: List<TaskListItem>?,
        var date: String?,
        var taskCategoryId: Long?,
        val taskType: TaskType?,
        var isPinned: Boolean?,
        var id: Long? = null
    ): Task(), Serializable

    data class TaskListItem(
        var title: String?,
        var isChecked: Boolean?
    ): Serializable

    enum class TaskType {
        Text, List
    }

}
