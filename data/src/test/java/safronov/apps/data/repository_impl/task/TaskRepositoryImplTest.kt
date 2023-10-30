package safronov.apps.data.repository_impl.task

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.Assert.*
import safronov.apps.data.data_source.local.model.task.TaskEntity
import safronov.apps.data.data_source.local.service.task.TaskService
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task.Task

/*
*
* 1 - добавление
* 2 - получение флоу
* 3 - получение
* 4 - получение по тексту
* 5 - изменение
* 6 - изменение списка
* 7 - удаление
* 8 - удаление списка
* 9 - ошибка при добавлении
* 10 - ошибка при получении флоу
* 11 - ошибка при получении
* 12 - ошибка при получении по тексту
* 13 - ошибка при изменении
* 14 = ошибка при изменении списка
* 15 - ошибка при удалении
* 16 - ошибка при удалении списка
*
* */

class TaskRepositoryImplTest {



}

private class FakeTaskService: TaskService {
    
    var isNeedToThrowException = false
    var dataToReturn = mutableListOf(
        TaskEntity(
            title = "some title",
            content = "content",
            date = "today",
            taskCategoryId = 45,
            taskType = Task.TaskType.Text,
            isPinned = false,
            id = 435
        )
    )

    override suspend fun insertTask(task: TaskEntity): Long? {
        if (isNeedToThrowException) throw DomainException("some exception")
        dataToReturn.clear()
        dataToReturn.add(task)
        return task.id
    }

    override suspend fun getTasksAsFlow(): Flow<List<TaskEntity>> {
        if (isNeedToThrowException) throw DomainException("some exception")
        return flow {
            emit(dataToReturn)
        }
    }

    override suspend fun getTasks(): List<TaskEntity> {
        if (isNeedToThrowException) throw DomainException("some exception")
        return dataToReturn
    }

    override suspend fun getTasksByText(text: String): List<TaskEntity> {
        if (isNeedToThrowException) throw DomainException("some exception")
        return listOf(dataToReturn.first())
    }

    override suspend fun changeTask(task: TaskEntity) {
        if (isNeedToThrowException) throw DomainException("some exception")
    }

    override suspend fun changeTasks(tasks: List<TaskEntity>) {
        if (isNeedToThrowException) throw DomainException("some exception")
    }

    override suspend fun deleteTask(task: TaskEntity) {
        if (isNeedToThrowException) throw DomainException("some exception")
    }

    override suspend fun deleteTasks(tasks: List<TaskEntity>) {
        if (isNeedToThrowException) throw DomainException("some exception")
    }

}