package safronov.apps.data.data_source.local.service.task

import kotlinx.coroutines.flow.Flow
import safronov.apps.data.data_source.local.app_db.dao_sql.TaskDao
import safronov.apps.data.data_source.local.model.task.TaskEntity
import safronov.apps.data.exception.DataException
import java.lang.Exception

class TaskServiceImpl(
    private val taskDao: TaskDao
): TaskService {

    override suspend fun insertTask(task: TaskEntity): Long? {
        try {
            return taskDao.insertTask(task)
        } catch (e: Exception) {
            throw DataException(e.message, e)
        }
    }

    override suspend fun getTasksAsFlow(): Flow<List<TaskEntity>> {
        try {
            return taskDao.getTasksAsFlow()
        } catch (e: Exception) {
            throw DataException(e.message, e)
        }
    }

    override suspend fun getTasks(): List<TaskEntity> {
        try {
            return taskDao.getTasks()
        } catch (e: Exception) {
            throw DataException(e.message, e)
        }
    }

    override suspend fun getTasksByText(text: String): List<TaskEntity> {
        try {
            return taskDao.getTasksByText(text)
        } catch (e: Exception) {
            throw DataException(e.message, e)
        }
    }

    override suspend fun changeTask(task: TaskEntity) {
        try {
            taskDao.changeTask(task)
        } catch (e: Exception) {
            throw DataException(e.message, e)
        }
    }

    override suspend fun changeTasks(tasks: List<TaskEntity>) {
        try {
            taskDao.changeTasks(tasks)
        } catch (e: Exception) {
            throw DataException(e.message, e)
        }
    }

    override suspend fun deleteTask(task: TaskEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTasks(tasks: List<TaskEntity>) {
        TODO("Not yet implemented")
    }

}
