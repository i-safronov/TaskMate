package safronov.apps.domain.use_case.task.delete

import safronov.apps.domain.model.task.Task
import safronov.apps.domain.repository.task.TaskRepository

class DeleteTasksUseCase(
    private val deletingTaskRepository: TaskRepository.DeletingTask
) {

    suspend fun execute(tasks: List<Task>) {
        deletingTaskRepository.deleteTasks(tasks = tasks)
    }

}
