package safronov.apps.taskmate.project.ui.fragment.fragment_main.task_text_details.view_model

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
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
import safronov.apps.domain.use_case.task.create.InsertTaskTextUseCase
import safronov.apps.domain.use_case.task.update.ChangeTaskTextUseCase
import safronov.apps.domain.use_case.task_category.read.GetTaskCategoriesUseCase
import safronov.apps.domain.use_case.task_category.read.GetTaskCategoryByIdUseCase
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList
import safronov.apps.taskmate.project.system_settings.data.DefaultTaskCategories
import safronov.apps.taskmate.project.system_settings.date.Date
import java.lang.IllegalStateException

class FragmentTaskTextDetailsViewModelTest {

    private lateinit var dataToSave: Task.TaskText
    private lateinit var taskCategory: TaskCategory
    private lateinit var fakeInsertingTaskRepository: FakeInsertingTaskRepository
    private lateinit var insertTaskTextUseCase: InsertTaskTextUseCase
    private lateinit var testDispatchersList: TestDispatchersList
    private lateinit var fragmentTaskTextDetailsViewModel: FragmentTaskTextDetailsViewModel
    private lateinit var fakeDate: Date
    private lateinit var fakeChangingTaskRepository: FakeChangingTaskRepository
    private lateinit var fakeDefaultTaskCategories: FakeDefaultTaskCategories
    private lateinit var fakeTaskCategoryRepository: FakeTaskCategoryRepository
    private val currentTime = "today"

    @Before
    fun setup() {
        fakeDate = FakeDate()
        fakeTaskCategoryRepository = FakeTaskCategoryRepository()
        fakeDefaultTaskCategories = FakeDefaultTaskCategories()
        taskCategory = TaskCategory(
            id = 55,
            icon = 325232,
            backgroundColor = 143463,
            categoryName = "some asda",
            categoryType = CategoryTypes.System
        )
        dataToSave = Task.TaskText(
            title = "some title",
            text = "some text",
            date = currentTime,
            taskCategoryId = 5,
            taskType = Task.TaskType.Text,
            isPinned = false,
            id = null
        )
        fakeChangingTaskRepository = FakeChangingTaskRepository()
        fakeInsertingTaskRepository = FakeInsertingTaskRepository()
        insertTaskTextUseCase = InsertTaskTextUseCase(insertingTaskRepository = fakeInsertingTaskRepository)
        testDispatchersList = TestDispatchersList()
        fragmentTaskTextDetailsViewModel = FragmentTaskTextDetailsViewModel(
            dispatchersList = testDispatchersList,
            date = fakeDate,
            insertTaskTextUseCase = insertTaskTextUseCase,
            changeTaskTextUseCase = ChangeTaskTextUseCase(changingTaskRepository = fakeChangingTaskRepository),
            defaultTaskCategories = fakeDefaultTaskCategories,
            getTaskCategoryByIdUseCase = GetTaskCategoryByIdUseCase(fakeTaskCategoryRepository),
            getTasksCategoriesUseCase = GetTaskCategoriesUseCase(taskCategoryRepository = fakeTaskCategoryRepository)
        )
    }

    @Test
    fun test_prepareToChangeExistingTask() {
        val defaultValue = Task.TaskText(
            title = "asdf",
            text = "asdf",
            date = "fasdf",
            taskCategoryId = fakeTaskCategoryRepository.dataToReturn.id,
            taskType = Task.TaskType.Text,
            isPinned = false,
            id = 235423
        )
        fragmentTaskTextDetailsViewModel.prepareToChangeExistingTask(defaultValue)
        assertEquals(true, defaultValue.title == fragmentTaskTextDetailsViewModel.getCurrentTaskTitle().value)
        assertEquals(true, defaultValue.text == fragmentTaskTextDetailsViewModel.getCurrentTaskText().value)
        assertEquals(true, defaultValue.taskCategoryId == fragmentTaskTextDetailsViewModel.getTaskCategory().value?.id)
        assertEquals(true, defaultValue.isPinned == fragmentTaskTextDetailsViewModel.getIsTaskPin().value)
    }

    @Test
    fun test_prepareToChangeExistingTask_expectedException() {
        fakeTaskCategoryRepository.isNeedToThrowException = true
        assertEquals(true, fragmentTaskTextDetailsViewModel.isWasException().value == null)
        val defaultValue = Task.TaskText(
            title = "asdf",
            text = "asdf",
            date = "fasdf",
            taskCategoryId = fakeTaskCategoryRepository.dataToReturn.id,
            taskType = Task.TaskType.Text,
            isPinned = false,
            id = 235423
        )
        fragmentTaskTextDetailsViewModel.prepareToChangeExistingTask(defaultValue)
        assertEquals(false, fragmentTaskTextDetailsViewModel.isWasException().value == null)
    }

