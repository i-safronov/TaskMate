package safronov.apps.data.data_source.local.service.task

import kotlinx.coroutines.flow.Flow
import safronov.apps.data.data_source.local.model.task.TaskEntity
import safronov.apps.data.data_source.local.model.task_category.TaskCategoryEntity

interface TaskService {

    suspend fun insertTask(task: TaskEntity): Long?
    suspend fun getTasksAsFlow(): Flow<List<TaskEntity>>
    suspend fun getTasksAsFlowByTaskCategory(taskCategory: TaskCategoryEntity): Flow<List<TaskEntity>>
    suspend fun getTasks(): List<TaskEntity>
    suspend fun getTasksByText(text: String): List<TaskEntity>
    suspend fun changeTask(task: TaskEntity)
    suspend fun changeTasks(tasks: List<TaskEntity>)
    suspend fun deleteTask(task: TaskEntity)
    suspend fun deleteTasks(tasks: List<TaskEntity>)

}