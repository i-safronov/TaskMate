package safronov.apps.domain.use_case.task_layout_manager

import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task_layout_manager.TaskLayoutManager
import safronov.apps.domain.repository.task_layout_manager.TaskLayoutManagerRepository

class SaveTaskLayoutManagerUseCaseTest {

    private lateinit var fakeSavingTaskLayoutManagerRepository: FakeSavingTaskLayoutManagerRepository
    private lateinit var saveTaskLayoutManagerUseCase: SaveTaskLayoutManagerUseCase

    @Before
    fun setUp() {
        fakeSavingTaskLayoutManagerRepository = FakeSavingTaskLayoutManagerRepository()
        saveTaskLayoutManagerUseCase = SaveTaskLayoutManagerUseCase(
            fakeSavingTaskLayoutManagerRepository
        )
    }

    @Test
    fun execute() = runBlocking {
        val manager = TaskLayoutManager.GridLayoutManager("grid")
        saveTaskLayoutManagerUseCase.execute(manager)
        assertEquals(true, fakeSavingTaskLayoutManagerRepository.dataToReturn == manager)
    }

    @Test(expected = DomainException::class)
    fun execute_expectedException(): Unit = runBlocking {
        fakeSavingTaskLayoutManagerRepository.exception = true
        val manager = TaskLayoutManager.GridLayoutManager("grid")
        saveTaskLayoutManagerUseCase.execute(manager)
    }

}

private class FakeSavingTaskLayoutManagerRepository: TaskLayoutManagerRepository.SavingTaskLayoutManagerRepository {

    var dataToReturn: TaskLayoutManager? = null
    var exception = false

    override suspend fun saveTaskLayoutManager(manager: TaskLayoutManager) {
        if (exception) throw DomainException("some exception")
        dataToReturn = manager
    }

}