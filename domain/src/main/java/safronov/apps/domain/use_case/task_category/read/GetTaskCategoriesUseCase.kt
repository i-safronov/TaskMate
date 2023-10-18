package safronov.apps.domain.use_case.task_category.read

import kotlinx.coroutines.flow.Flow
import safronov.apps.domain.model.task_category.TaskCategory
import safronov.apps.domain.repository.task_category.TaskCategoryRepository

class GetTaskCategoriesUseCase(
    private val taskCategoryRepository: TaskCategoryRepository
) {

    suspend fun execute(): Flow<List<TaskCategory>> {
        return taskCategoryRepository.getTaskCategories()
    }

}
