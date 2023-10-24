package safronov.apps.domain.use_case.task.read

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertEquals
import org.junit.Test
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task.Task
import safronov.apps.domain.repository.task.TaskRepository
import java.lang.IllegalStateException

class GetTasksAsFlowUseCaseTest {

    @Test
    fun `test, execute, should get items`() {
        val fakeGettingTaskRepository = FakeGettingTaskRepository()
        val getTasksAsFlowUseCase = GetTasksAsFlowUseCase(
            gettingTaskRepository = fakeGettingTaskRepository
        )
        assertEquals(fakeGettingTaskRepository.dataToReturn, getTasksAsFlowUseCase.execute().first())
    }

    @Test(expected = DomainException::class)
    fun `test, execute, should throw domain exception`() {
        val fakeGettingTaskRepository = FakeGettingTaskRepository()
        val getTasksAsFlowUseCase = GetTasksAsFlowUseCase(
            gettingTaskRepository = fakeGettingTaskRepository
        )
        getTasksAsFlowUseCase.execute().first()
    }

}

private class FakeGettingTaskRepository: TaskRepository.GettingTask {

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
        if (isNeedToThrowException) throw DomainException("some exception")
        return flow {
            emit(dataToReturn)
        }
    }

    override suspend fun getTasks(): List<Task> {
        throw IllegalStateException("don't use this method")
    }

}