    @Test
    fun `test, load default task category`() {
        assertEquals(true, fragmentTaskTextDetailsViewModel.getTaskCategory().value == null)
        assertEquals(false, fragmentTaskTextDetailsViewModel.getTaskCategory().value == fakeDefaultTaskCategories.taskCategoryToReturn)
        assertEquals(true, fakeDefaultTaskCategories.countOfRequestToGetDefaultTaskCategory == 0)
        fragmentTaskTextDetailsViewModel.loadDefaultTaskCategory()
        assertEquals(true, fakeDefaultTaskCategories.countOfRequestToGetDefaultTaskCategory == 1)
        assertEquals(false, fragmentTaskTextDetailsViewModel.getTaskCategory().value == null)
        assertEquals(true, fragmentTaskTextDetailsViewModel.getTaskCategory().value == fakeDefaultTaskCategories.taskCategoryToReturn)
    }

    @Test
    fun test_loadDefaultTaskCategoryWhenCategoryExists() {
        assertEquals(true, fragmentTaskTextDetailsViewModel.getTaskCategory().value == null)
        assertEquals(false, fragmentTaskTextDetailsViewModel.getTaskCategory().value == fakeDefaultTaskCategories.taskCategoryToReturn)
        assertEquals(true, fakeDefaultTaskCategories.countOfRequestToGetDefaultTaskCategory == 0)
        fragmentTaskTextDetailsViewModel.loadDefaultTaskCategory()
        assertEquals(true, fakeDefaultTaskCategories.countOfRequestToGetDefaultTaskCategory == 1)
        assertEquals(false, fragmentTaskTextDetailsViewModel.getTaskCategory().value == null)
        assertEquals(true, fragmentTaskTextDetailsViewModel.getTaskCategory().value == fakeDefaultTaskCategories.taskCategoryToReturn)
        fragmentTaskTextDetailsViewModel.loadDefaultTaskCategory()
        assertEquals(true, fakeDefaultTaskCategories.countOfRequestToGetDefaultTaskCategory == 1)
    }

    @Test
    fun `test, save task category`() {
        assertEquals(true, fragmentTaskTextDetailsViewModel.getTaskCategory().value != taskCategory)
        fragmentTaskTextDetailsViewModel.saveTaskCategory(
            taskCategory = taskCategory
        )
        assertEquals(true, fragmentTaskTextDetailsViewModel.getTaskCategory().value == taskCategory)
    }

    @Test
    fun `test, save task pin`() {
        assertEquals(true, fragmentTaskTextDetailsViewModel.getIsTaskPin().value != true)
        fragmentTaskTextDetailsViewModel.pinCurrentTask()
        assertEquals(true, fragmentTaskTextDetailsViewModel.getIsTaskPin().value == true)
    }

    @Test
    fun `test, save title`() {
        assertEquals(true, fragmentTaskTextDetailsViewModel.getCurrentTaskTitle().value.isEmpty())
        fragmentTaskTextDetailsViewModel.saveCurrentTaskTitle(title = "some title")
        assertEquals(false, fragmentTaskTextDetailsViewModel.getCurrentTaskTitle().value.isEmpty())
    }

    @Test
    fun `test, save text`() {
        assertEquals(true, fragmentTaskTextDetailsViewModel.getCurrentTaskText().value.isEmpty())
        fragmentTaskTextDetailsViewModel.saveCurrentTaskText(text = "some title")
        assertEquals(false, fragmentTaskTextDetailsViewModel.getCurrentTaskText().value.isEmpty())
    }

    @Test
    fun test_loadTaskCategories() = runBlocking {
        fragmentTaskTextDetailsViewModel.loadTaskCategories()
        assertEquals(true, fakeTaskCategoryRepository.list == fragmentTaskTextDetailsViewModel.getCategories().first())
    }

    @Test
    fun `test, show task category `() {
        assertEquals(true, fragmentTaskTextDetailsViewModel.getTaskCategory().value != taskCategory)
        fragmentTaskTextDetailsViewModel.saveTaskCategory(
            taskCategory = taskCategory
        )
        assertEquals(true, fragmentTaskTextDetailsViewModel.getTaskCategory().value == taskCategory)
    }

    @Test
    fun `test, show task pin`() {
        assertEquals(true, fragmentTaskTextDetailsViewModel.getIsTaskPin().value != true)
        fragmentTaskTextDetailsViewModel.pinCurrentTask()
        assertEquals(true, fragmentTaskTextDetailsViewModel.getIsTaskPin().value == true)
    }

