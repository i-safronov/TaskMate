package safronov.apps.taskmate.project.ui.fragment.fragment_main.task_list_details.view_model

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task.Task
import safronov.apps.domain.model.task_category.TaskCategory
import safronov.apps.domain.model.task_category.category_type.CategoryTypes
import safronov.apps.domain.repository.task.TaskRepository
import safronov.apps.domain.repository.task_category.TaskCategoryRepository
import safronov.apps.domain.use_case.task.create.InsertTaskListUseCase
import safronov.apps.domain.use_case.task.update.ChangeTaskListUseCase
import safronov.apps.domain.use_case.task_category.read.GetTaskCategoryByIdUseCase
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList
import safronov.apps.taskmate.project.system_settings.data.DefaultTaskCategories
import safronov.apps.taskmate.project.system_settings.date.Date
import java.lang.IllegalStateException

class FragmentTaskListDetailsViewModelTest {

    private lateinit var fakeInsertingTaskRepository: FakeInsertingTaskRepository
    private lateinit var fakeChangingTaskRepository: FakeChangingTaskRepository
    private lateinit var fakeDefaultTaskCategories: FakeDefaultTaskCategories
    private lateinit var fakeDate: FakeDate
    private lateinit var testDispatchersList: TestDispatchersList
    private lateinit var fragmentTaskListDetailsViewModel: FragmentTaskListDetailsViewModel
    private lateinit var insertTaskListUseCase: InsertTaskListUseCase
    private lateinit var changeTaskListUseCase: ChangeTaskListUseCase
    private lateinit var taskCategory: TaskCategory
    private lateinit var fakeTaskCategoryRepository: FakeTaskCategoryRepository

    @Before
    fun setUp() {
        fakeTaskCategoryRepository = FakeTaskCategoryRepository()
        fakeInsertingTaskRepository = FakeInsertingTaskRepository()
        fakeChangingTaskRepository = FakeChangingTaskRepository()
        fakeDefaultTaskCategories = FakeDefaultTaskCategories()
        fakeDate = FakeDate()
        taskCategory = TaskCategory(
            id = 55,
            icon = 325232,
            backgroundColor = 143463,
            categoryName = "some asda",
            categoryType = CategoryTypes.System
        )
        testDispatchersList = TestDispatchersList()
        insertTaskListUseCase = InsertTaskListUseCase(insertingTask = fakeInsertingTaskRepository)
        changeTaskListUseCase = ChangeTaskListUseCase(changingTaskRepository = fakeChangingTaskRepository)
        fragmentTaskListDetailsViewModel = FragmentTaskListDetailsViewModel(
            dispatchersList = testDispatchersList,
            date = fakeDate,
            insertTaskListUseCase = insertTaskListUseCase,
            changeTaskListUseCase = changeTaskListUseCase,
            defaultTaskCategories = fakeDefaultTaskCategories,
            getTaskCategoryByIdUseCase = GetTaskCategoryByIdUseCase(fakeTaskCategoryRepository)
        )
    }

    @Test
    fun test_prepareChangeExistingTask() {
        val defaultValue = Task.TaskList(
            title = "asdf",
            list = listOf(Task.TaskListItem(title = "asfd", isChecked = true)),
            date = "fasdf",
            taskCategoryId = fakeTaskCategoryRepository.dataToReturn.id,
            taskType = Task.TaskType.Text,
            isPinned = false,
            id = 235423
        )
        fragmentTaskListDetailsViewModel.prepareToChangeExistingTask(defaultValue)
        assertEquals(true, defaultValue.title == fragmentTaskListDetailsViewModel.getCurrentTaskTitle().value)
        assertEquals(true, defaultValue.list == fragmentTaskListDetailsViewModel.getCurrentTaskListItems())
        assertEquals(true, defaultValue.taskCategoryId == fragmentTaskListDetailsViewModel.getCurrentTaskCategory().value?.id)
        assertEquals(true, defaultValue.isPinned == fragmentTaskListDetailsViewModel.getIsCurrentTaskPin().value)
    }

