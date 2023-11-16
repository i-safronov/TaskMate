package safronov.apps.domain.use_case.task.read

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task.Task
import safronov.apps.domain.model.task_category.TaskCategory
import safronov.apps.domain.repository.task.TaskRepository

class GetTasksByTextUseCaseTest {

    private lateinit var fakeGettingTasksByParametersRepository: FakeGettingTasksByParametersRepository
    private lateinit var getTasksByTextUseCase: GetTasksByTextUseCase

    @Before
    fun setUp() {
        fakeGettingTasksByParametersRepository = FakeGettingTasksByParametersRepository()
        getTasksByTextUseCase = GetTasksByTextUseCase(
            gettingTasksByParametersRepository = fakeGettingTasksByParametersRepository
        )
    }

    @Test
    fun `test, execute, should return data`() = runBlocking {
        val result: List<Task> = getTasksByTextUseCase.execute(text = "some text")
        assertEquals(true, result == fakeGettingTasksByParametersRepository.dataToReturn)
    }

    @Test(expected = DomainException::class)
    fun `test, execute, should throw domain exception`(): Unit = runBlocking {
        fakeGettingTasksByParametersRepository.isNeedToThrowException = true
        getTasksByTextUseCase.execute(text = "some text")
    }

}

private class FakeGettingTasksByParametersRepository: TaskRepository.GettingTaskByParameters {

    var isNeedToThrowException = false
    val dataToReturn = listOf(
        Task.TaskList(
            title = "some title1",
            list = listOf(
                Task.TaskListItem(
                    title = "some title2",
                    isChecked = true
                )
            ),
            date = "todayw",
            taskCategoryId = 545,
            taskType = Task.TaskType.Text,
            isPinned = true,
            id = 3
        )
    )

    override suspend fun getTasksByText(text: String): List<Task> {
        if (isNeedToThrowException) throw DomainException("some exception")
        return dataToReturn
    }

    override suspend fun getTasksAsFlowByTaskCategory(taskCategory: TaskCategory): Flow<List<Task>> {
        TODO("Not yet implemented")
    }

}