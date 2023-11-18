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
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task.Task
import safronov.apps.domain.model.task_category.TaskCategory
import safronov.apps.domain.model.task_category.category_type.CategoryTypes
import safronov.apps.domain.model.task_layout_manager.TaskLayoutManager
import safronov.apps.domain.repository.task.TaskRepository
import safronov.apps.domain.repository.task_category.TaskCategoryRepository
import safronov.apps.domain.repository.task_layout_manager.TaskLayoutManagerRepository
import safronov.apps.domain.use_case.task.delete.DeleteTasksUseCase
import safronov.apps.domain.use_case.task.read.GetTasksAsFlowByTaskCategoryUseCase
import safronov.apps.domain.use_case.task_category.read.GetTaskCategoriesUseCase
import safronov.apps.domain.use_case.task_category.update.UpdateTaskCategoriesUseCase
import safronov.apps.domain.use_case.task_layout_manager.GetTaskLayoutManagerUseCase
import safronov.apps.domain.use_case.task_layout_manager.SaveTaskLayoutManagerUseCase
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList
import java.lang.IllegalStateException

class FragmentMainViewModelTest {

    private lateinit var fakeTaskRepositoryGetting: FakeTaskRepositoryGetting
    private lateinit var fragmentMainViewModel: FragmentMainViewModel
    private lateinit var taskEntityConverter: TaskEntityConverter
    private lateinit var fakeDeletingTaskRepository: FakeDeletingTaskRepository
    private lateinit var fakeTaskCategoryRepository: FakeTaskCategoryRepository
    private lateinit var fakeTaskRepositoryGettingByParams: FakeTaskRepositoryGettingByParams
    private lateinit var fakeMutableTaskLayoutManager: FakeMutableTaskLayoutManager

