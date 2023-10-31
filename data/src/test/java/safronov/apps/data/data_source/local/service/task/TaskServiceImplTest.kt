package safronov.apps.data.data_source.local.service.task

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.Assert.*

import org.junit.Before
import safronov.apps.data.data_source.local.app_db.dao_sql.TaskDao
import safronov.apps.data.data_source.local.model.task.TaskEntity
import safronov.apps.data.exception.DataException
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task.Task

class TaskServiceImplTest {

    @Before
    fun setUp() {
    }

}

private class FakeTaskDao: TaskDao {

    var isNeedToThrowException = false
    var deletedItemId: String? = null
    var deletedItemIds = mutableListOf<String>()
    var dataToReturn = mutableListOf(
        TaskEntity(
            title = "title",
            content = "fas",
            date = "date",
            taskCategoryId = 54,
            taskType = Task.TaskType.Text,
            isPinned = false,
            id = 54321
        )
    )

    override fun insertTask(task: TaskEntity): Long? {
        if (isNeedToThrowException) throw DataException("some exception")
        dataToReturn.clear()
        dataToReturn.add(task)
        return task.id
    }

    override fun getTasksAsFlow(): Flow<List<TaskEntity>> {
        if (isNeedToThrowException) throw DataException("some exception")
        return flow {
            emit(dataToReturn)
        }
    }

    override fun getTasks(): List<TaskEntity> {
        if (isNeedToThrowException) throw DataException("some exception")
        return dataToReturn
    }

    override fun getTasksByText(text: String): List<TaskEntity> {
        if (isNeedToThrowException) throw DataException("some exception")
        return dataToReturn
    }

    override fun changeTask(task: TaskEntity) {
        if (isNeedToThrowException) throw DataException("some exception")
        dataToReturn.clear()
        dataToReturn.add(task)
    }

    override fun changeTasks(tasks: List<TaskEntity>) {
        if (isNeedToThrowException) throw DataException("some exception")
        dataToReturn = tasks.toMutableList()
    }

    override fun deleteTask(task: TaskEntity) {
        if (isNeedToThrowException) throw DataException("some exception")
        deletedItemId = task.id.toString()
        dataToReturn.clear()
    }

    override fun deleteTasks(tasks: List<TaskEntity>) {
        if (isNeedToThrowException) throw DataException("some exception")
        tasks.forEach {
            deletedItemIds.add(it.id.toString())
        }
        dataToReturn.clear()
    }

}