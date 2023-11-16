package safronov.apps.domain.use_case.task.read

import kotlinx.coroutines.flow.Flow
import safronov.apps.domain.model.task.Task
import safronov.apps.domain.model.task_category.TaskCategory
import safronov.apps.domain.repository.task.TaskRepository

class GetTasksAsFlowByTaskCategory(
    private val taskRepository: TaskRepository.GettingTaskByParameters
) {

    suspend fun execute(taskCategory: TaskCategory): Flow<List<Task>> {
        return taskRepository.getTasksAsFlowByTaskCategory(taskCategory)
    }

}