    @Test
    fun `test, save task, should save task`() {

        val titleToSave = "title"
        val textToSave = "some text"

        assertEquals(false, fakeInsertingTaskRepository.dataToReturn.title == titleToSave)
        assertEquals(false, fakeInsertingTaskRepository.dataToReturn.text == textToSave)
        assertEquals(false, fakeInsertingTaskRepository.dataToReturn.taskCategoryId == taskCategory.id)
        assertEquals(false, fakeInsertingTaskRepository.dataToReturn.isPinned == true)

        fragmentTaskTextDetailsViewModel.saveCurrentTaskTitle(title = titleToSave)
        fragmentTaskTextDetailsViewModel.saveCurrentTaskText(text = textToSave)
        fragmentTaskTextDetailsViewModel.saveTaskCategory(taskCategory = taskCategory)
        fragmentTaskTextDetailsViewModel.pinCurrentTask()
        fragmentTaskTextDetailsViewModel.saveCurrentTask()

        assertEquals(true, fakeInsertingTaskRepository.dataToReturn.title == titleToSave)
        assertEquals(true, fakeInsertingTaskRepository.dataToReturn.text == textToSave)
        assertEquals(true, fakeInsertingTaskRepository.dataToReturn.taskCategoryId == taskCategory.id)
        assertEquals(true, fakeInsertingTaskRepository.dataToReturn.isPinned == true)
    }

    @Test
    fun `test, save task, should don't save task 'cause title and text is empty`() {
        assertEquals(true, fakeInsertingTaskRepository.dataToReturn != dataToSave)
        fragmentTaskTextDetailsViewModel.saveCurrentTask()
        assertEquals(true, fakeInsertingTaskRepository.dataToReturn != dataToSave)
    }

    @Test
    fun `test, save task twice, should update prev task`() {

        val titleToSave = "title"
        val textToSave = "some text"

        assertEquals(false, fakeInsertingTaskRepository.dataToReturn.title == titleToSave)
        assertEquals(false, fakeInsertingTaskRepository.dataToReturn.text == textToSave)
        assertEquals(false, fakeInsertingTaskRepository.dataToReturn.taskCategoryId == taskCategory.id)
        assertEquals(false, fakeInsertingTaskRepository.dataToReturn.isPinned == true)

        fragmentTaskTextDetailsViewModel.saveCurrentTaskTitle(title = titleToSave)
        fragmentTaskTextDetailsViewModel.saveCurrentTaskText(text = textToSave)
        fragmentTaskTextDetailsViewModel.saveTaskCategory(taskCategory = taskCategory)
        fragmentTaskTextDetailsViewModel.pinCurrentTask()
        fragmentTaskTextDetailsViewModel.saveCurrentTask()

        fragmentTaskTextDetailsViewModel.saveCurrentTask()

        assertEquals(true, fakeInsertingTaskRepository.dataToReturn.title == titleToSave)
        assertEquals(true, fakeInsertingTaskRepository.dataToReturn.text == textToSave)
        assertEquals(true, fakeInsertingTaskRepository.dataToReturn.taskCategoryId == taskCategory.id)
        assertEquals(true, fakeInsertingTaskRepository.dataToReturn.isPinned == true)

        val newText = "some new text"
        fragmentTaskTextDetailsViewModel.saveCurrentTaskText(text = newText)
        fragmentTaskTextDetailsViewModel.saveCurrentTask()
        assertEquals(true, fakeChangingTaskRepository.dataToReturn.text == newText)
    }

    @Test
    fun `test, save task and request save task when nothing has been changed`() {
        val titleToSave = "title"
        val textToSave = "some text"

        assertEquals(false, fakeInsertingTaskRepository.dataToReturn.title == titleToSave)
        assertEquals(false, fakeInsertingTaskRepository.dataToReturn.text == textToSave)
        assertEquals(false, fakeInsertingTaskRepository.dataToReturn.taskCategoryId == taskCategory.id)
        assertEquals(false, fakeInsertingTaskRepository.dataToReturn.isPinned == true)

        fragmentTaskTextDetailsViewModel.saveCurrentTaskTitle(title = titleToSave)
        fragmentTaskTextDetailsViewModel.saveCurrentTaskText(text = textToSave)
        fragmentTaskTextDetailsViewModel.saveTaskCategory(taskCategory = taskCategory)
        fragmentTaskTextDetailsViewModel.pinCurrentTask()
        fragmentTaskTextDetailsViewModel.saveCurrentTask()

        fragmentTaskTextDetailsViewModel.saveCurrentTask()

        assertEquals(true, fakeInsertingTaskRepository.dataToReturn.title == titleToSave)
        assertEquals(true, fakeInsertingTaskRepository.dataToReturn.text == textToSave)
        assertEquals(true, fakeInsertingTaskRepository.dataToReturn.taskCategoryId == taskCategory.id)
        assertEquals(true, fakeInsertingTaskRepository.dataToReturn.isPinned == true)

        fragmentTaskTextDetailsViewModel.saveCurrentTask()

        assertEquals(true, fakeInsertingTaskRepository.dataToReturn.title == titleToSave)
        assertEquals(true, fakeInsertingTaskRepository.dataToReturn.text == textToSave)
        assertEquals(true, fakeInsertingTaskRepository.dataToReturn.taskCategoryId == taskCategory.id)
        assertEquals(true, fakeInsertingTaskRepository.dataToReturn.isPinned == true)
    }

