package safronov.apps.data.data_source.local.service.task

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import safronov.apps.data.data_source.local.app_db.dao_sql.TaskDao
import safronov.apps.data.data_source.local.model.task.TaskEntity
import safronov.apps.data.exception.DataException
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task.Task
import java.lang.IllegalStateException

class TaskServiceImplTest {

    private lateinit var fakeTaskDao: FakeTaskDao
    private lateinit var taskService: TaskService
    private lateinit var dataToInsert: TaskEntity

    @Before
    fun setUp() {
        fakeTaskDao = FakeTaskDao()
        taskService = TaskServiceImpl(taskDao = fakeTaskDao)
        dataToInsert = TaskEntity(
            title = "asd",
            content = "asertef",
            date = "qerwqrwe",
            taskCategoryId = 3,
            taskType = Task.TaskType.Text,
            isPinned = true,
            id = 252345
        )
    }

    @Test
    fun test_insertTask() = runBlocking {
        assertEquals(true, fakeTaskDao.dataToReturn.first() != dataToInsert)
        taskService.insertTask(dataToInsert)
        assertEquals(true, fakeTaskDao.dataToReturn.first() == dataToInsert)
    }

    @Test(expected = DataException::class)
    fun test_insertTask_shouldThrowException() = runBlocking {
        fakeTaskDao.isNeedToThrowException = true
        assertEquals(true, fakeTaskDao.dataToReturn.first() != dataToInsert)
        taskService.insertTask(dataToInsert)
        assertEquals(true, fakeTaskDao.dataToReturn.first() == dataToInsert)
    }

    @Test
    fun test_getTasksAsFlow() = runBlocking {
        assertEquals(true, fakeTaskDao.dataToReturn.first() != dataToInsert)
        taskService.insertTask(dataToInsert)
        assertEquals(true, fakeTaskDao.dataToReturn.first() == dataToInsert)
        val data = taskService.getTasksAsFlow().first().first()
        assertEquals(true, data == dataToInsert)
    }

    @Test(expected = DataException::class)
    fun test_getTasksAsFlow_shouldThrowException() = runBlocking {
        fakeTaskDao.isNeedToThrowException = true
        assertEquals(true, fakeTaskDao.dataToReturn.first() != dataToInsert)
        taskService.insertTask(dataToInsert)
        assertEquals(true, fakeTaskDao.dataToReturn.first() == dataToInsert)
        val data = taskService.getTasksAsFlow().first().first()
        assertEquals(true, data == dataToInsert)
    }

    @Test
    fun test_getTasks() = runBlocking {
        assertEquals(true, fakeTaskDao.dataToReturn.first() != dataToInsert)
        taskService.insertTask(dataToInsert)
        assertEquals(true, fakeTaskDao.dataToReturn.first() == dataToInsert)
        val data = taskService.getTasks().first()
        assertEquals(true, data == dataToInsert)
    }

    @Test(expected = DataException::class)
    fun test_getTasks_shouldThrowException() = runBlocking {
        fakeTaskDao.isNeedToThrowException = true
        assertEquals(true, fakeTaskDao.dataToReturn.first() != dataToInsert)
        taskService.insertTask(dataToInsert)
        assertEquals(true, fakeTaskDao.dataToReturn.first() == dataToInsert)
        val data = taskService.getTasks().first()
        assertEquals(true, data == dataToInsert)
    }

    @Test
    fun test_getTasksByText() = runBlocking {
        assertEquals(true, fakeTaskDao.dataToReturn.first() != dataToInsert)
        taskService.insertTask(dataToInsert)
        val textToSearch = "some text"
        assertEquals(true, fakeTaskDao.dataToReturn.first() == dataToInsert)
        val data = taskService.getTasksByText(textToSearch).first()
        assertEquals(true, data == dataToInsert)
        assertEquals(true, textToSearch == fakeTaskDao.textToGetTask)
    }

    @Test(expected = DataException::class)
    fun test_getTasksByText_shouldThrowException() = runBlocking {
        fakeTaskDao.isNeedToThrowException = true
        assertEquals(true, fakeTaskDao.dataToReturn.first() != dataToInsert)
        taskService.insertTask(dataToInsert)
        assertEquals(true, fakeTaskDao.dataToReturn.first() == dataToInsert)
        val data = taskService.getTasksByText("some text").first()
        assertEquals(true, data == dataToInsert)
    }

    @Test
    fun test_changeTask() = runBlocking {
        assertEquals(true, fakeTaskDao.dataToReturn.first() != dataToInsert)
        taskService.changeTask(dataToInsert)
        assertEquals(true, fakeTaskDao.dataToReturn.first() == dataToInsert)
    }

