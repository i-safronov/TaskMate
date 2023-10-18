package safronov.apps.domain.use_case.task_category.delete

import safronov.apps.domain.repository.task_category.TaskCategoryRepository

class ClearTaskCategoriesUseCase(
    private val taskCategoryRepository: TaskCategoryRepository
) {

    suspend fun execute() {
        taskCategoryRepository.clearTaskCategories()
    }

}
