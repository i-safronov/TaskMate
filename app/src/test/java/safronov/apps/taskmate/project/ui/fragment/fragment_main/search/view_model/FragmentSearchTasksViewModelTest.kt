package safronov.apps.taskmate.project.ui.fragment.fragment_main.search.view_model

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task.Task
import safronov.apps.domain.repository.task.TaskRepository
import safronov.apps.domain.use_case.task.read.GetTasksByTextUseCase
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList

class FragmentSearchTasksViewModelTest {

    private lateinit var fakeGettingTasksByParametersRepository: FakeGettingTasksByParametersRepository
    private lateinit var getTasksByTextUseCase: GetTasksByTextUseCase
    private lateinit var fragmentSearchTasksViewModel: FragmentSearchTasksViewModel

    @Before
    fun setUp() {
        fakeGettingTasksByParametersRepository = FakeGettingTasksByParametersRepository()
        getTasksByTextUseCase = GetTasksByTextUseCase(gettingTasksByParametersRepository = fakeGettingTasksByParametersRepository)
        fragmentSearchTasksViewModel = FragmentSearchTasksViewModel(
            dispatchersList = TestDispatchersList(),
            getTasksByTextUseCase = getTasksByTextUseCase
        )
    }

    @Test
    fun `test, search by text, should return data`() {
        assertEquals(true, fragmentSearchTasksViewModel.getTasks().value.isEmpty())
        fragmentSearchTasksViewModel.getTasksByText(text = "some text")
        assertEquals(false, fragmentSearchTasksViewModel.getTasks().value.isEmpty())
        assertEquals(true, fragmentSearchTasksViewModel.getTasks().value == fakeGettingTasksByParametersRepository.dataToReturn)
    }

    @Test
    fun `test, search by text, should return data and don't exception`() {
        assertEquals(true, fragmentSearchTasksViewModel.getTasks().value.isEmpty())
        assertEquals(true, fragmentSearchTasksViewModel.isWasException().value == null)
        fragmentSearchTasksViewModel.getTasksByText(text = "some text")
        assertEquals(false, fragmentSearchTasksViewModel.getTasks().value.isEmpty())
        assertEquals(true, fragmentSearchTasksViewModel.getTasks().value == fakeGettingTasksByParametersRepository.dataToReturn)
        assertEquals(true, fragmentSearchTasksViewModel.isWasException().value == null)
    }

    @Test
    fun `test, search by text, should return empty list`() {
        fakeGettingTasksByParametersRepository.isNeedEmptyList = true
        assertEquals(true, fragmentSearchTasksViewModel.getTasks().value.isEmpty())
        fragmentSearchTasksViewModel.getTasksByText(text = "some text")
        assertEquals(true, fragmentSearchTasksViewModel.getTasks().value.isEmpty())
    }

    @Test
    fun `test, search by text, should show that was exception`() {
        assertEquals(true, fragmentSearchTasksViewModel.isWasException().value == null)
        fakeGettingTasksByParametersRepository.isNeedToThrowException = true
        fragmentSearchTasksViewModel.getTasksByText(text = "some text")
        assertEquals(false, fragmentSearchTasksViewModel.isWasException().value == null)
        assertEquals(true, fragmentSearchTasksViewModel.isWasException().value?.message == fakeGettingTasksByParametersRepository.messageException)
    }

}

private class FakeGettingTasksByParametersRepository: TaskRepository.GettingTaskByParameters {

    val messageException = "some exception"
    var isNeedToThrowException = false
    var isNeedEmptyList = false
    val dataToReturn = listOf(
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

    override suspend fun getTasksByText(text: String): List<Task> {
        if (isNeedToThrowException) throw DomainException(messageException)
        if (isNeedEmptyList) return emptyList()
        return dataToReturn
    }

}

private class TestDispatchersList(
    private val testCoroutineDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
): DispatchersList {
    override fun io(): CoroutineDispatcher {
        return testCoroutineDispatcher
    }

    override fun ui(): CoroutineDispatcher {
        return testCoroutineDispatcher
    }
}