    @Test(expected = DataException::class)
    fun test_changeTask_shouldThrowException() = runBlocking {
        fakeTaskDao.isNeedToThrowException = true
        assertEquals(true, fakeTaskDao.dataToReturn.first() != dataToInsert)
        taskService.changeTask(dataToInsert)
        assertEquals(true, fakeTaskDao.dataToReturn.first() == dataToInsert)
    }

    @Test
    fun test_changeTasks() = runBlocking {
        assertEquals(true, fakeTaskDao.dataToReturn.first() != dataToInsert)
        taskService.changeTasks(listOf(dataToInsert))
        assertEquals(true, fakeTaskDao.dataToReturn.first() == dataToInsert)
    }

    @Test(expected = DataException::class)
    fun test_changeTasks_shouldThrowException() = runBlocking {
        fakeTaskDao.isNeedToThrowException = true
        assertEquals(true, fakeTaskDao.dataToReturn.first() != dataToInsert)
        taskService.changeTasks(listOf(dataToInsert))
        assertEquals(true, fakeTaskDao.dataToReturn.first() == dataToInsert)
    }

    @Test
    fun test_deleteTask() = runBlocking {
        assertEquals(true, fakeTaskDao.dataToReturn.isNotEmpty())
        taskService.deleteTask(dataToInsert)
        assertEquals(false, fakeTaskDao.dataToReturn.isNotEmpty())
        assertEquals(true, fakeTaskDao.deletedItemId == dataToInsert.id)
    }

    @Test(expected = DataException::class)
    fun test_deleteTask_shouldThrowException() = runBlocking {
        fakeTaskDao.isNeedToThrowException = true
        assertEquals(true, fakeTaskDao.dataToReturn.isNotEmpty())
        taskService.deleteTask(dataToInsert)
        assertEquals(false, fakeTaskDao.dataToReturn.isNotEmpty())
        assertEquals(true, fakeTaskDao.deletedItemId == dataToInsert.id)
    }

    @Test
    fun test_deleteTasks() = runBlocking {
        assertEquals(true, fakeTaskDao.dataToReturn.isNotEmpty())
        val items = listOf(dataToInsert)
        val itemsToDelete = mutableListOf<Long?>()
        items.forEach {
            itemsToDelete.add(it.id)
        }
        taskService.deleteTasks(items)
        assertEquals(false, fakeTaskDao.dataToReturn.isNotEmpty())
        assertEquals(true, fakeTaskDao.deletedItemIds == itemsToDelete)
    }

    @Test(expected = DataException::class)
    fun test_deleteTasks_shouldThrowException() = runBlocking {
        fakeTaskDao.isNeedToThrowException = true
        assertEquals(true, fakeTaskDao.dataToReturn.isNotEmpty())
        val items = listOf(dataToInsert)
        val itemsToDelete = mutableListOf<Long?>()
        items.forEach {
            itemsToDelete.add(it.id)
        }
        taskService.deleteTasks(items)
        assertEquals(false, fakeTaskDao.dataToReturn.isNotEmpty())
        assertEquals(true, fakeTaskDao.deletedItemIds == itemsToDelete)
    }

}

private class FakeTaskDao: TaskDao {

    var isNeedToThrowException = false
    var deletedItemId: Long? = null
    var textToGetTask = ""
    var deletedItemIds = mutableListOf<Long?>()
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
        if (isNeedToThrowException) throw IllegalStateException("some exception")
        dataToReturn.clear()
        dataToReturn.add(task)
        return task.id
    }

    override fun getTasksAsFlow(): Flow<List<TaskEntity>> {
        if (isNeedToThrowException) throw IllegalStateException("some exception")
        return flow {
            emit(dataToReturn)
        }
    }

    override fun getTasks(): List<TaskEntity> {
        if (isNeedToThrowException) throw IllegalStateException("some exception")
        return dataToReturn
    }

    override fun getTasksByText(text: String): List<TaskEntity> {
        if (isNeedToThrowException) throw IllegalStateException("some exception")
        textToGetTask = text
        return dataToReturn
    }

    override fun changeTask(task: TaskEntity) {
        if (isNeedToThrowException) throw IllegalStateException("some exception")
        dataToReturn.clear()
        dataToReturn.add(task)
    }

    override fun changeTasks(tasks: List<TaskEntity>) {
        if (isNeedToThrowException) throw IllegalStateException("some exception")
        dataToReturn = tasks.toMutableList()
    }

    override fun deleteTask(task: TaskEntity) {
        if (isNeedToThrowException) throw IllegalStateException("some exception")
        deletedItemId = task.id
        dataToReturn.clear()
    }

    override fun deleteTasks(tasks: List<TaskEntity>) {
        if (isNeedToThrowException) throw IllegalStateException("some exception")
        tasks.forEach {
            deletedItemIds.add(it.id)
        }
        dataToReturn.clear()
    }

}