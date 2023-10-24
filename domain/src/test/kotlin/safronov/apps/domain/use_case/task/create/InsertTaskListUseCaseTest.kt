package safronov.apps.domain.use_case.task.create

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task.Task
import safronov.apps.domain.repository.task.TaskRepository
import java.lang.IllegalStateException

class InsertTaskListUseCaseTest {

    private val newData = Task.TaskList(
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

    @Test
    fun `test, execute, should save item`() = runBlocking {
        val fakeInsertingTask1 = FakeInsertingTask1()
        val insertTaskListUseCase = InsertTaskListUseCase(
            insertingTask = fakeInsertingTask1
        )
        assertEquals(false, newData == fakeInsertingTask1.dataToReturn)
        insertTaskListUseCase.execute(
            task = newData
        )
        assertEquals(true, newData == fakeInsertingTask1.dataToReturn)
    }

    @Test(expected = DomainException::class)
    fun `test, execute, should throw domain exception`() = runBlocking {
        val fakeInsertingTask1 = FakeInsertingTask1()
        fakeInsertingTask1.isNeedToThrowException = true
        val insertTaskListUseCase = InsertTaskListUseCase(
            insertingTask = fakeInsertingTask1
        )
        assertEquals(false, newData == fakeInsertingTask1.dataToReturn)
        insertTaskListUseCase.execute(
            task = newData
        )
    }

}

private class FakeInsertingTask1: TaskRepository.InsertingTask {

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

    override suspend fun insertTaskText(task: Task.TaskText) {
        throw IllegalStateException("don't use this method in this use case")
    }

    override suspend fun insertTaskList(task: Task.TaskList) {
        if (isNeedToThrowException) throw DomainException("some exception")
        dataToReturn = task
    }

}