package safronov.apps.domain.model.task

import safronov.apps.domain.model.task_category.TaskCategory

sealed class Task {

    data class TaskText(
        val title: String?,
        val text: String?,
        val date: String?,
        val taskCategory: TaskCategory,
        val taskType: TaskType?,
        val isPinned: Boolean?,
        val id: Long? = null
    ): Task()

    data class ListTask(
        val title: String?,
        val list: List<TaskListItem>?,
        val date: String?,
        val taskCategory: TaskCategory,
        val taskType: TaskType?,
        val isPinned: Boolean?,
        val id: Long? = null
    ): Task()

    data class TaskListItem(
        val title: String?,
        var isChecked: Boolean?
    )

    enum class TaskType {
        Text, List
    }

}
