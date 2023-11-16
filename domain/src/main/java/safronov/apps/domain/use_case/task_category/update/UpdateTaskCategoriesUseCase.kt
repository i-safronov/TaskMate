package safronov.apps.domain.use_case.task_category.update

import safronov.apps.domain.model.task_category.TaskCategory
import safronov.apps.domain.repository.task_category.TaskCategoryRepository

class UpdateTaskCategoriesUseCase(
    private val taskCategoryRepository: TaskCategoryRepository
) {

    suspend fun execute(categories: List<TaskCategory>) {
        taskCategoryRepository.updateTaskCategories(categories)
    }

}