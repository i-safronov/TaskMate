package safronov.apps.data.data_source.local.service.task_category

import kotlinx.coroutines.flow.Flow
import safronov.apps.data.data_source.local.model.task_category.TaskCategoryEntity

interface TaskCategoryService {

    suspend fun insertTaskCategories(list: List<TaskCategoryEntity>)
    suspend fun getTaskCategories(): Flow<List<TaskCategoryEntity>>
    suspend fun getTaskCategoryById(id: String): TaskCategoryEntity?
    suspend fun updateTaskCategory(taskCategory: TaskCategoryEntity)
    suspend fun updateTaskCategories(categories: List<TaskCategoryEntity>)
    suspend fun clearTaskCategories()

}