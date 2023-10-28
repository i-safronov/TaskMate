package safronov.apps.data.repository_impl.task

import kotlinx.coroutines.flow.Flow
import safronov.apps.domain.model.task.Task
import safronov.apps.domain.repository.task.TaskRepository

class TaskRepositoryImpl: TaskRepository.TaskRepositoryMutable {

    override suspend fun insertTaskText(task: Task.TaskText): Long? {
        TODO("Not yet implemented")
    }

    override suspend fun insertTaskList(task: Task.TaskList): Long? {
        TODO("Not yet implemented")
    }

    override suspend fun getTasksAsFlow(): Flow<List<Task>> {
        TODO("Not yet implemented")
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


}