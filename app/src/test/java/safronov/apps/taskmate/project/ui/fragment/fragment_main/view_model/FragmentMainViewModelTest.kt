package safronov.apps.taskmate.project.ui.fragment.fragment_main.view_model

import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import safronov.apps.data.data_source.local.model.converter.task.TaskEntityConverter
import safronov.apps.data.data_source.local.model.converter.task.TaskEntityConverterImpl
import safronov.apps.data.exception.DataException
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task.Task
import safronov.apps.domain.repository.task.TaskRepository
import safronov.apps.domain.use_case.task.delete.DeleteTaskListUseCase
import safronov.apps.domain.use_case.task.delete.DeleteTasksUseCase
import safronov.apps.domain.use_case.task.read.GetTasksAsFlowUseCase
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList
import java.lang.IllegalStateException

class FragmentMainViewModelTest {

    private lateinit var fakeTaskRepositoryGetting: FakeTaskRepositoryGetting
    private lateinit var fragmentMainViewModel: FragmentMainViewModel
    private lateinit var taskEntityConverter: TaskEntityConverter
    private lateinit var fakeDeletingTaskRepository: FakeDeletingTaskRepository

    @Before
    fun setUp() {
        fakeDeletingTaskRepository = FakeDeletingTaskRepository()
        fakeTaskRepositoryGetting = FakeTaskRepositoryGetting()
        fragmentMainViewModel = FragmentMainViewModel(
            dispatchersList = TestDispatchersList(),
            getTasksAsFlowUseCase = GetTasksAsFlowUseCase(gettingTaskRepository = fakeTaskRepositoryGetting),
            deleteTasksUseCase = DeleteTasksUseCase(deletingTaskRepository = fakeDeletingTaskRepository)
        )
        taskEntityConverter = TaskEntityConverterImpl(Gson())
    }

    @Test
    fun test_deleteTasks() {
        val deleted = fakeDeletingTaskRepository.dataToReturn.toList()
        fragmentMainViewModel.deleteTasks(deleted)
        assertEquals(true, fakeDeletingTaskRepository.dataToReturn.isEmpty())
        assertEquals(true, fakeDeletingTaskRepository.requestToDeleteList?.toList() == deleted)
        assertEquals(true, fakeDeletingTaskRepository.countOfRequest == 1)
    }

    @Test
    fun test_deleteTasks_expectedException() {
        assertEquals(false, fragmentMainViewModel.getIsWasException().value != null)
        fakeDeletingTaskRepository.isNeedToThrowException = true
        val deleted = fakeDeletingTaskRepository.dataToReturn
        fragmentMainViewModel.deleteTasks(deleted)
        assertEquals(true, fragmentMainViewModel.getIsWasException().value != null)
    }

    @Test
    fun test_loadTasks() = runBlocking {
        assertEquals(true, fakeTaskRepositoryGetting.countOfRequest == 0)
        assertEquals(true, fragmentMainViewModel.getTasks().first().isNullOrEmpty())
        fragmentMainViewModel.loadTasks()
        assertEquals(true, fakeTaskRepositoryGetting.countOfRequest == 1)
        assertEquals(false, fragmentMainViewModel.getTasks().first()?.isEmpty() == true)
        assertEquals(true,
            fragmentMainViewModel.getTasks().first() == fakeTaskRepositoryGetting.dataToReturn
            )
    }

    @Test
    fun test_loadTasks_expectedException() = runBlocking {
        fakeTaskRepositoryGetting.isNeedToThrowException = true
        assertEquals(true, fakeTaskRepositoryGetting.countOfRequest == 0)
        assertEquals(true, fragmentMainViewModel.getTasks().first().isNullOrEmpty())
        assertEquals(true, fragmentMainViewModel.getIsWasException().first() == null)
        fragmentMainViewModel.loadTasks()
        assertEquals(false, fragmentMainViewModel.getIsWasException().first() == null)
        assertEquals(true, fragmentMainViewModel.getIsWasException().first() is DomainException)
    }

}

private class FakeTaskRepositoryGetting: TaskRepository.GettingTask {

    var isNeedToThrowException = false
    var countOfRequest = 0
    val dataToReturn = listOf<Task>(
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
        if (isNeedToThrowException) throw DomainException("some exception")
        countOfRequest++
        return flow {
            emit(dataToReturn)
        }
    }

    override suspend fun getTasks(): List<Task> {
        throw IllegalStateException("don't use this method -_- ")
    }

}

private class FakeDeletingTaskRepository: TaskRepository.DeletingTask {

    var isNeedToThrowException = false
    val dataToReturn = mutableListOf<Task>(
        Task.TaskText(title = null, text = null, date = null, taskCategoryId = null, taskType = null, isPinned = false, id = null),
        Task.TaskList(title = null, list = null, date = null, taskCategoryId = null, taskType = null, isPinned = false, id = null)
    )
    var requestToDeleteList: List<Task>? = null
    var countOfRequest = 0

    override suspend fun deleteTaskText(task: Task.TaskText) {
        throw IllegalStateException("don't use this method")
    }

    override suspend fun deleteTaskList(task: Task.TaskList) {
        throw IllegalStateException("don't use this method")
    }

    override suspend fun deleteTasks(tasks: List<Task>) {
        if (isNeedToThrowException) throw DomainException("some exception")
        requestToDeleteList = tasks
        countOfRequest++
        dataToReturn.clear()
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