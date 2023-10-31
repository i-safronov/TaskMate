package safronov.apps.data.repository_impl.task

import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import safronov.apps.data.data_source.local.model.converter.task.TaskEntityConverter
import safronov.apps.data.data_source.local.model.converter.task.TaskEntityConverterImpl
import safronov.apps.data.data_source.local.model.task.TaskEntity
import safronov.apps.data.data_source.local.service.task.TaskService
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task.Task
import safronov.apps.domain.repository.task.TaskRepository
import java.lang.IllegalStateException

class TaskRepositoryImplTest {

    private lateinit var taskRepository: TaskRepository.TaskRepositoryMutable
    private lateinit var fakeTaskService: FakeTaskService
    private lateinit var taskEntityConverter: TaskEntityConverter

    private lateinit var taskText: Task.TaskText
    private lateinit var taskEntityText: TaskEntity
    private lateinit var taskEntityList: TaskEntity
    private lateinit var taskList: Task.TaskList
    private lateinit var listOfTaskEntityText: List<TaskEntity>
    private lateinit var listOfTaskEntityList: List<TaskEntity>
    private lateinit var listOfTaskText: List<Task.TaskText>
    private lateinit var listOfTaskList: List<Task.TaskList>

    @Before
    fun setUp() {
        fakeTaskService = FakeTaskService()
        taskEntityConverter = TaskEntityConverterImpl(Gson())
        taskRepository = TaskRepositoryImpl(
            taskService = fakeTaskService,
            taskEntityConverter = taskEntityConverter
        )
        taskEntityText = TaskEntity(
            title = "something",
            content = "context",
            date = "date",
            taskCategoryId = 4,
            taskType = Task.TaskType.Text,
            isPinned = true,
            id = 32
        )
        taskEntityList = TaskEntity(
            title = "something",
            content = Gson().toJson(listOf<Task.TaskListItem>(
                Task.TaskListItem(
                    title = "some title",
                    isChecked = true
                )
            )),
            date = "date",
            taskCategoryId = 4,
            taskType = Task.TaskType.Text,
            isPinned = true,
            id = 32
        )
        taskText = Task.TaskText(
            title = "something",
            text = "context",
            date = "date",
            taskCategoryId = 4,
            taskType = Task.TaskType.Text,
            isPinned = true,
            id = 32
        )
        taskList = Task.TaskList(
            title = "something",
            list = listOf<Task.TaskListItem>(
                Task.TaskListItem(
                    title = "some title",
                    isChecked = true
                )
            ),
            date = "date",
            taskCategoryId = 4,
            taskType = Task.TaskType.Text,
            isPinned = true,
            id = 32
        )
        listOfTaskEntityText = listOf(taskEntityText)
        listOfTaskEntityList = listOf(taskEntityList)
        listOfTaskText = listOf(taskText)
        listOfTaskList = listOf(taskList)
    }

    @Test
    fun test_insertTaskText() = runBlocking {
        assertEquals(true, taskText != taskEntityConverter.convertTaskEntityToTaskText(fakeTaskService.dataToReturn.first()))
        taskRepository.insertTaskText(taskText)
        assertEquals(true, taskText == taskEntityConverter.convertTaskEntityToTaskText(fakeTaskService.dataToReturn.first()))
    }

    @Test(expected = DomainException::class)
    fun test_insertTaskText_shouldThrowDomainException() = runBlocking {
        fakeTaskService.isNeedToThrowException = true
        assertEquals(true, taskText != taskEntityConverter.convertTaskEntityToTaskText(fakeTaskService.dataToReturn.first()))
        taskRepository.insertTaskText(taskText)
        assertEquals(true, taskText == taskEntityConverter.convertTaskEntityToTaskText(fakeTaskService.dataToReturn.first()))
    }

    @Test
    fun test_insertTaskList() = runBlocking {
        taskRepository.insertTaskList(taskList)
        assertEquals(true, taskList == taskEntityConverter.convertTaskEntityToTaskList(fakeTaskService.dataToReturn.first()))
    }

