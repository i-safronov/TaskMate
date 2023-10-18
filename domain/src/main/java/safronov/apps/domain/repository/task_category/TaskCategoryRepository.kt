package safronov.apps.domain.repository.task_category

import kotlinx.coroutines.flow.Flow
import safronov.apps.domain.model.task_category.TaskCategory

interface TaskCategoryRepository {

    fun insertTaskCategories(list: List<TaskCategory>)
    fun getTaskCategories(): Flow<List<TaskCategory>>
    fun clearTaskCategories()

}