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

    @Before
    fun setUp() {
        fakeInsertingTaskRepository = FakeInsertingTaskRepository()
        fakeChangingTaskRepository = FakeChangingTaskRepository()
        fakeDefaultTaskCategories = FakeDefaultTaskCategories()
        fakeDate = FakeDate()
        testDispatchersList = TestDispatchersList()
        insertTaskListUseCase = InsertTaskListUseCase(insertingTask = fakeInsertingTaskRepository)
        changeTaskListUseCase = ChangeTaskListUseCase(changingTaskRepository = fakeChangingTaskRepository)
        fragmentCreateTaskListViewModel = FragmentCreateTaskListViewModel(
            dispatchersList = testDispatchersList,
            date = fakeDate,
            insertTaskListUseCase = insertTaskListUseCase,
            changeTaskListUseCase = changeTaskListUseCase
        )
    }

    @Test
    fun `test, save task title`() {

    }

    @Test
    fun `test, add task list item`() {

    }

    @Test
    fun `test, save task pin`() {

    }

    @Test
    fun `test, save task category`() {

    }

    @Test
    fun `test, load default task category`() {

    }

    @Test
    fun `test, change task list item`() {

    }

    @Test
    fun `test, save current task`() {

    }

    @Test
    fun `test, save current task twice, should update prev item`() {

    }

    @Test
    fun `test, save current task and save again current task when nothing has been changed`() {

    }

    @Test
    fun `test, get task title`() {

    }

    @Test
    fun `test, get task list items`() {

    }

    @Test
    fun `test, get is task pinned`() {

    }

    @Test
    fun `test, get task category`() {

    }

    @Test
    fun `test, get current time`() {

    }

    @Test
    fun `test, get is was exception`() {

    }

    @Test
    fun `test, delete task list item`() {

    }

}

private class FakeInsertingTaskRepository: TaskRepository.InsertingTask {

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

    override suspend fun insertTaskText(task: Task.TaskText): Long? {
        throw IllegalStateException("don't user this method -_- ")
    }

    override suspend fun insertTaskList(task: Task.TaskList): Long? {
        if (isNeedToThrowException) throw DomainException("some exception")
        dataToReturn = task
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