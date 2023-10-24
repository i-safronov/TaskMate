package safronov.apps.domain.use_case.task.delete

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task.Task
import safronov.apps.domain.repository.task.TaskRepository
import java.lang.IllegalStateException

class DeleteTasksUseCaseTest {

    @Test
    fun `test, execute, should delete items`() = runBlocking {
        val fakeDeletingTaskRepository2 = FakeDeletingTaskRepository2()
        val deleteTasksUseCase = DeleteTasksUseCase(
            deletingTaskRepository = fakeDeletingTaskRepository2
        )
        assertEquals(true, fakeDeletingTaskRepository2.dataToReturn.isNotEmpty())
        deleteTasksUseCase.execute(
            tasks = fakeDeletingTaskRepository2.dataToReturn.toList()
        )
        assertEquals(false, fakeDeletingTaskRepository2.dataToReturn.isNotEmpty())
    }

    @Test(expected = DomainException::class)
    fun `test, execute, should throw domain exception`() = runBlocking {
        val fakeDeletingTaskRepository2 = FakeDeletingTaskRepository2()
        fakeDeletingTaskRepository2.isNeedToThrowException = true
        val deleteTasksUseCase = DeleteTasksUseCase(
            deletingTaskRepository = fakeDeletingTaskRepository2
        )
        assertEquals(true, fakeDeletingTaskRepository2.dataToReturn.isNotEmpty())
        deleteTasksUseCase.execute(
            tasks = fakeDeletingTaskRepository2.dataToReturn.toList()
        )
    }

}

private class FakeDeletingTaskRepository2: TaskRepository.DeletingTask {

    var isNeedToThrowException = false
    val dataToReturn = mutableListOf<Task>(
        Task.TaskText(
            title = "some title",
            text = "some text",
            date = "today",
            taskCategoryId = 5,
            taskType = Task.TaskType.Text,
            isPinned = false,
            id = 2
        )
    )

    override suspend fun deleteTaskText(task: Task.TaskText) {
        throw IllegalStateException("don't use this method")
    }

    override suspend fun deleteTaskList(task: Task.TaskList) {
        throw IllegalStateException("don't use this method")
    }

    override suspend fun deleteTasks(tasks: List<Task>) {
        if (isNeedToThrowException) throw DomainException("some exception")
        dataToReturn.clear()
    }

}