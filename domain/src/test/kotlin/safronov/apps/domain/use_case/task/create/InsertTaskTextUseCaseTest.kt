package safronov.apps.domain.use_case.task.create

import org.junit.Assert.assertEquals
import org.junit.Test
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task.Task
import safronov.apps.domain.repository.task.TaskRepository
import java.lang.IllegalStateException

class InsertTaskTextUseCaseTest {

    private val newData = Task.TaskText(
        title = "some titles",
        text = "some textf",
        date = "todaya",
        taskCategoryId = 45,
        taskType = Task.TaskType.Text,
        isPinned = true,
        id = 5
    )

    @Test
    fun `test, insert item, should saved and return item`() {
        val fakeInsertingTask = FakeInsertingTask()
        val insertTaskTextUseCase = InsertTaskTextUseCase(
            insertingTaskRepository = fakeInsertingTask
        )
        insertTaskTextUseCase.execute(task = newData)
        assertEquals(newData, fakeInsertingTask.dataToReturn)
    }

    @Test(expected = DomainException::class)
    fun `test, insert item, should throw domain exception`() {
        val fakeInsertingTask = FakeInsertingTask()
        fakeInsertingTask.isNeedToThrowException = true
        val insertTaskTextUseCase = InsertTaskTextUseCase(
            insertingTaskRepository = fakeInsertingTask
        )
        insertTaskTextUseCase.execute(task = newData)
    }

}

class FakeInsertingTask: TaskRepository.InsertingTask {

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

    override suspend fun insertTaskText(task: Task.TaskText) {
        if (isNeedToThrowException) throw DomainException("some exception")
        dataToReturn = task
    }

    override suspend fun insertTaskList(task: Task.TaskList) {
        throw IllegalStateException("don't use this method in this use case")
    }

}