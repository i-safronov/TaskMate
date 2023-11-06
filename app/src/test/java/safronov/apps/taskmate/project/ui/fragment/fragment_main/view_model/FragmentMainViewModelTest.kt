package safronov.apps.taskmate.project.ui.fragment.fragment_main.view_model

import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import safronov.apps.data.data_source.local.model.converter.task.TaskEntityConverter
import safronov.apps.data.data_source.local.model.converter.task.TaskEntityConverterImpl
import safronov.apps.data.exception.DataException
import safronov.apps.domain.model.task.Task
import safronov.apps.domain.repository.task.TaskRepository
import safronov.apps.domain.use_case.task.read.GetTasksAsFlowUseCase
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList
import java.lang.IllegalStateException

class FragmentMainViewModelTest {

    private lateinit var fakeTaskRepositoryGetting: FakeTaskRepositoryGetting
    private lateinit var fragmentMainViewModel: FragmentMainViewModel
    private lateinit var taskEntityConverter: TaskEntityConverter

    @Before
    fun setUp() {
        fakeTaskRepositoryGetting = FakeTaskRepositoryGetting()
        fragmentMainViewModel = FragmentMainViewModel(
            dispatchersList = TestDispatchersList(),
            getTasksAsFlowUseCase = GetTasksAsFlowUseCase(gettingTaskRepository = fakeTaskRepositoryGetting)
        )
        taskEntityConverter = TaskEntityConverterImpl(Gson())
    }

    @Test
    fun test_loadTasks() {
        assertEquals(true, fakeTaskRepositoryGetting.countOfRequest == 0)
        assertEquals(true, fragmentMainViewModel.getTasks().first().isEmpty() == true)
        fragmentMainViewModel.loadTasks()
        assertEquals(true, fakeTaskRepositoryGetting.countOfRequest == 1)
        assertEquals(false, fragmentMainViewModel.getTasks().first().isEmpty() == true)
        assertEquals(true,
            taskEntityConverter
                .convertListOfTaskToListOfTaskEntity(fragmentMainViewModel.getTasks().first().value())
                    == fakeTaskRepositoryGetting.dataToReturn
            )
    }

    @Test
    fun test_loadTasks_expectedException() {
        fakeTaskRepositoryGetting.isNeedToThrowException = true
        assertEquals(true, fakeTaskRepositoryGetting.countOfRequest == 0)
        assertEquals(true, fragmentMainViewModel.getTasks().first().isEmpty() == true)
        assertEquals(true, fragmentMainViewModel.getIsWasException().first().value == null)
        fragmentMainViewModel.loadTasks()
        assertEquals(false, fragmentMainViewModel.getIsWasException().first().value == null)
    }

}

private class FakeTaskRepositoryGetting: TaskRepository.GettingTask {

    var isNeedToThrowException = false
    var countOfRequest = 0
    val dataToReturn = listOf(
        Task.TaskText(
            title = "asometr",
            text = "fasdf",
            date = "today",
            taskCategoryId = 45,
            taskType = Task.TaskType.Text,
            isPinned = false,
            id = 43
        ),
        Task.TaskList(
            title = "asometr",
            list = listOf(
                Task.TaskListItem(
                    title = "somet",
                    isChecked = true
                )
            ),
            date = "today",
            taskCategoryId = 45,
            taskType = Task.TaskType.List,
            isPinned = false,
            id = 43
        )
    )

    override suspend fun getTasksAsFlow(): Flow<List<Task>> {
        if (isNeedToThrowException) throw DataException("some exception")
        countOfRequest++
        return flow {
            emit(dataToReturn)
        }
    }

    override suspend fun getTasks(): List<Task> {
        throw IllegalStateException("don't use this method -_- ")
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