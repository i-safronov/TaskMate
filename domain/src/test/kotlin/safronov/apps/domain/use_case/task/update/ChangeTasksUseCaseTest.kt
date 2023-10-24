package safronov.apps.domain.use_case.task.update

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task.Task
import safronov.apps.domain.repository.task.TaskRepository
import java.lang.IllegalStateException

class ChangeTasksUseCaseTest {

    private val newData = listOf<Task>(
        Task.TaskText(
            title = "some title",
            text = "some text",
            date = "today",
            taskCategoryId = 5,
            taskType = Task.TaskType.Text,
            isPinned = false,
            id = 2
        ),
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

    @Test
    fun `test, items, should change items`() = runBlocking {
        val fakeChangingTasksUseCase2 = FakeChangingTasksUseCase2()
        val changeTasksUseCase = ChangeTasksUseCase(
            changingTasksUseCase = fakeChangingTasksUseCase2
        )
        assertEquals(false, fakeChangingTasksUseCase2.dataToReturn == newData)
        changeTasksUseCase.execute(
            tasks = newData
        )
        assertEquals(true, fakeChangingTasksUseCase2.dataToReturn == newData)
    }

    @Test(expected = DomainException::class)
    fun `test, items, should throw domain exception`() = runBlocking {
        val fakeChangingTasksUseCase2 = FakeChangingTasksUseCase2()
        fakeChangingTasksUseCase2.isNeedToThrowException = true
        val changeTasksUseCase = ChangeTasksUseCase(
            changingTasksUseCase = fakeChangingTasksUseCase2
        )
        assertEquals(false, fakeChangingTasksUseCase2.dataToReturn == newData)
        changeTasksUseCase.execute(
            tasks = newData
        )
    }

}

private class FakeChangingTasksUseCase2: TaskRepository.ChangingTask {

    var isNeedToThrowException = false
    var dataToReturn = listOf<Task>()

    override suspend fun changeTaskText(task: Task.TaskText) {
        throw IllegalStateException("don't use this method")
    }

    override suspend fun changeTaskList(task: Task.TaskList) {
        throw IllegalStateException("don't use this method")
    }

    override suspend fun changeTasks(tasks: List<Task>) {
        if (isNeedToThrowException) throw DomainException("some exception")
        dataToReturn = tasks
    }

}