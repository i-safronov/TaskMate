package safronov.apps.domain.use_case.task.delete

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task.Task
import safronov.apps.domain.repository.task.TaskRepository
import java.lang.IllegalStateException

class DeleteTaskListUseCaseTest {

    @Test
    fun `test, execute, should delete items`() = runBlocking {
        val fakeDeletingTaskRepository = FakeDeletingTaskRepository1()
        val deleteTaskListUseCase = DeleteTaskListUseCase(
            deletingTaskRepository = fakeDeletingTaskRepository
        )
        assertEquals(true, fakeDeletingTaskRepository.dataToReturn.isNotEmpty())
        deleteTaskListUseCase.execute(
            task = fakeDeletingTaskRepository.dataToReturn.first()
        )
        assertEquals(false, fakeDeletingTaskRepository.dataToReturn.isNotEmpty())
    }

    @Test(expected = DomainException::class)
    fun `test, execute, should throw exception`() = runBlocking {
        val fakeDeletingTaskRepository = FakeDeletingTaskRepository1()
        fakeDeletingTaskRepository.isNeedToThrowException = true
        val deleteTaskListUseCase = DeleteTaskListUseCase(
            deletingTaskRepository = fakeDeletingTaskRepository
        )
        assertEquals(true, fakeDeletingTaskRepository.dataToReturn.isNotEmpty())
        deleteTaskListUseCase.execute(
            task = fakeDeletingTaskRepository.dataToReturn.first()
        )
    }

}

private class FakeDeletingTaskRepository1: TaskRepository.DeletingTask {

    var isNeedToThrowException = false
    var dataToReturn = mutableListOf(
        Task.TaskList(
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
    )

    override suspend fun deleteTaskText(task: Task.TaskText) {
        throw IllegalStateException("some exception")
    }

    override suspend fun deleteTaskList(task: Task.TaskList) {
        if (isNeedToThrowException) throw DomainException("some exception")
        dataToReturn.clear()
    }

    override suspend fun deleteTasks(tasks: List<Task>) {
        throw IllegalStateException("some exception")
    }

}