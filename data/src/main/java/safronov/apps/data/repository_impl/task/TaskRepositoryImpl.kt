package safronov.apps.data.repository_impl.task

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import safronov.apps.data.data_source.local.model.converter.task.TaskEntityConverter
import safronov.apps.data.data_source.local.model.task.TaskEntity
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
                    tasks.add(getTaskByTaskType(it))
                }
                tasks
            }
        } catch (e: Exception) {
            throw DomainException(e.message, e)
        }
    }

    override suspend fun getTasks(): List<Task> {
        try {
            return taskService.getTasks().map {
                getTaskByTaskType(it)
            }
        } catch (e: Exception) {
            throw DomainException(e.message, e)
        }
    }

    override suspend fun getTasksByText(text: String): List<Task> {
        try {
            return taskService.getTasksByText(text).map {
                getTaskByTaskType(it)
            }
        } catch (e: Exception) {
            throw DomainException(e.message, e)
        }
    }

    override suspend fun changeTaskText(task: Task.TaskText) {
        try {
            taskService.changeTask(taskEntityConverter.convertTaskTextToTaskEntity(task))
        } catch (e: Exception) {
            throw DomainException(e.message, e)
        }
    }

    override suspend fun changeTaskList(task: Task.TaskList) {
        try {
            taskService.changeTask(taskEntityConverter.convertTaskListToTaskEntity(task))
        } catch (e: Exception) {
            throw DomainException(e.message, e)
        }
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

    private fun getTaskByTaskType(task: TaskEntity): Task {
        if (task.taskType == Task.TaskType.Text) {
            return taskEntityConverter.convertTaskEntityToTaskText(task)
        } else if (task.taskType == Task.TaskType.List) {
            return taskEntityConverter.convertTaskEntityToTaskList(task)
        } else {
            throw IllegalStateException("task type didn't found")
        }
    }

}
