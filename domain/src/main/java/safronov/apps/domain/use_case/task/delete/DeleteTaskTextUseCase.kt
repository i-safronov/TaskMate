package safronov.apps.domain.use_case.task.delete

import safronov.apps.domain.model.task.Task
import safronov.apps.domain.repository.task.TaskRepository

class DeleteTaskTextUseCase(
    private val deletingTaskRepository: TaskRepository.DeletingTask
) {

    suspend fun execute(task: Task.TaskText) {
        deletingTaskRepository.deleteTaskText(task = task)
    }

}
