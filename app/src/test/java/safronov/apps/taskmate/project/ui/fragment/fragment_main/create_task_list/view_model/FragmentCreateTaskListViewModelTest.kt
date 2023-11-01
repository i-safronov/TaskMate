package safronov.apps.taskmate.project.ui.fragment.fragment_main.create_task_list.view_model

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task.Task
import safronov.apps.domain.model.task_category.TaskCategory
import safronov.apps.domain.model.task_category.category_type.CategoryTypes
import safronov.apps.domain.repository.task.TaskRepository
import safronov.apps.domain.use_case.task.create.InsertTaskListUseCase
import safronov.apps.domain.use_case.task.update.ChangeTaskListUseCase
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList
import safronov.apps.taskmate.project.system_settings.data.DefaultTaskCategories
import safronov.apps.taskmate.project.system_settings.date.Date
import java.lang.IllegalStateException

/*
* actions:
* save:
* save title, save added item in list, save pin, save task category, save item changed(changed text, checked and etc),
* save current task to storage, save item twice(should update prev item if it needs)
* get:
* get title, get items in list, get task is pinned, get task category, get current time, get was exception or not,
* delete:
* delete task list item
* */

class FragmentCreateTaskListViewModelTest {

    private lateinit var fakeInsertingTaskRepository: FakeInsertingTaskRepository
    private lateinit var fakeChangingTaskRepository: FakeChangingTaskRepository
    private lateinit var fakeDefaultTaskCategories: FakeDefaultTaskCategories
    private lateinit var fakeDate: FakeDate
    private lateinit var testDispatchersList: TestDispatchersList
    private lateinit var fragmentCreateTaskListViewModel: FragmentCreateTaskListViewModel
    private lateinit var insertTaskListUseCase: InsertTaskListUseCase
    private lateinit var changeTaskListUseCase: ChangeTaskListUseCase
    private lateinit var taskCategory: TaskCategory

    @Before
    fun setUp() {
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
        fragmentCreateTaskListViewModel = FragmentCreateTaskListViewModel(
            dispatchersList = testDispatchersList,
            date = fakeDate,
            insertTaskListUseCase = insertTaskListUseCase,
            changeTaskListUseCase = changeTaskListUseCase,
            defaultTaskCategories = fakeDefaultTaskCategories
        )
    }

    @Test
    fun `test, save task title`() {
        val taskTitle = "some title 123"
        assertEquals(true, fragmentCreateTaskListViewModel.getCurrentTaskTitle().value.isEmpty())
        fragmentCreateTaskListViewModel.saveCurrentTaskTitle(title = taskTitle)
        assertEquals(false, fragmentCreateTaskListViewModel.getCurrentTaskTitle().value.isEmpty())
    }

    @Test
    fun `test, save task pin`() {
        assertEquals(false, fragmentCreateTaskListViewModel.getIsCurrentTaskPin().value)
        fragmentCreateTaskListViewModel.pinCurrentTask()
        assertEquals(true, fragmentCreateTaskListViewModel.getIsCurrentTaskPin().value)
    }

    @Test
    fun `test, save task pin twice, should pin is be false`() {
        assertEquals(false, fragmentCreateTaskListViewModel.getIsCurrentTaskPin().value)
        fragmentCreateTaskListViewModel.pinCurrentTask()
        assertEquals(true, fragmentCreateTaskListViewModel.getIsCurrentTaskPin().value)
        fragmentCreateTaskListViewModel.pinCurrentTask()
        assertEquals(false, fragmentCreateTaskListViewModel.getIsCurrentTaskPin().value)
    }

    @Test
    fun `test, save task category`() {
        assertEquals(true, fragmentCreateTaskListViewModel.getCurrentTaskCategory().value == null)
        fragmentCreateTaskListViewModel.saveCurrentTaskCategory(taskCategory = taskCategory)
        assertEquals(false, fragmentCreateTaskListViewModel.getCurrentTaskCategory().value == null)
        assertEquals(true, fragmentCreateTaskListViewModel.getCurrentTaskCategory().value == taskCategory)
    }

    @Test
    fun `test, load default task category`() {
        assertEquals(true, fragmentCreateTaskListViewModel.getCurrentTaskCategory()?.value == null)
        fragmentCreateTaskListViewModel.loadDefaultCurrentTaskCategory()
        assertEquals(true, fragmentCreateTaskListViewModel.getCurrentTaskCategory()?.value == fakeDefaultTaskCategories.taskCategoryToReturn)
    }

