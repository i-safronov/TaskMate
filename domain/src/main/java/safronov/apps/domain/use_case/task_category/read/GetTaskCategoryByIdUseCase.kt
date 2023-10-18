package safronov.apps.domain.use_case.task_category.read

import safronov.apps.domain.model.task_category.TaskCategory
import safronov.apps.domain.repository.task_category.TaskCategoryRepository

class GetTaskCategoryByIdUseCase(
    private val taskCategoryRepository: TaskCategoryRepository
) {

    suspend fun execute(id: String): TaskCategory? {
        return taskCategoryRepository.getTaskCategoryById(id = id)
    }

}