    @Test
    fun `test, save task, should show that was exception`() {
        fakeInsertingTaskRepository.isNeedToThrowException = true

        assertEquals(true, fakeInsertingTaskRepository.dataToReturn != dataToSave)
        assertEquals(true, fragmentTaskTextDetailsViewModel.isWasException().value == null)

        fragmentTaskTextDetailsViewModel.saveCurrentTaskTitle(title = dataToSave.title)
        fragmentTaskTextDetailsViewModel.saveCurrentTaskText(text = dataToSave.text)
        fragmentTaskTextDetailsViewModel.saveTaskCategory(taskCategory = taskCategory)
        fragmentTaskTextDetailsViewModel.pinCurrentTask()

        fragmentTaskTextDetailsViewModel.saveCurrentTask()
        assertEquals(true, fragmentTaskTextDetailsViewModel.isWasException().value != null)
        assertEquals(true, fragmentTaskTextDetailsViewModel.isWasException().value?.message == "some exception")
    }

    @Test
    fun `test, save task when nothing has been changed`() {
        assertEquals(true, fakeInsertingTaskRepository.countOfRequest == 0)
        fragmentTaskTextDetailsViewModel.saveCurrentTask()
        assertEquals(true, fakeInsertingTaskRepository.countOfRequest == 0)
    }

    @Test
    fun `test, get current time`() {
        assertEquals(true, fakeDate.getCurrentTime() == fragmentTaskTextDetailsViewModel.getCurrentTime())
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
        ),
        TaskCategory(
            icon = 632443,
            backgroundColor = 434343,
            categoryName = "some namew",
            categoryType = CategoryTypes.User
        )
    )

    override fun getDefaultTaskCategory(): TaskCategory {
        countOfRequestToGetDefaultTaskCategory++
        return taskCategoryToReturn
    }

    override fun getDefaultTaskCategories(): List<TaskCategory> {
        return emptyList()
    }

}

private class FakeDate: Date {
    override fun getCurrentTime(): String {
        return "today"
    }
}

private class FakeTaskCategoryRepository: TaskCategoryRepository {

    var isNeedToThrowException = false
    val list = listOf(
        TaskCategory(
            id = 252,
            icon = 43543,
            backgroundColor = 324534,
            categoryName = "asdfa",
            categoryType = CategoryTypes.User
        ),
        TaskCategory(
            id = 32523,
            icon = 435235243,
            backgroundColor = 253,
            categoryName = "asdfa",
            categoryType = CategoryTypes.User
        )
    )
    val dataToReturn = TaskCategory(
        id = 252,
        icon = 43543,
        backgroundColor = 324534,
        categoryName = "asdfa",
        categoryType = CategoryTypes.User
    )

    override suspend fun updateTaskCategories(categories: List<TaskCategory>) {
        throw IllegalStateException("don't use this method")
    }

    override suspend fun insertTaskCategories(list: List<TaskCategory>) {
        throw IllegalStateException("don't use this method")
    }

    override suspend fun getTaskCategories(): Flow<List<TaskCategory>> {
        return flow {
            emit(list)
        }
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

private class FakeInsertingTaskRepository: TaskRepository.InsertingTask {

    var isNeedToThrowException = false
    var countOfRequest = 0
    var dataToReturn = Task.TaskText(
        title = "som23e title23",
        text = "some text34",
        date = "today5",
        taskCategoryId = 45,
        taskType = Task.TaskType.Text,
        isPinned = false,
        id = null
    )

    override suspend fun insertTaskText(task: Task.TaskText): Long? {
        if (isNeedToThrowException) throw DomainException("some exception")
        dataToReturn = task
        countOfRequest++
        return dataToReturn.id
    }

    override suspend fun insertTaskList(task: Task.TaskList): Long? {
        throw IllegalStateException("don't use this method")
    }
}

private class FakeChangingTaskRepository: TaskRepository.ChangingTask {

    var isNeedToThrowException = false
    var dataToReturn = Task.TaskText(
        title = "some titlef",
        text = "some textfas",
        date = "todayfas",
        taskCategoryId = 51,
        taskType = Task.TaskType.Text,
        isPinned = false,
        id = 23
    )

    override suspend fun changeTaskText(task: Task.TaskText) {
        if (isNeedToThrowException) throw DomainException("some exception")
        dataToReturn = task
    }

    override suspend fun changeTaskList(task: Task.TaskList) {
        throw IllegalStateException("don't use this method -_- ")
    }

    override suspend fun changeTasks(tasks: List<Task>) {
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