    @Test
    fun test_prepareChangeExistingTask_expectedException() {
        fakeTaskCategoryRepository.isNeedToThrowException = true
        assertEquals(false, fragmentTaskListDetailsViewModel.isWasException().value != null)
        val defaultValue = Task.TaskList(
            title = "asdf",
            list = listOf(Task.TaskListItem(title = "asfd", isChecked = true)),
            date = "fasdf",
            taskCategoryId = fakeTaskCategoryRepository.dataToReturn.id,
            taskType = Task.TaskType.Text,
            isPinned = false,
            id = 235423
        )
        fragmentTaskListDetailsViewModel.prepareToChangeExistingTask(defaultValue)
        assertEquals(true, fragmentTaskListDetailsViewModel.isWasException().value != null)
    }

    @Test
    fun `test, save task title`() {
        val taskTitle = "some title 123"
        assertEquals(true, fragmentTaskListDetailsViewModel.getCurrentTaskTitle().value.isEmpty())
        fragmentTaskListDetailsViewModel.saveCurrentTaskTitle(title = taskTitle)
        assertEquals(false, fragmentTaskListDetailsViewModel.getCurrentTaskTitle().value.isEmpty())
    }

    @Test
    fun `test, save task pin`() {
        assertEquals(false, fragmentTaskListDetailsViewModel.getIsCurrentTaskPin().value)
        fragmentTaskListDetailsViewModel.pinCurrentTask()
        assertEquals(true, fragmentTaskListDetailsViewModel.getIsCurrentTaskPin().value)
    }

    @Test
    fun `test, save task pin twice, should pin is be false`() {
        assertEquals(false, fragmentTaskListDetailsViewModel.getIsCurrentTaskPin().value)
        fragmentTaskListDetailsViewModel.pinCurrentTask()
        assertEquals(true, fragmentTaskListDetailsViewModel.getIsCurrentTaskPin().value)
        fragmentTaskListDetailsViewModel.pinCurrentTask()
        assertEquals(false, fragmentTaskListDetailsViewModel.getIsCurrentTaskPin().value)
    }

    @Test
    fun `test, save task category`() {
        assertEquals(true, fragmentTaskListDetailsViewModel.getCurrentTaskCategory().value == null)
        fragmentTaskListDetailsViewModel.saveCurrentTaskCategory(taskCategory = taskCategory)
        assertEquals(false, fragmentTaskListDetailsViewModel.getCurrentTaskCategory().value == null)
        assertEquals(true, fragmentTaskListDetailsViewModel.getCurrentTaskCategory().value == taskCategory)
    }

    @Test
    fun test_saveCurrentTaskListItems() {
        assertEquals(true, fragmentTaskListDetailsViewModel.getCurrentTaskListItems().isEmpty())
        val list = listOf(
            Task.TaskListItem(
                title = "some title",
                isChecked = true
            )
        )
        fragmentTaskListDetailsViewModel.saveCurrentTaskListItems(
            taskListItems = list
        )
        assertEquals(false, fragmentTaskListDetailsViewModel.getCurrentTaskListItems().isEmpty())
        assertEquals(true, list == fragmentTaskListDetailsViewModel.getCurrentTaskListItems())
    }

    @Test
    fun `test, load default task category`() {
        assertEquals(true, fragmentTaskListDetailsViewModel.getCurrentTaskCategory()?.value == null)
        fragmentTaskListDetailsViewModel.loadDefaultCurrentTaskCategory()
        assertEquals(true, fragmentTaskListDetailsViewModel.getCurrentTaskCategory()?.value == fakeDefaultTaskCategories.taskCategoryToReturn)
    }

    @Test
    fun test_getTaskCategories() {
        val categories = fragmentTaskListDetailsViewModel.getTaskCategories()
        assertEquals(true, fakeDefaultTaskCategories.taskCategoriesToReturn == categories)
    }

