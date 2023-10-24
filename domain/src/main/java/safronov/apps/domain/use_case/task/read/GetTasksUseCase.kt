package safronov.apps.domain.use_case.task.read

import safronov.apps.domain.model.task.Task
import safronov.apps.domain.repository.task.TaskRepository

class GetTasksUseCase(
    private val gettingTaskRepository: TaskRepository.GettingTask
) {

    suspend fun execute(): List<Task> {
        return gettingTaskRepository.getTasks()
    }

}