    @Test(expected = DomainException::class)
    fun test_insertTaskList_shouldThrowDomainException() = runBlocking {
        fakeTaskService.isNeedToThrowException = true
        taskRepository.insertTaskList(taskList)
        assertEquals(true, taskList == taskEntityConverter.convertTaskEntityToTaskList(fakeTaskService.dataToReturn.first()))
    }

    @Test
    fun test_getTasksAsFlow() = runBlocking {
        taskRepository.insertTaskList(taskList)
        assertEquals(true, taskList == taskEntityConverter.convertTaskEntityToTaskList(fakeTaskService.dataToReturn.first()))
        val result: Task = taskRepository.getTasksAsFlow().first().first()
        if (result is Task.TaskText) {
            assertEquals(true, taskEntityConverter.convertTaskEntityToTaskText(fakeTaskService.dataToReturn.first()) == result)
        } else if (result is Task.TaskList) {
            assertEquals(true, taskList == result)
        } else {
            throw IllegalStateException("oops")
        }
    }

    @Test(expected = DomainException::class)
    fun test_getTasksAsFlow_shouldThrowDomainException() = runBlocking {
        fakeTaskService.isNeedToThrowException = true
        taskRepository.insertTaskList(taskList)
        assertEquals(true, taskList == taskEntityConverter.convertTaskEntityToTaskList(fakeTaskService.dataToReturn.first()))
        val result: Task = taskRepository.getTasksAsFlow().first().first()
        if (result is Task.TaskText) {
            assertEquals(true, taskEntityConverter.convertTaskEntityToTaskText(fakeTaskService.dataToReturn.first()) == result)
        } else if (result is Task.TaskList) {
            assertEquals(true, taskList == result)
        } else {
            throw IllegalStateException("oops")
        }
    }

    @Test
    fun test_getTasks() = runBlocking {
        taskRepository.insertTaskList(taskList)
        assertEquals(true, taskList == taskEntityConverter.convertTaskEntityToTaskList(fakeTaskService.dataToReturn.first()))
        val result: Task = taskRepository.getTasks().first()
        if (result is Task.TaskText) {
            assertEquals(true, taskEntityConverter.convertTaskEntityToTaskText(fakeTaskService.dataToReturn.first()) == result)
        } else {
            throw IllegalStateException("oops")
        }
    }

    @Test(expected = DomainException::class)
    fun test_getTasks_shouldThrowDomainException() = runBlocking {
        fakeTaskService.isNeedToThrowException = true
        taskRepository.insertTaskList(taskList)
        assertEquals(true, taskList == taskEntityConverter.convertTaskEntityToTaskList(fakeTaskService.dataToReturn.first()))
        val result: Task = taskRepository.getTasks().first()
        if (result is Task.TaskText) {
            assertEquals(true, taskEntityConverter.convertTaskEntityToTaskText(fakeTaskService.dataToReturn.first()) == result)
        } else {
            throw IllegalStateException("oops")
        }
    }

    @Test
    fun test_getTasksByText() = runBlocking {
        taskRepository.insertTaskText(taskText)
        assertEquals(true, taskText == taskEntityConverter.convertTaskEntityToTaskText(fakeTaskService.dataToReturn.first()))
        val result: Task = taskRepository.getTasksByText("text").first()
        if (result is Task.TaskText) {
            assertEquals(true, taskText == result)
        } else if (result is Task.TaskList) {
            assertEquals(true, taskList == result)
        } else {
            throw IllegalStateException("oops")
        }
    }

    @Test(expected = DomainException::class)
    fun test_getTasksByText_shouldThrowDomainException() = runBlocking {
        fakeTaskService.isNeedToThrowException = true
        taskRepository.insertTaskText(taskText)
        assertEquals(true, taskText == taskEntityConverter.convertTaskEntityToTaskText(fakeTaskService.dataToReturn.first()))
        val result: Task = taskRepository.getTasksByText("text").first()
        if (result is Task.TaskText) {
            assertEquals(true, taskText == result)
        } else if (result is Task.TaskList) {
            assertEquals(true, taskList == result)
        } else {
            throw IllegalStateException("oops")
        }
    }

