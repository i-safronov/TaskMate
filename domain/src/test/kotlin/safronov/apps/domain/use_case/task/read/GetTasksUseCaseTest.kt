package safronov.apps.domain.use_case.task.read

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task.Task
import safronov.apps.domain.repository.task.TaskRepository
import java.lang.IllegalStateException

class GetTasksUseCaseTest {

    @Test
    fun `test, execute, should get items`() = runBlocking {
        val fakeGettingTaskRepository1 = FakeGettingTaskRepository1()
        val getTasksUseCase = GetTasksUseCase(
            gettingTaskRepository = fakeGettingTaskRepository1
        )
        assertEquals(fakeGettingTaskRepository1.dataToReturn, getTasksUseCase.execute())
    }

    @Test(expected = DomainException::class)
    fun `test, execute, should throw domain exception`(): Unit = runBlocking {
        val fakeGettingTaskRepository1 = FakeGettingTaskRepository1()
        fakeGettingTaskRepository1.isNeedToThrowException = true
        val getTasksUseCase = GetTasksUseCase(
            gettingTaskRepository = fakeGettingTaskRepository1
        )
        getTasksUseCase.execute()
    }

}

private class FakeGettingTaskRepository1: TaskRepository.GettingTask {

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

    override suspend fun getTasksAsFlow(): Flow<List<Task>> {
        throw IllegalStateException("don't use this method")
    }

    override suspend fun getTasks(): List<Task> {
        if (isNeedToThrowException) throw DomainException("some exception")
        return dataToReturn
    }

}