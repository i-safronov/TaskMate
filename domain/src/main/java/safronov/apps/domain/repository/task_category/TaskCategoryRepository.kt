package safronov.apps.domain.repository.task_category

import kotlinx.coroutines.flow.Flow
import safronov.apps.domain.model.task_category.TaskCategory

interface TaskCategoryRepository {

    suspend fun insertTaskCategories(list: List<TaskCategory>)
    suspend fun getTaskCategories(): Flow<List<TaskCategory>>
    suspend fun getTaskCategoryById(id: String): TaskCategory?
    suspend fun updateTaskCategory(taskCategory: TaskCategory)
    suspend fun updateTaskCategories(categories: List<TaskCategory>) {}
    suspend fun clearTaskCategories()

}