    @Test
    fun test_changeTaskText() = runBlocking {
        assertEquals(true, taskText != taskEntityConverter.convertTaskEntityToTaskText(fakeTaskService.dataToReturn.first()))
        taskRepository.changeTaskText(taskText)
        assertEquals(true, taskText == taskEntityConverter.convertTaskEntityToTaskText(fakeTaskService.dataToReturn.first()))
    }

    @Test(expected = DomainException::class)
    fun test_changeTaskText_shouldThrowDomainException() = runBlocking {
        fakeTaskService.isNeedToThrowException = true
        assertEquals(true, taskText != taskEntityConverter.convertTaskEntityToTaskText(fakeTaskService.dataToReturn.first()))
        taskRepository.changeTaskText(taskText)
        assertEquals(true, taskText == taskEntityConverter.convertTaskEntityToTaskText(fakeTaskService.dataToReturn.first()))
    }

    @Test
    fun test_changeTaskList() = runBlocking {
        taskRepository.changeTaskList(taskList)
        assertEquals(true, taskList == taskEntityConverter.convertTaskEntityToTaskList(fakeTaskService.dataToReturn.first()))
    }

    @Test(expected = DomainException::class)
    fun test_changeTaskList_shouldThrowDomainException() = runBlocking {
        fakeTaskService.isNeedToThrowException = true
        taskRepository.changeTaskList(taskList)
        assertEquals(true, taskList == taskEntityConverter.convertTaskEntityToTaskList(fakeTaskService.dataToReturn.first()))
    }

    @Test
    fun test_changeTasks() = runBlocking {
        val oldData = fakeTaskService.dataToReturn
        taskRepository.changeTasks(listOfTaskText)
        assertEquals(false, oldData == fakeTaskService.dataToReturn)
        val resultList = taskEntityConverter.convertListOfTaskEntityToListOfTaskText(fakeTaskService.dataToReturn)
        assertEquals(true, resultList == listOfTaskText)
    }

    @Test(expected = DomainException::class)
    fun test_changeTasks_shouldThrowDomainException() = runBlocking {
        fakeTaskService.isNeedToThrowException = true
        val oldData = fakeTaskService.dataToReturn
        taskRepository.changeTasks(listOfTaskText)
        assertEquals(false, oldData == fakeTaskService.dataToReturn)
        val resultList = taskEntityConverter.convertListOfTaskEntityToListOfTaskText(fakeTaskService.dataToReturn)
        assertEquals(true, resultList == listOfTaskText)
    }

    @Test
    fun test_deleteTaskText() = runBlocking {
        assertEquals(false, fakeTaskService.dataToReturn.isEmpty())
        assertEquals(true, fakeTaskService.requestDeleteItemId != taskText.id)
        taskRepository.deleteTaskText(taskText)
        assertEquals(true, fakeTaskService.dataToReturn.isEmpty())
        assertEquals(true, fakeTaskService.requestDeleteItemId == taskText.id)
    }

    @Test(expected = DomainException::class)
    fun test_deleteTaskText_shouldThrowDomainException() = runBlocking {
        fakeTaskService.isNeedToThrowException = true
        assertEquals(false, fakeTaskService.dataToReturn.isEmpty())
        assertEquals(true, fakeTaskService.requestDeleteItemId != taskText.id)
        taskRepository.deleteTaskText(taskText)
        assertEquals(true, fakeTaskService.dataToReturn.isEmpty())
        assertEquals(true, fakeTaskService.requestDeleteItemId == taskText.id)
    }

    @Test
    fun test_deleteTaskList() = runBlocking {
        assertEquals(false, fakeTaskService.dataToReturn.isEmpty())
        assertEquals(true, fakeTaskService.requestDeleteItemId != taskList.id)
        taskRepository.deleteTaskList(taskList)
        assertEquals(true, fakeTaskService.dataToReturn.isEmpty())
        assertEquals(true, fakeTaskService.requestDeleteItemId == taskList.id)
    }