    @Test
    fun test_loadDefaultTaskCategoryWhenCategoryExists() {
        assertEquals(true, fragmentTaskListDetailsViewModel.getCurrentTaskCategory().value == null)
        assertEquals(false, fragmentTaskListDetailsViewModel.getCurrentTaskCategory().value == fakeDefaultTaskCategories.taskCategoryToReturn)
        assertEquals(true, fakeDefaultTaskCategories.countOfRequestToGetDefaultTaskCategory == 0)
        fragmentTaskListDetailsViewModel.loadDefaultCurrentTaskCategory()
        assertEquals(true, fakeDefaultTaskCategories.countOfRequestToGetDefaultTaskCategory == 1)
        assertEquals(false, fragmentTaskListDetailsViewModel.getCurrentTaskCategory().value == null)
        assertEquals(true, fragmentTaskListDetailsViewModel.getCurrentTaskCategory().value == fakeDefaultTaskCategories.taskCategoryToReturn)
        fragmentTaskListDetailsViewModel.loadDefaultCurrentTaskCategory()
        assertEquals(true, fakeDefaultTaskCategories.countOfRequestToGetDefaultTaskCategory == 1)
    }

    @Test
    fun `test, save current task`() {
        val taskListItems = listOf(
            Task.TaskListItem(
                title = "some title",
                isChecked = false
            )
        )
        assertEquals(true, fragmentTaskListDetailsViewModel.getCurrentTaskListItems().isEmpty())
        fragmentTaskListDetailsViewModel.saveCurrentTaskListItems(taskListItems = taskListItems)
        assertEquals(false, fragmentTaskListDetailsViewModel.getCurrentTaskListItems().isEmpty())
        assertEquals(true, fragmentTaskListDetailsViewModel.getCurrentTaskListItems() == taskListItems)
    }

    @Test
    fun `test, get current task items after saved current task`() {
        val taskItem = Task.TaskListItem(
            title = "some title",
            isChecked = false
        )
        val taskListItems = listOf(
            taskItem
        )
        assertEquals(true, fragmentTaskListDetailsViewModel.getCurrentTaskListItems().isEmpty())
        fragmentTaskListDetailsViewModel.saveCurrentTaskListItems(taskListItems = taskListItems)
        assertEquals(true, fragmentTaskListDetailsViewModel.getCurrentTaskListItems() == taskListItems)
        assertEquals(false, fragmentTaskListDetailsViewModel.getCurrentTaskListItems().isEmpty())
    }

    @Test
    fun `test, save current task twice, should update prev item`() {

        val taskItem = Task.TaskListItem(
            title = "some title",
            isChecked = false
        )
        val taskListItems = listOf(
            taskItem
        )
        assertEquals(true, fragmentTaskListDetailsViewModel.getCurrentTaskListItems().isEmpty())
        fragmentTaskListDetailsViewModel.saveCurrentTaskListItems(taskListItems = taskListItems)
        assertEquals(true, fragmentTaskListDetailsViewModel.getCurrentTaskListItems() == taskListItems)
        assertEquals(false, fragmentTaskListDetailsViewModel.getCurrentTaskListItems().isEmpty())

        taskItem.isChecked = true
        taskItem.title = "some new title"

        fragmentTaskListDetailsViewModel.saveCurrentTaskListItems(taskListItems = taskListItems)
        assertEquals(true, fragmentTaskListDetailsViewModel.getCurrentTaskListItems() == taskListItems)
        assertEquals(false, fragmentTaskListDetailsViewModel.getCurrentTaskListItems().isEmpty())

    }

