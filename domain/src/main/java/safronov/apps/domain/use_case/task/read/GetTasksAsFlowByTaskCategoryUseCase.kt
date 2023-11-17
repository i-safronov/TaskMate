package safronov.apps.domain.use_case.task.read

import kotlinx.coroutines.flow.Flow
import safronov.apps.domain.model.task.Task
import safronov.apps.domain.model.task_category.TaskCategory
import safronov.apps.domain.model.task_category.category_type.CategoryTypes
import safronov.apps.domain.repository.task.TaskRepository

class GetTasksAsFlowByTaskCategoryUseCase(
    private val taskRepository: TaskRepository.GettingTaskByParameters,
    private val taskRepositoryGetting: TaskRepository.GettingTask
) {

    suspend fun execute(taskCategory: TaskCategory): Flow<List<Task>> {
        return if (taskCategory.categoryType == CategoryTypes.System) {
            taskRepositoryGetting.getTasksAsFlow()
        } else {
            taskRepository.getTasksAsFlowByTaskCategory(taskCategory)
        }
    }

}