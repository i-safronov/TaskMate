package safronov.apps.domain.use_case.task.delete

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task.Task
import safronov.apps.domain.repository.task.TaskRepository
import java.lang.IllegalStateException

class DeleteTaskTextUseCaseTest {

    @Test
    fun `test, execute, should delete items`() = runBlocking {
        val fakeDeletingTaskRepository = FakeDeletingTaskRepository()
        val deleteTaskTextUseCase = DeleteTaskTextUseCase(
            deletingTaskRepository = fakeDeletingTaskRepository
        )
        val oldData = fakeDeletingTaskRepository.data.first()
        deleteTaskTextUseCase.execute(task = oldData)
        assertEquals(true, fakeDeletingTaskRepository.data.isEmpty())
    }

    @Test(expected = DomainException::class)
    fun `test, execute, should throw domain exception`() = runBlocking {
        val fakeDeletingTaskRepository = FakeDeletingTaskRepository()
        fakeDeletingTaskRepository.isNeedToThrowException = true
        val deleteTaskTextUseCase = DeleteTaskTextUseCase(
            deletingTaskRepository = fakeDeletingTaskRepository
        )
        val oldData = fakeDeletingTaskRepository.data.first()
        deleteTaskTextUseCase.execute(task = oldData)
        assertEquals(true, fakeDeletingTaskRepository.data.first() != oldData)
        assertEquals(true, fakeDeletingTaskRepository.data.isEmpty())
    }


}

private class FakeDeletingTaskRepository: TaskRepository.DeletingTask {

    var isNeedToThrowException = false
    var data = mutableListOf(
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
        if (isNeedToThrowException) throw DomainException("some exception")
        data.clear()
    }

    override suspend fun deleteTaskList(task: Task.TaskList) {
        throw IllegalStateException("don't use this method -_-")
    }

    override suspend fun deleteTasks(tasks: List<Task>) {
        throw IllegalStateException("don't use this method -_-")
    }

}