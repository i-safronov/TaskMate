package safronov.apps.domain.use_case.task_layout_manager

import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task_layout_manager.TaskLayoutManager
import safronov.apps.domain.repository.task_layout_manager.TaskLayoutManagerRepository

class GetTaskLayoutManagerUseCaseTest {

    private lateinit var fakeGettingTaskLayoutManager: FakeGettingTaskLayoutManager
    private lateinit var getTaskLayoutManagerUseCase: GetTaskLayoutManagerUseCase

    @Before
    fun setUp() {
        fakeGettingTaskLayoutManager = FakeGettingTaskLayoutManager()
        getTaskLayoutManagerUseCase = GetTaskLayoutManagerUseCase(
            fakeGettingTaskLayoutManager
        )
    }

    @Test
    fun execute_linear() = runBlocking {
        assertEquals(true, getTaskLayoutManagerUseCase.execute() is TaskLayoutManager.LinearLayoutManager)
    }

    @Test
    fun execute_grid() = runBlocking {
        fakeGettingTaskLayoutManager.isGrid = true
        assertEquals(true, getTaskLayoutManagerUseCase.execute() is TaskLayoutManager.GridLayoutManager)
    }

    @Test(expected = DomainException::class)
    fun execute_expectedException(): Unit = runBlocking {
        fakeGettingTaskLayoutManager.exception = true
        getTaskLayoutManagerUseCase.execute()
    }

}

private class FakeGettingTaskLayoutManager: TaskLayoutManagerRepository.GettingTaskLayoutManager {

    val grid = TaskLayoutManager.GridLayoutManager().name
    val linear = TaskLayoutManager.LinearLayoutManager().name
    var isGrid = false
    var exception = false

    override suspend fun getTaskLayoutManager(): String? {
        if (exception) throw DomainException("some exception")
        if (isGrid) return grid
        return linear
    }

}