    @Before
    fun setUp() {
        fakeMutableTaskLayoutManager = FakeMutableTaskLayoutManager()
        fakeTaskRepositoryGettingByParams = FakeTaskRepositoryGettingByParams()
        fakeDeletingTaskRepository = FakeDeletingTaskRepository()
        fakeTaskRepositoryGetting = FakeTaskRepositoryGetting()
        fakeTaskCategoryRepository = FakeTaskCategoryRepository()
        fragmentMainViewModel = FragmentMainViewModel(
            dispatchersList = TestDispatchersList(),
            deleteTasksUseCase = DeleteTasksUseCase(deletingTaskRepository = fakeDeletingTaskRepository),
            getTaskCategoriesUseCase = GetTaskCategoriesUseCase(
                fakeTaskCategoryRepository
            ),
            updateTaskCategoriesUseCase = UpdateTaskCategoriesUseCase(fakeTaskCategoryRepository),
            getTasksAsFlowByTaskCategoryUseCase = GetTasksAsFlowByTaskCategoryUseCase(
                fakeTaskRepositoryGettingByParams,
                fakeTaskRepositoryGetting
            ),
            getTaskLayoutManagerUseCase = GetTaskLayoutManagerUseCase(gettingTaskLayoutManager = fakeMutableTaskLayoutManager),
            saveTaskLayoutManagerUseCase = SaveTaskLayoutManagerUseCase(savingTaskLayoutManagerRepository = fakeMutableTaskLayoutManager)
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
    fun test_loadPage() = runBlocking {
        assertEquals(true, fakeTaskRepositoryGetting.countOfRequest == 0)
        assertEquals(true, fragmentMainViewModel.getTasks().first().isNullOrEmpty())
        fragmentMainViewModel.loadPage()
        assertEquals(true, fakeTaskRepositoryGetting.countOfRequest == 0)
        assertEquals(true, fakeTaskRepositoryGettingByParams.requestCount == 1)
        assertEquals(false, fragmentMainViewModel.getTasks().first()?.isEmpty() == true)
        assertEquals(true,
            fragmentMainViewModel.getTasks().first() == fakeTaskRepositoryGettingByParams.dataToReturn
            )
    }

    @Test
    fun loadTaskBySystemTaskCategory() = runBlocking {
        assertEquals(true, fakeTaskRepositoryGetting.countOfRequest == 0)
        assertEquals(true, fakeTaskRepositoryGettingByParams.requestCount == 0)
        assertEquals(true, fragmentMainViewModel.getTasks().first().isNullOrEmpty())
        assertEquals(true, fragmentMainViewModel.getCategory().value == null)
        fakeTaskCategoryRepository.isSystem = true
        fragmentMainViewModel.loadPage()
        assertEquals(true, fakeTaskRepositoryGetting.countOfRequest == 1)
        assertEquals(true, fakeTaskRepositoryGettingByParams.requestCount == 0)
        assertEquals(false, fragmentMainViewModel.getTasks().first()?.isEmpty() == true)
        assertEquals(true, fragmentMainViewModel.getCategory().value == fakeTaskCategoryRepository.dataToReturn.first())
        assertEquals(false, fragmentMainViewModel.getCategory().value == null)
        assertEquals(true,
            fragmentMainViewModel.getTasks().first() == fakeTaskRepositoryGetting.dataToReturn
        )
    }

    @Test
    fun test_loadPageWithoutTaskCategory_expectedException() = runBlocking {
        assertEquals(true, fakeTaskRepositoryGetting.countOfRequest == 0)
        assertEquals(true, fakeTaskRepositoryGettingByParams.requestCount == 0)
        assertEquals(true, fragmentMainViewModel.getTasks().first().isNullOrEmpty())
        assertEquals(true, fragmentMainViewModel.getCategory().value == null)
        fakeTaskCategoryRepository.isEmpty = true
        fragmentMainViewModel.loadPage()
        assertEquals(true, fakeTaskRepositoryGetting.countOfRequest == 0)
        assertEquals(true, fakeTaskRepositoryGettingByParams.requestCount == 0)
        assertEquals(true, fragmentMainViewModel.getTasks().first()?.isEmpty() == null)
        assertEquals(true, fragmentMainViewModel.getIsWasException().value != null)
    }

    @Test
    fun test_loadPage_expectedException() = runBlocking {
        fakeTaskRepositoryGettingByParams.isNeedToThrowException = true
        assertEquals(true, fakeTaskRepositoryGetting.countOfRequest == 0)
        assertEquals(true, fragmentMainViewModel.getTasks().first().isNullOrEmpty())
        assertEquals(true, fragmentMainViewModel.getIsWasException().first() == null)
        fragmentMainViewModel.loadPage()
        assertEquals(false, fragmentMainViewModel.getIsWasException().first() == null)
        assertEquals(true, fragmentMainViewModel.getIsWasException().value is DomainException)
    }

    @Test
    fun test_loadPageWithTaskCategories() = runBlocking {
        assertEquals(true, fakeTaskRepositoryGetting.countOfRequest == 0)
        assertEquals(true, fragmentMainViewModel.getTasks().first().isNullOrEmpty())
        fragmentMainViewModel.loadPage()
        assertEquals(true, fakeTaskRepositoryGetting.countOfRequest == 0)
        assertEquals(true, fakeTaskRepositoryGettingByParams.requestCount == 1)
        assertEquals(false, fragmentMainViewModel.getTasks().first()?.isEmpty() == true)
        assertEquals(true,
            fragmentMainViewModel.getTasks().first() == fakeTaskRepositoryGettingByParams.dataToReturn
        )
        assertEquals(true, fragmentMainViewModel.getCategories().first() == fakeTaskCategoryRepository.dataToReturn)
    }

    @Test
    fun testUpdateTaskCategories() {
        val data = fakeTaskCategoryRepository.dataToReturn
        fragmentMainViewModel.updateTaskCategories(data)
        assertEquals(true, fakeTaskCategoryRepository.dataToReturn == data)
    }

    @Test
    fun testUpdateTaskCategories_expectedException() {
        val data = fakeTaskCategoryRepository.dataToReturn
        fakeTaskCategoryRepository.isNeedToThrowException = true
        fragmentMainViewModel.updateTaskCategories(data)
        assertEquals(true, fragmentMainViewModel.getIsWasException().value != null)
    }

    @Test
    fun loadPage_checkTaskLayoutManagerLinear() = runBlocking {
        assertEquals(true, fragmentMainViewModel.getTaskLayoutManager().value == null)
        fragmentMainViewModel.loadPage()
        assertEquals(false, fragmentMainViewModel.getTaskLayoutManager().value == null)
        assertEquals(true, fragmentMainViewModel.getTaskLayoutManager().value?.name == fakeMutableTaskLayoutManager.linear)
    }

    @Test
    fun loadPage_checkTaskLayoutManagerGrid() = runBlocking {
        fakeMutableTaskLayoutManager.isGrid = true
        assertEquals(true, fragmentMainViewModel.getTaskLayoutManager().value == null)
        fragmentMainViewModel.loadPage()
        assertEquals(false, fragmentMainViewModel.getTaskLayoutManager().value == null)
        assertEquals(true, fragmentMainViewModel.getTaskLayoutManager().value?.name == fakeMutableTaskLayoutManager.grid)
    }

    @Test
    fun saveTaskLayoutManager_linear() {
        val manager = TaskLayoutManager.LinearLayoutManager()
        fragmentMainViewModel.saveTaskLayoutManagerUseCase(manager)
        assertEquals(true, fakeMutableTaskLayoutManager.savedManager == manager)
        assertEquals(true, fakeMutableTaskLayoutManager.savedRequests == 1)
        assertEquals(true, fragmentMainViewModel.getIsWasException().value == null)
    }

    @Test
    fun saveTaskLayoutManager_grid() {
        val manager = TaskLayoutManager.GridLayoutManager()
        fragmentMainViewModel.saveTaskLayoutManagerUseCase(manager)
        assertEquals(true, fakeMutableTaskLayoutManager.savedManager == manager)
        assertEquals(true, fakeMutableTaskLayoutManager.savedRequests == 1)
        assertEquals(true, fragmentMainViewModel.getIsWasException().value == null)
    }

    @Test
    fun saveTaskLayoutManager_expectedException() {
        fakeMutableTaskLayoutManager.exception = true
        val manager = TaskLayoutManager.GridLayoutManager()
        fragmentMainViewModel.saveTaskLayoutManagerUseCase(manager)
        assertEquals(true, fragmentMainViewModel.getIsWasException().value != null)
    }

}

private class FakeMutableTaskLayoutManager: TaskLayoutManagerRepository.MutableTaskLayoutManagerRepository {

    val grid = TaskLayoutManager.GridLayoutManager().name
    val linear = TaskLayoutManager.LinearLayoutManager().name
    var isGrid = false
    var exception = false
    var savedManager: TaskLayoutManager? = null
    var savedRequests = 0

    override suspend fun saveTaskLayoutManager(manager: TaskLayoutManager) {
        if (exception) throw DomainException("some exception")
        savedRequests++
        savedManager = manager
    }

    override suspend fun getTaskLayoutManager(): String? {
        if (exception) throw DomainException("some exception")
        if (isGrid) return grid
        return linear
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

private class FakeTaskRepositoryGettingByParams: TaskRepository.GettingTaskByParameters {

    var isNeedToThrowException = false
    var requestCategory: TaskCategory? = null
    var requestCount = 0
    val taskCategory = TaskCategory(
        id = 2342,
        icon = null,
        backgroundColor = null,
        categoryName = "asdf",
        categoryType = CategoryTypes.User
    )
    val dataToReturn = listOf<Task>(
        Task.TaskText(
            title = "asdf",
            text = "asdf",
            date = "asdf",
            taskCategoryId = taskCategory.id,
            taskType = Task.TaskType.Text,
            isPinned = true,
            id = 32423
        )
    )

    override suspend fun getTasksByText(text: String): List<Task> {
        throw IllegalStateException("don't use this method")
    }

    override suspend fun getTasksAsFlowByTaskCategory(taskCategory: TaskCategory): Flow<List<Task>> {
        if (isNeedToThrowException) throw DomainException("some exception")
        requestCategory = taskCategory
        requestCount++
        return flow {
            emit(dataToReturn)
        }
    }

}

private class FakeTaskCategoryRepository: TaskCategoryRepository {

    var isNeedToThrowException = false
    var requests = 0
    var isSystem = false
    var isEmpty = false
    var dataToReturn = listOf(TaskCategory(
        id = 2342,
        icon = null,
        backgroundColor = 23423,
        categoryName = "alsjdkf",
        categoryType = CategoryTypes.User
    ))

    override suspend fun insertTaskCategories(list: List<TaskCategory>) {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskCategories(): Flow<List<TaskCategory>> {
        return flow {
            if (isSystem) {
                dataToReturn.first().categoryType = CategoryTypes.System
            }
            if (isEmpty) {
                emit(emptyList())
            } else {
                emit(dataToReturn)
            }
        }
    }

    override suspend fun getTaskCategoryById(id: String): TaskCategory? {
        TODO("Not yet implemented")
    }

    override suspend fun updateTaskCategory(taskCategory: TaskCategory) {
        TODO("Not yet implemented")
    }

    override suspend fun updateTaskCategories(categories: List<TaskCategory>) {
        if (isNeedToThrowException) throw DomainException("some exception")
        requests++
        dataToReturn = categories
    }

    override suspend fun clearTaskCategories() {
        TODO("Not yet implemented")
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