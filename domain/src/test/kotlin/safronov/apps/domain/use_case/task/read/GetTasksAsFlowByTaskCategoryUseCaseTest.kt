package safronov.apps.domain.use_case.task.read

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task.Task
import safronov.apps.domain.model.task_category.TaskCategory
import safronov.apps.domain.model.task_category.category_type.CategoryTypes
import safronov.apps.domain.repository.task.TaskRepository
import java.lang.IllegalStateException

class GetTasksAsFlowByTaskCategoryUseCaseTest {

    private lateinit var fakeTaskRepositoryGettingByParams: FakeTaskRepositoryGettingByParams
    private lateinit var getTasksAsFlowByTaskCategoryUseCase: GetTasksAsFlowByTaskCategoryUseCase
    private lateinit var fakeTaskRepositoryGetting: FakeTaskRepositoryGetting

    @Before
    fun setUp() {
        fakeTaskRepositoryGettingByParams = FakeTaskRepositoryGettingByParams()
        fakeTaskRepositoryGetting = FakeTaskRepositoryGetting()
        getTasksAsFlowByTaskCategoryUseCase = GetTasksAsFlowByTaskCategoryUseCase(
            fakeTaskRepositoryGettingByParams,
            taskRepositoryGetting = fakeTaskRepositoryGetting
        )
    }

    @Test
    fun execute() = runBlocking {
        val data: List<Task> = fakeTaskRepositoryGettingByParams.dataToReturn
        val category = fakeTaskRepositoryGettingByParams.taskCategory
        val result: Flow<List<Task>> = getTasksAsFlowByTaskCategoryUseCase.execute(category)
        assertEquals(true, data == result.first())
        assertEquals(true, fakeTaskRepositoryGettingByParams.requestCategory == category)
        assertEquals(true, fakeTaskRepositoryGettingByParams.requestCount == 1)
        assertEquals(true, fakeTaskRepositoryGetting.requestCount == 0)
    }

    @Test
    fun executeBySystemTaskCategory() = runBlocking {
        val data: List<Task> = fakeTaskRepositoryGetting.dataToReturn
        val category = fakeTaskRepositoryGetting.taskCategory
        val result: Flow<List<Task>> = getTasksAsFlowByTaskCategoryUseCase.execute(category)
        assertEquals(true, data == result.first())
        assertEquals(true, fakeTaskRepositoryGetting.requestCategory == category)
        assertEquals(true, fakeTaskRepositoryGettingByParams.requestCount == 0)
        assertEquals(true, fakeTaskRepositoryGetting.requestCount == 1)
    }

    @Test(expected = DomainException::class)
    fun execute_expectedException() = runBlocking {
        fakeTaskRepositoryGettingByParams.isNeedToThrowException = true
        val category = fakeTaskRepositoryGettingByParams.taskCategory
        val result = getTasksAsFlowByTaskCategoryUseCase.execute(category)
    }

}

private class FakeTaskRepositoryGettingByParams: TaskRepository.GettingTaskByParameters {

    var isNeedToThrowException = false
    var requestCategory: TaskCategory? = null
    var requestCount = 0
    val taskCategory = TaskCategory(
        id = 2342,
        icon = null,
        backgroundColor = null,
        categoryName = "asdf",
        categoryType = CategoryTypes.User
    )
    val dataToReturn = listOf<Task>(
        Task.TaskText(
            title = "asdf",
            text = "asdf",
            date = "asdf",
            taskCategoryId = taskCategory.id,
            taskType = Task.TaskType.Text,
            isPinned = true,
            id = 32423
        )
    )

    override suspend fun getTasksByText(text: String): List<Task> {
        throw IllegalStateException("don't use this method")
    }

    override suspend fun getTasksAsFlowByTaskCategory(taskCategory: TaskCategory): Flow<List<Task>> {
        if (isNeedToThrowException) throw DomainException("some exception")
        requestCategory = taskCategory
        requestCount++
        return flow {
            emit(dataToReturn)
        }
    }

}

private class FakeTaskRepositoryGetting: TaskRepository.GettingTask {

    var isNeedToThrowException = false
    var requestCategory: TaskCategory? = null
    var requestCount = 0
    val taskCategory = TaskCategory(
        id = 324235324543223523,
        icon = 23423,
        backgroundColor = null,
        categoryName = "asdf",
        categoryType = CategoryTypes.System
    )
    val dataToReturn = listOf<Task>(
        Task.TaskText(
            title = "asdf",
            text = "asdf",
            date = "asdf",
            taskCategoryId = taskCategory.id,
            taskType = Task.TaskType.Text,
            isPinned = true,
            id = 32423
        )
    )

    override suspend fun getTasksAsFlow(): Flow<List<Task>> {
        if (isNeedToThrowException) throw DomainException("some exception")
        requestCategory = taskCategory
        requestCount++
        return flow {
            emit(dataToReturn)
        }
    }

    override suspend fun getTasks(): List<Task> {
        return dataToReturn
    }


}