    @Test
    fun `test, save current task and save again current task when nothing has been changed`() {
        val taskItem = Task.TaskListItem(
            title = "some title",
            isChecked = false
        )
        val taskListItems = listOf(
            taskItem
        )
        assertEquals(true, fragmentTaskListDetailsViewModel.getCurrentTaskListItems().isEmpty())
        fragmentTaskListDetailsViewModel.saveCurrentTaskListItems(taskListItems = taskListItems)
        assertEquals(true, fragmentTaskListDetailsViewModel.getCurrentTaskListItems() == taskListItems)
        assertEquals(false, fragmentTaskListDetailsViewModel.getCurrentTaskListItems().isEmpty())
        fragmentTaskListDetailsViewModel.saveCurrentTaskListItems(taskListItems = taskListItems)
        assertEquals(true, fragmentTaskListDetailsViewModel.getCurrentTaskListItems() == taskListItems)
        assertEquals(false, fragmentTaskListDetailsViewModel.getCurrentTaskListItems().isEmpty())
    }

    @Test
    fun `test, get task title`() {
        val taskTitle = "some title 123"
        assertEquals(true, fragmentTaskListDetailsViewModel.getCurrentTaskTitle().value.isEmpty())
        fragmentTaskListDetailsViewModel.saveCurrentTaskTitle(title = taskTitle)
        assertEquals(false, fragmentTaskListDetailsViewModel.getCurrentTaskTitle().value.isEmpty())
        assertEquals(true, fragmentTaskListDetailsViewModel.getCurrentTaskTitle().value == taskTitle)
    }

    @Test
    fun `test, get task list items`() {
        val taskItem = Task.TaskListItem(
            title = "some title",
            isChecked = false
        )
        val taskListItems = listOf(
            taskItem
        )
        assertEquals(true, fragmentTaskListDetailsViewModel.getCurrentTaskListItems().isEmpty())
        fragmentTaskListDetailsViewModel.saveCurrentTaskListItems(taskListItems = taskListItems)
        assertEquals(true, fragmentTaskListDetailsViewModel.getCurrentTaskListItems() == taskListItems)
    }

    @Test
    fun `test, get is task pinned`() {
        assertEquals(false, fragmentTaskListDetailsViewModel.getIsCurrentTaskPin().value)
        fragmentTaskListDetailsViewModel.pinCurrentTask()
        assertEquals(true, fragmentTaskListDetailsViewModel.getIsCurrentTaskPin().value)
    }

    @Test
    fun `test, get task category`() {
        assertEquals(true, fragmentTaskListDetailsViewModel.getCurrentTaskCategory()?.value == null)
        fragmentTaskListDetailsViewModel.saveCurrentTaskCategory(taskCategory = taskCategory)
        assertEquals(false, fragmentTaskListDetailsViewModel.getCurrentTaskCategory()?.value == null)
        assertEquals(true, fragmentTaskListDetailsViewModel.getCurrentTaskCategory()?.value == taskCategory)
    }

    @Test
    fun `test, get current time`() {
        assertEquals(true, fragmentTaskListDetailsViewModel.getCurrentTime() == fakeDate.getCurrentTime())
    }

    @Test
    fun `test, get is was exception, should return null`() {
        assertEquals(true, fragmentTaskListDetailsViewModel.isWasException()?.value == null)
    }

    @Test
    fun `test, get is was exception, should return domain exception`() {
        fakeInsertingTaskRepository.isNeedToThrowException = true
        val taskItem = Task.TaskListItem(
            title = "some title",
            isChecked = false
        )
        val taskListItems = listOf(
            taskItem
        )
        fragmentTaskListDetailsViewModel.saveCurrentTaskListItems(taskListItems)
        assertEquals(true, fragmentTaskListDetailsViewModel.isWasException()?.value == null)
        fragmentTaskListDetailsViewModel.saveCurrentTask()
        assertEquals(false, fragmentTaskListDetailsViewModel.isWasException()?.value == null)
    }

    @Test
    fun `test, save current task when title and list is empty`() {
        assertEquals(true, fakeInsertingTaskRepository.countOfRequest == 0)
        fragmentTaskListDetailsViewModel.saveCurrentTaskListItems(taskListItems = emptyList<Task.TaskListItem>())
        assertEquals(true, fakeInsertingTaskRepository.countOfRequest == 0)
    }

