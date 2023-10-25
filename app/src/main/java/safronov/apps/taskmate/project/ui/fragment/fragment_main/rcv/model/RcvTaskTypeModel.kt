package safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.model

import safronov.apps.domain.model.task.Task


data class RcvTaskTypeModel(
    val title: String,
    val img: Int,
    val taskType: Task.TaskType
)
