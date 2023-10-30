package safronov.apps.data.repository_impl.task

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import safronov.apps.data.data_source.local.model.converter.task.TaskEntityConverter
import safronov.apps.data.data_source.local.service.task.TaskService
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task.Task
import safronov.apps.domain.repository.task.TaskRepository
import java.lang.Exception
import java.lang.IllegalStateException

class TaskRepositoryImpl(
    private val taskService: TaskService,
    private val taskEntityConverter: TaskEntityConverter
) : TaskRepository.TaskRepositoryMutable {

    override suspend fun insertTaskText(task: Task.TaskText): Long? {
        try {
            val taskEntity = taskEntityConverter.convertTaskTextToTaskEntity(task)
            return taskService.insertTask(taskEntity)
        } catch (e: Exception) {
            throw DomainException(e.message, e)
        }
    }

    override suspend fun insertTaskList(task: Task.TaskList): Long? {
        try {
            val taskEntity = taskEntityConverter.convertTaskListToTaskEntity(task)
            return taskService.insertTask(taskEntity)
        } catch (e: Exception) {
            throw DomainException(e.message, e)
        }
    }

    override suspend fun getTasksAsFlow(): Flow<List<Task>> {
        try {
            return taskService.getTasksAsFlow().map { taskEntities ->
                val tasks = mutableListOf<Task>()

                taskEntities.forEach {
                    if (it.taskType == Task.TaskType.Text) {
                        tasks.add(taskEntityConverter.convertTaskEntityToTaskText(it))
                    } else if (it.taskType == Task.TaskType.List) {
                        tasks.add(taskEntityConverter.convertTaskEntityToTaskList(it))
                    } else {
                        throw IllegalStateException("task type didn't found")
                    }
                }

                tasks
            }
        } catch (e: Exception) {
            throw DomainException(e.message, e)
        }
    }

    override suspend fun getTasks(): List<Task> {
        TODO("Not yet implemented")
    }

    override suspend fun changeTaskText(task: Task.TaskText) {
        TODO("Not yet implemented")
    }

    override suspend fun changeTaskList(task: Task.TaskList) {
        TODO("Not yet implemented")
    }

    override suspend fun changeTasks(tasks: List<Task>) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTaskText(task: Task.TaskText) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTaskList(task: Task.TaskList) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTasks(tasks: List<Task>) {
        TODO("Not yet implemented")
    }

    override suspend fun getTasksByText(text: String): List<Task> {
        TODO("Not yet implemented")
    }

}
