package safronov.apps.domain.use_case.task_category.update

import kotlinx.coroutines.flow.first
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task_category.TaskCategory
import safronov.apps.domain.model.task_category.category_type.CategoryTypes
import safronov.apps.domain.repository.task_category.TaskCategoryRepository
import safronov.apps.domain.use_case.task_category.read.GetTaskCategoriesUseCase
import safronov.apps.domain.use_case.task_category.read.GetTaskCategoryByIdUseCase

class UpdateTaskCategoryUseCase(
    private val taskCategoryRepository: TaskCategoryRepository,
    private val getTaskCategoriesUseCase: GetTaskCategoriesUseCase,
    private val getTaskCategoryByIdUseCase: GetTaskCategoryByIdUseCase
) {

    suspend fun execute(taskCategory: TaskCategory): List<TaskCategory> {
        val currentCategory = getTaskCategoryByIdUseCase.execute(taskCategory.id.toString())
        if (currentCategory?.categoryType == CategoryTypes.System) {
            throw DomainException("Couldn't to change system task category")
        }
        taskCategoryRepository.updateTaskCategory(taskCategory = taskCategory)
        return getTaskCategoriesUseCase.execute().first()
    }

}
