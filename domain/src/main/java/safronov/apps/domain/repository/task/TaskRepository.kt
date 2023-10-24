package safronov.apps.domain.repository.task

import kotlinx.coroutines.flow.Flow
import safronov.apps.domain.model.task.Task

interface TaskRepository {

    interface InsertingTask: TaskRepository {
        suspend fun insertTaskText(task: Task.TaskText)
        suspend fun insertTaskList(task: Task.TaskList)
    }

    interface GettingTask: TaskRepository {
        suspend fun getTasksAsFlow(): Flow<List<Task>>
        suspend fun getTasks(): List<Task>
    }

    interface ChangingTask: TaskRepository {
        suspend fun changeTaskText(task: Task.TaskText)
        suspend fun changeTaskList(task: Task.TaskList)
        suspend fun changeTasks(tasks: List<Task>)
    }

    interface DeletingTask: TaskRepository {
        suspend fun deleteTaskText(task: Task.TaskText)
        suspend fun deleteTaskList(task: Task.TaskList)
        suspend fun deleteTasks(tasks: List<Task>)
    }

    interface TaskRepositoryMutable: InsertingTask, GettingTask, ChangingTask, DeletingTask

}