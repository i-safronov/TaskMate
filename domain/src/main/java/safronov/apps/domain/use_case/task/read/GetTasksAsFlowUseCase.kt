package safronov.apps.domain.use_case.task.read

import kotlinx.coroutines.flow.Flow
import safronov.apps.domain.model.task.Task
import safronov.apps.domain.repository.task.TaskRepository

class GetTasksAsFlowUseCase(
    private val gettingTaskRepository: TaskRepository.GettingTask
) {

    suspend fun execute(): Flow<List<Task>> {
        return gettingTaskRepository.getTasksAsFlow()
    }

}