    @Test
    fun `test, task saved after insert item, should be null`() {
        val taskItem = Task.TaskListItem(
            title = "some title",
            isChecked = false
        )
        val taskListItems = listOf(
            taskItem
        )
        assertEquals(true, fragmentTaskListDetailsViewModel.getCurrentTaskListItems().isEmpty())
        assertEquals(true, fragmentTaskListDetailsViewModel.getTaskSaved().value == null)
        fragmentTaskListDetailsViewModel.saveCurrentTaskListItems(taskListItems = taskListItems)
        fragmentTaskListDetailsViewModel.saveCurrentTask()
        assertEquals(true, fragmentTaskListDetailsViewModel.getCurrentTaskListItems() == taskListItems)
        assertEquals(true, fragmentTaskListDetailsViewModel.getTaskSaved().value == true)
    }

}

private class FakeInsertingTaskRepository: TaskRepository.InsertingTask {

    var isNeedToThrowException = false
    var countOfRequest = 0
    var dataToReturn = Task.TaskList(
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

    override suspend fun insertTaskText(task: Task.TaskText): Long? {
        throw IllegalStateException("don't user this method -_- ")
    }

    override suspend fun insertTaskList(task: Task.TaskList): Long? {
        if (isNeedToThrowException) throw DomainException("some exception")
        dataToReturn = task
        countOfRequest++
        return dataToReturn.id
    }

}

private class FakeTaskCategoryRepository: TaskCategoryRepository {

    var isNeedToThrowException = false
    val dataToReturn = TaskCategory(
        id = 252,
        icon = 43543,
        backgroundColor = 324534,
        categoryName = "asdfa",
        categoryType = CategoryTypes.User
    )

    override suspend fun insertTaskCategories(list: List<TaskCategory>) {
        throw IllegalStateException("don't use this method")
    }

    override suspend fun getTaskCategories(): Flow<List<TaskCategory>> {
        throw IllegalStateException("don't use this method")
    }

    override suspend fun getTaskCategoryById(id: String): TaskCategory? {
        if (isNeedToThrowException) throw DomainException("some exception :)")
        return dataToReturn
    }

    override suspend fun updateTaskCategory(taskCategory: TaskCategory) {
        throw IllegalStateException("don't use this method")
    }

    override suspend fun clearTaskCategories() {
        throw IllegalStateException("don't use this method")
    }

}

private class FakeChangingTaskRepository: TaskRepository.ChangingTask {

    var isNeedToThrowException = false
    var dataToReturn = Task.TaskList(
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

    override suspend fun changeTaskText(task: Task.TaskText) {
        throw IllegalStateException("don't use this method -_- ")
    }

    override suspend fun changeTaskList(task: Task.TaskList) {
        if (isNeedToThrowException) throw DomainException("some exception")
        dataToReturn = task
    }

    override suspend fun changeTasks(tasks: List<Task>) {
        throw IllegalStateException("don't use this method -_- ")
    }

}

private class FakeDefaultTaskCategories: DefaultTaskCategories {

    var countOfRequestToGetDefaultTaskCategory = 0
    val taskCategoryToReturn = TaskCategory(
        icon = 6324,
        backgroundColor = 4343,
        categoryName = "some name",
        categoryType = CategoryTypes.User
    )
    val taskCategoriesToReturn = listOf(
        TaskCategory(
            icon = 6324,
            backgroundColor = 4343,
            categoryName = "some name",
            categoryType = CategoryTypes.User
        )
    )

    override fun getDefaultTaskCategories(): List<TaskCategory> {
        return taskCategoriesToReturn
    }

    override fun getDefaultTaskCategory(): TaskCategory {
        countOfRequestToGetDefaultTaskCategory++
        return taskCategoryToReturn
    }

}

private class FakeDate: Date {
    override fun getCurrentTime(): String {
        return "today"
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