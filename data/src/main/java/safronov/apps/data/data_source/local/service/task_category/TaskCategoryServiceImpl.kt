package safronov.apps.data.data_source.local.service.task_category

import kotlinx.coroutines.flow.Flow
import safronov.apps.data.data_source.local.app_db.dao.TaskCategoryDao
import safronov.apps.data.data_source.local.model.task_category.TaskCategoryEntity
import safronov.apps.data.exception.DataException

class TaskCategoryServiceImpl(
    private val taskCategoryDao: TaskCategoryDao
): TaskCategoryService {

    override suspend fun insertTaskCategories(list: List<TaskCategoryEntity>) {
        try {
            taskCategoryDao.insertTaskCategories(list)
        } catch (e: Exception) {
            throw DataException(e.message, e)
        }
    }

    override suspend fun getTaskCategories(): Flow<List<TaskCategoryEntity>> {
        try {
            return taskCategoryDao.getTaskCategories()
        } catch (e: Exception) {
            throw DataException(e.message, e)
        }
    }

    override suspend fun getTaskCategoryById(id: String): TaskCategoryEntity? {
        try {
            return taskCategoryDao.getTaskCategoryById(id)
        } catch (e: Exception) {
            throw DataException(e.message, e)
        }
    }

    override suspend fun updateTaskCategory(taskCategory: TaskCategoryEntity) {
        try {
            taskCategoryDao.updateTaskCategory(taskCategory)
        } catch (e: Exception) {
            throw DataException(e.message, e)
        }
    }

    override suspend fun clearTaskCategories() {
        try {
            taskCategoryDao.clearTaskCategories()
        } catch (e: Exception) {
            throw DataException(e.message, e)
        }
    }

}