    @Test
    fun test_loadDefaultTaskCategoryWhenCategoryExists() {
        assertEquals(true, fragmentCreateTaskListViewModel.getCurrentTaskCategory().value == null)
        assertEquals(false, fragmentCreateTaskListViewModel.getCurrentTaskCategory().value == fakeDefaultTaskCategories.taskCategoryToReturn)
        assertEquals(true, fakeDefaultTaskCategories.countOfRequestToGetDefaultTaskCategory == 0)
        fragmentCreateTaskListViewModel.loadDefaultCurrentTaskCategory()
        assertEquals(true, fakeDefaultTaskCategories.countOfRequestToGetDefaultTaskCategory == 1)
        assertEquals(false, fragmentCreateTaskListViewModel.getCurrentTaskCategory().value == null)
        assertEquals(true, fragmentCreateTaskListViewModel.getCurrentTaskCategory().value == fakeDefaultTaskCategories.taskCategoryToReturn)
        fragmentCreateTaskListViewModel.loadDefaultCurrentTaskCategory()
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
        assertEquals(true, fragmentCreateTaskListViewModel.getCurrentTaskListItems().isEmpty())
        fragmentCreateTaskListViewModel.saveCurrentTask(taskListItems = taskListItems)
        assertEquals(false, fragmentCreateTaskListViewModel.getCurrentTaskListItems().isEmpty())
        assertEquals(true, fragmentCreateTaskListViewModel.getCurrentTaskListItems() == taskListItems)
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
        assertEquals(true, fragmentCreateTaskListViewModel.getCurrentTaskListItems().isEmpty())
        fragmentCreateTaskListViewModel.saveCurrentTask(taskListItems = taskListItems)
        assertEquals(true, fragmentCreateTaskListViewModel.getCurrentTaskListItems() == taskListItems)
        assertEquals(false, fragmentCreateTaskListViewModel.getCurrentTaskListItems().isEmpty())
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
        assertEquals(true, fragmentCreateTaskListViewModel.getCurrentTaskListItems().isEmpty())
        fragmentCreateTaskListViewModel.saveCurrentTask(taskListItems = taskListItems)
        assertEquals(true, fragmentCreateTaskListViewModel.getCurrentTaskListItems() == taskListItems)
        assertEquals(false, fragmentCreateTaskListViewModel.getCurrentTaskListItems().isEmpty())

        taskItem.isChecked = true
        taskItem.title = "some new title"

        fragmentCreateTaskListViewModel.saveCurrentTask(taskListItems = taskListItems)
        assertEquals(true, fragmentCreateTaskListViewModel.getCurrentTaskListItems() == taskListItems)
        assertEquals(false, fragmentCreateTaskListViewModel.getCurrentTaskListItems().isEmpty())

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
        assertEquals(true, fragmentCreateTaskListViewModel.getCurrentTaskListItems().isEmpty())
        fragmentCreateTaskListViewModel.saveCurrentTask(taskListItems = taskListItems)
        assertEquals(true, fragmentCreateTaskListViewModel.getCurrentTaskListItems() == taskListItems)
        assertEquals(false, fragmentCreateTaskListViewModel.getCurrentTaskListItems().isEmpty())
        fragmentCreateTaskListViewModel.saveCurrentTask(taskListItems = taskListItems)
        assertEquals(true, fragmentCreateTaskListViewModel.getCurrentTaskListItems() == taskListItems)
        assertEquals(false, fragmentCreateTaskListViewModel.getCurrentTaskListItems().isEmpty())
    }

    @Test
    fun `test, get task title`() {
        val taskTitle = "some title 123"
        assertEquals(true, fragmentCreateTaskListViewModel.getCurrentTaskTitle().value.isEmpty())
        fragmentCreateTaskListViewModel.saveCurrentTaskTitle(title = taskTitle)
        assertEquals(false, fragmentCreateTaskListViewModel.getCurrentTaskTitle().value.isEmpty())
        assertEquals(true, fragmentCreateTaskListViewModel.getCurrentTaskTitle().value == taskTitle)
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
        assertEquals(true, fragmentCreateTaskListViewModel.getCurrentTaskListItems().isEmpty())
        fragmentCreateTaskListViewModel.saveCurrentTask(taskListItems = taskListItems)
        assertEquals(true, fragmentCreateTaskListViewModel.getCurrentTaskListItems() == taskListItems)
    }

    @Test
    fun `test, get is task pinned`() {
        assertEquals(false, fragmentCreateTaskListViewModel.getIsCurrentTaskPin().value)
        fragmentCreateTaskListViewModel.pinCurrentTask()
        assertEquals(true, fragmentCreateTaskListViewModel.getIsCurrentTaskPin().value)
    }

    @Test
    fun `test, get task category`() {
        assertEquals(true, fragmentCreateTaskListViewModel.getCurrentTaskCategory()?.value == null)
        fragmentCreateTaskListViewModel.saveCurrentTaskCategory(taskCategory = taskCategory)
        assertEquals(false, fragmentCreateTaskListViewModel.getCurrentTaskCategory()?.value == null)
        assertEquals(true, fragmentCreateTaskListViewModel.getCurrentTaskCategory()?.value == taskCategory)
    }

    @Test
    fun `test, get current time`() {
        assertEquals(true, fragmentCreateTaskListViewModel.getCurrentTime() == fakeDate.getCurrentTime())
    }

    @Test
    fun `test, get is was exception, should return null`() {
        assertEquals(true, fragmentCreateTaskListViewModel.isWasException()?.value == null)
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
        assertEquals(true, fragmentCreateTaskListViewModel.isWasException()?.value == null)
        fragmentCreateTaskListViewModel.saveCurrentTask(taskListItems = taskListItems)
        assertEquals(false, fragmentCreateTaskListViewModel.isWasException()?.value == null)
    }

    @Test
    fun `test, save current task when title and list is empty`() {
        assertEquals(true, fakeInsertingTaskRepository.countOfRequest == 0)
        fragmentCreateTaskListViewModel.saveCurrentTask(taskListItems = emptyList<Task.TaskListItem>())
        assertEquals(true, fakeInsertingTaskRepository.countOfRequest == 0)
    }

    @Test
    fun `test, task saved after insert item, shoul be null`() {
        val taskItem = Task.TaskListItem(
            title = "some title",
            isChecked = false
        )
        val taskListItems = listOf(
            taskItem
        )
        assertEquals(true, fragmentCreateTaskListViewModel.getCurrentTaskListItems().isEmpty())
        assertEquals(true, fragmentCreateTaskListViewModel.getTaskSaved().value == null)
        fragmentCreateTaskListViewModel.saveCurrentTask(taskListItems = taskListItems)
        assertEquals(true, fragmentCreateTaskListViewModel.getCurrentTaskListItems() == taskListItems)
        assertEquals(true, fragmentCreateTaskListViewModel.getTaskSaved().value == null)
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

    override fun getDefaultTaskCategories(): List<TaskCategory> {
        throw IllegalStateException("don't use this method")
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