package safronov.apps.data.repository_impl.task_category

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import safronov.apps.data.data_source.local.model.task_category.TaskCategoryEntity
import safronov.apps.data.data_source.local.service.task_category.TaskCategoryService
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task_category.TaskCategory
import safronov.apps.domain.repository.task_category.TaskCategoryRepository

class TaskCategoryRepositoryImpl(
    private val taskCategoryService: TaskCategoryService
): TaskCategoryRepository {

    override suspend fun insertTaskCategories(list: List<TaskCategory>) {
        try {
            taskCategoryService.insertTaskCategories(
                list = TaskCategoryEntity.convertListOfTaskCategoryToListOfTaskCategoryEntity(list)
            )
        } catch (e: Exception) {
            throw DomainException(e.message, e)
        }
    }

    override suspend fun getTaskCategories(): Flow<List<TaskCategory>> {
        try {
            return taskCategoryService.getTaskCategories().map {
                TaskCategoryEntity.convertListOfTaskCategoryEntityToListOfTaskCategory(it)
            }
        } catch (e: Exception) {
            throw DomainException(e.message, e)
        }
    }

    override suspend fun getTaskCategoryById(id: String): TaskCategory? {
        try {
            val itemById = taskCategoryService.getTaskCategoryById(id = id) ?: return null
            return TaskCategoryEntity.convertTaskCategoryEntityToTaskCategory(itemById)
        } catch (e: Exception) {
            throw DomainException(e.message, e)
        }
    }

    override suspend fun updateTaskCategory(taskCategory: TaskCategory) {
        try {
            val taskCategoryEntity = TaskCategoryEntity.convertTaskCategoryToTaskCategoryEntity(taskCategory)
            taskCategoryService.updateTaskCategory(taskCategory = taskCategoryEntity)
        } catch (e: Exception) {
            throw DomainException(e.message, e)
        }
    }

    override suspend fun clearTaskCategories() {
        try {
            taskCategoryService.clearTaskCategories()
        } catch (e: Exception) {
            throw DomainException(e.message, e)
        }
    }

}
