package safronov.apps.domain.use_case.task.update

import org.junit.Assert.assertEquals
import org.junit.Test
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task.Task
import safronov.apps.domain.repository.task.TaskRepository
import java.lang.IllegalStateException

class ChangeTaskListUseCaseTest {

    private val newData = Task.TaskList(
        title = "some title2",
        list = listOf(
            Task.TaskListItem(
                title = "some titl3e",
                isChecked = true
            )
        ),
        date = "toda3y",
        taskCategoryId = 55,
        taskType = Task.TaskType.Text,
        isPinned = true,
        id = 21
    )

    @Test
    fun `test, execute, should change items`() {
        val fakeChangingTaskRepository = FakeChangingTaskRepository1()
        val changeTaskListUseCase = ChangeTaskListUseCase(
            changingTaskRepository = fakeChangingTaskRepository
        )
        assertEquals(false, fakeChangingTaskRepository.dataToReturn == newData)
        changeTaskListUseCase.execute(
            task = newData
        )
        assertEquals(true, fakeChangingTaskRepository.dataToReturn == newData)
    }

    @Test(expected = DomainException::class)
    fun `test, execute, should throw domain exception`() {
        val fakeChangingTaskRepository = FakeChangingTaskRepository1()
        fakeChangingTaskRepository.isNeedToThrowException = true
        val changeTaskListUseCase = ChangeTaskListUseCase(
            changingTaskRepository = fakeChangingTaskRepository
        )
        assertEquals(false, fakeChangingTaskRepository.dataToReturn == newData)
        changeTaskListUseCase.execute(
            task = newData
        )
    }

}

private class FakeChangingTaskRepository1: TaskRepository.ChangingTask {

    var isNeedToThrowException = false
    var dataToReturn = Task.TaskList(
        title = "some title",
        list = listOf(
            Task.TaskListItem(
                title = "some title",
                isChecked = false
            )
        ),
        date = "today",
        taskCategoryId = 5,
        taskType = Task.TaskType.Text,
        isPinned = false,
        id = 2
    )

    override suspend fun changeTaskText(task: Task.TaskText) {
        throw IllegalStateException("don't use this method -_- ")
    }

    override suspend fun changeTaskList(task: Task.TaskList) {
        if (isNeedToThrowException) throw DomainException("some exception")
        dataToReturn = task
    }

    override suspend fun changeTasks(tasks: List<Task>) {
        throw IllegalStateException("don't use this method -_- ")
    }

}