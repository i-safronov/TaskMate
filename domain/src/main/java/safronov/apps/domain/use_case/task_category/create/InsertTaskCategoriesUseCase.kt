package safronov.apps.domain.use_case.task_category.create

import kotlinx.coroutines.flow.first
import safronov.apps.domain.model.task_category.TaskCategory
import safronov.apps.domain.repository.task_category.TaskCategoryRepository
import safronov.apps.domain.use_case.task_category.read.GetTaskCategoriesUseCase

class InsertTaskCategoriesUseCase(
    private val taskCategoryRepository: TaskCategoryRepository,
    private val getTaskCategoriesUseCase: GetTaskCategoriesUseCase
) {

    suspend fun execute(list: List<TaskCategory>): List<TaskCategory> {
        taskCategoryRepository.insertTaskCategories(list)
        return getTaskCategoriesUseCase.execute().first()
    }

}
