package safronov.apps.domain.model.task

sealed class Task {

    data class TaskText(
        var title: String?,
        var text: String?,
        var date: String?,
        var taskCategoryId: Long?,
        val taskType: TaskType?,
        var isPinned: Boolean?,
        val id: Long? = null
    ): Task()

    data class TaskList(
        val title: String?,
        val list: List<TaskListItem>?,
        val date: String?,
        val taskCategoryId: Long?,
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
