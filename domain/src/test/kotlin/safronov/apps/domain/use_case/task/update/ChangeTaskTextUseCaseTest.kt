package safronov.apps.domain.use_case.task.update

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task.Task
import safronov.apps.domain.repository.task.TaskRepository
import java.lang.IllegalStateException

class ChangeTaskTextUseCaseTest {

    private val newData = Task.TaskText(
        title = "some titl3e",
        text = "some te3xt",
        date = "toda3y",
        taskCategoryId = 55,
        taskType = Task.TaskType.Text,
        isPinned = false,
        id = 21
    )

    @Test
    fun `test, execute, should change items`() = runBlocking {
        val fakeChangingTaskRepository = FakeChangingTaskRepository()
        val changeTaskTextUseCase = ChangeTaskTextUseCase(
            changingTaskRepository = fakeChangingTaskRepository
        )
        assertEquals(false, fakeChangingTaskRepository.dataToReturn == newData)
        changeTaskTextUseCase.execute(task = newData)
        assertEquals(true, fakeChangingTaskRepository.dataToReturn == newData)
    }

    @Test(expected = DomainException::class)
    fun `test, execute, should throw domain exception`() = runBlocking {
        val fakeChangingTaskRepository = FakeChangingTaskRepository()
        fakeChangingTaskRepository.isNeedToThrowException = true
        val changeTaskTextUseCase = ChangeTaskTextUseCase(
            changingTaskRepository = fakeChangingTaskRepository
        )
        assertEquals(false, fakeChangingTaskRepository.dataToReturn == newData)
        changeTaskTextUseCase.execute(task = newData)
    }

}

private class FakeChangingTaskRepository: TaskRepository.ChangingTask {

    var isNeedToThrowException = false
    var dataToReturn = Task.TaskText(
        title = "some title",
        text = "some text",
        date = "today",
        taskCategoryId = 5,
        taskType = Task.TaskType.Text,
        isPinned = false,
        id = 2
    )

    override suspend fun changeTaskText(task: Task.TaskText) {
        if (isNeedToThrowException) throw DomainException("some exception")
        dataToReturn = task
    }

    override suspend fun changeTaskList(task: Task.TaskList) {
        throw IllegalStateException("don't use this method -_- ")
    }

    override suspend fun changeTasks(tasks: List<Task>) {
        throw IllegalStateException("don't use this method -_- ")
    }

}