    @Test(expected = DomainException::class)
    fun test_deleteTaskList_shouldThrowDomainException() = runBlocking {
        fakeTaskService.isNeedToThrowException = true
        assertEquals(false, fakeTaskService.dataToReturn.isEmpty())
        assertEquals(true, fakeTaskService.requestDeleteItemId != taskList.id)
        taskRepository.deleteTaskList(taskList)
        assertEquals(true, fakeTaskService.dataToReturn.isEmpty())
        assertEquals(true, fakeTaskService.requestDeleteItemId == taskList.id)
    }

    @Test
    fun test_deleteTasks() = runBlocking {
        val listDeletedItemsId = mutableListOf<Long?>()
        listOfTaskText.forEach {
            listDeletedItemsId.add(it.id)
        }
        assertEquals(false, fakeTaskService.dataToReturn.isEmpty())
        assertEquals(true, fakeTaskService.requestToDeleteItemsId != listDeletedItemsId)
        assertEquals(true, fakeTaskService.requestToDeleteItemsId.isEmpty())

        taskRepository.deleteTasks(listOfTaskText)
        assertEquals(true, fakeTaskService.dataToReturn.isEmpty())
        assertEquals(true, fakeTaskService.requestToDeleteItemsId == listDeletedItemsId)
        assertEquals(true, fakeTaskService.requestToDeleteItemsId.isNotEmpty())
    }

    @Test(expected = DomainException::class)
    fun test_deleteTasks_shouldThrowDomainException() = runBlocking {
        fakeTaskService.isNeedToThrowException = true
        val listDeletedItemsId = mutableListOf<Long?>()
        listOfTaskText.forEach {
            listDeletedItemsId.add(it.id)
        }
        assertEquals(false, fakeTaskService.dataToReturn.isEmpty())
        assertEquals(true, fakeTaskService.requestToDeleteItemsId != listDeletedItemsId)
        assertEquals(true, fakeTaskService.requestToDeleteItemsId.isEmpty())

        taskRepository.deleteTasks(listOfTaskText)

        assertEquals(true, fakeTaskService.dataToReturn.isEmpty())
        assertEquals(true, fakeTaskService.requestToDeleteItemsId == listDeletedItemsId)
        assertEquals(true, fakeTaskService.requestToDeleteItemsId.isNotEmpty())
    }

}

private class FakeTaskService: TaskService {
    
    var isNeedToThrowException = false
    var requestDeleteItemId: Long? = null
    var requestToDeleteItemsId: MutableList<Long?> = mutableListOf()
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
        if (isNeedToThrowException) throw IllegalStateException("some exception")
        dataToReturn.clear()
        dataToReturn.add(task)
        return task.id
    }

    override suspend fun getTasksAsFlow(): Flow<List<TaskEntity>> {
        if (isNeedToThrowException) throw IllegalStateException("some exception")
        return flow {
            emit(dataToReturn)
        }
    }

    override suspend fun getTasks(): List<TaskEntity> {
        if (isNeedToThrowException) throw IllegalStateException("some exception")
        return dataToReturn
    }

    override suspend fun getTasksByText(text: String): List<TaskEntity> {
        if (isNeedToThrowException) throw IllegalStateException("some exception")
        return listOf(dataToReturn.first())
    }

    override suspend fun changeTask(task: TaskEntity) {
        if (isNeedToThrowException) throw IllegalStateException("some exception")
        dataToReturn.clear()
        dataToReturn.add(task)
    }

    override suspend fun changeTasks(tasks: List<TaskEntity>) {
        if (isNeedToThrowException) throw IllegalStateException("some exception")
        dataToReturn = tasks.toMutableList()
    }

    override suspend fun deleteTask(task: TaskEntity) {
        if (isNeedToThrowException) throw IllegalStateException("some exception")
        requestDeleteItemId = task.id
        dataToReturn.clear()
    }

    override suspend fun deleteTasks(tasks: List<TaskEntity>) {
        if (isNeedToThrowException) throw IllegalStateException("some exception")
        tasks.forEach {
            requestToDeleteItemsId.add(it.id)
        }
        dataToReturn.clear()
    }

}