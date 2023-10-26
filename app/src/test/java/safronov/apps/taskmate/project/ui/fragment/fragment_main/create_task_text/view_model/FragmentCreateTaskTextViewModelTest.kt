package safronov.apps.taskmate.project.ui.fragment.fragment_main.create_task_text.view_model

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
import safronov.apps.domain.use_case.task.create.InsertTaskTextUseCase
import safronov.apps.domain.use_case.task.update.ChangeTaskTextUseCase
import safronov.apps.taskmate.R
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList
import safronov.apps.taskmate.project.system_settings.data.DefaultTaskCategories
import safronov.apps.taskmate.project.system_settings.date.Date
import java.lang.IllegalStateException

class FragmentCreateTaskTextViewModelTest {

    private lateinit var dataToSave: Task.TaskText
    private lateinit var taskCategory: TaskCategory
    private lateinit var fakeInsertingTaskRepository: FakeInsertingTaskRepository
    private lateinit var insertTaskTextUseCase: InsertTaskTextUseCase
    private lateinit var testDispatchersList: TestDispatchersList
    private lateinit var fragmentCreateTaskTextViewModel: FragmentCreateTaskTextViewModel
    private lateinit var fakeDate: Date
    private lateinit var fakeChangingTaskRepository: FakeChangingTaskRepository
    private lateinit var fakeDefaultTaskCategories: FakeDefaultTaskCategories
    private val currentTime = "today"

    @Before
    fun setup() {
        fakeDate = FakeDate()
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
        fragmentCreateTaskTextViewModel = FragmentCreateTaskTextViewModel(
            dispatchersList = testDispatchersList,
            date = fakeDate,
            insertTaskTextUseCase = insertTaskTextUseCase,
            changeTaskTextUseCase = ChangeTaskTextUseCase(changingTaskRepository = fakeChangingTaskRepository)
        )
    }

    @Test
    fun `test, load default task category`() {
        assertEquals(true, fragmentCreateTaskTextViewModel.getTaskCategory().value == null)
        assertEquals(false, fragmentCreateTaskTextViewModel.getTaskCategory().value == fakeDefaultTaskCategories.taskCategoryToReturn)
        fragmentCreateTaskTextViewModel.laodDefaultTaskCategory()
        assertEquals(false, fragmentCreateTaskTextViewModel.getTaskCategory().value == null)
        assertEquals(true, fragmentCreateTaskTextViewModel.getTaskCategory().value == fakeDefaultTaskCategories.taskCategoryToReturn)
    }

    @Test
    fun `test, save task category`() {
        assertEquals(true, fragmentCreateTaskTextViewModel.getTaskCategory().value != taskCategory)
        fragmentCreateTaskTextViewModel.saveTaskCategory(
            taskCategory = taskCategory
        )
        assertEquals(true, fragmentCreateTaskTextViewModel.getTaskCategory().value == taskCategory)
    }

    @Test
    fun `test, save task pin`() {
        assertEquals(true, fragmentCreateTaskTextViewModel.getIsTaskPin().value != true)
        fragmentCreateTaskTextViewModel.pinCurrentTask()
        assertEquals(true, fragmentCreateTaskTextViewModel.getIsTaskPin().value == true)
    }

    @Test
    fun `test, save title`() {
        assertEquals(true, fragmentCreateTaskTextViewModel.getCurrentTaskTitle().value.isEmpty())
        fragmentCreateTaskTextViewModel.saveCurrentTaskTitle(title = "some title")
        assertEquals(false, fragmentCreateTaskTextViewModel.getCurrentTaskTitle().value.isEmpty())
    }

    @Test
    fun `test, save text`() {
        assertEquals(true, fragmentCreateTaskTextViewModel.getCurrentTaskText().value.isEmpty())
        fragmentCreateTaskTextViewModel.saveCurrentTaskText(text = "some title")
        assertEquals(false, fragmentCreateTaskTextViewModel.getCurrentTaskText().value.isEmpty())
    }

    @Test
    fun `test, show task category `() {
        assertEquals(true, fragmentCreateTaskTextViewModel.getTaskCategory().value != taskCategory)
        fragmentCreateTaskTextViewModel.saveTaskCategory(
            taskCategory = taskCategory
        )
        assertEquals(true, fragmentCreateTaskTextViewModel.getTaskCategory().value == taskCategory)
    }

    @Test
    fun `test, show task pin`() {
        assertEquals(true, fragmentCreateTaskTextViewModel.getIsTaskPin().value != true)
        fragmentCreateTaskTextViewModel.pinCurrentTask()
        assertEquals(true, fragmentCreateTaskTextViewModel.getIsTaskPin().value == true)
    }

    @Test
    fun `test, save task, should save task`() {

        val titleToSave = "title"
        val textToSave = "some text"

        assertEquals(false, fakeInsertingTaskRepository.dataToReturn.title == titleToSave)
        assertEquals(false, fakeInsertingTaskRepository.dataToReturn.text == textToSave)
        assertEquals(false, fakeInsertingTaskRepository.dataToReturn.taskCategoryId == taskCategory.id)
        assertEquals(false, fakeInsertingTaskRepository.dataToReturn.isPinned == true)

        fragmentCreateTaskTextViewModel.saveCurrentTaskTitle(title = titleToSave)
        fragmentCreateTaskTextViewModel.saveCurrentTaskText(text = textToSave)
        fragmentCreateTaskTextViewModel.saveTaskCategory(taskCategory = taskCategory)
        fragmentCreateTaskTextViewModel.pinCurrentTask()
        fragmentCreateTaskTextViewModel.saveCurrentTask()

        assertEquals(true, fakeInsertingTaskRepository.dataToReturn.title == titleToSave)
        assertEquals(true, fakeInsertingTaskRepository.dataToReturn.text == textToSave)
        assertEquals(true, fakeInsertingTaskRepository.dataToReturn.taskCategoryId == taskCategory.id)
        assertEquals(true, fakeInsertingTaskRepository.dataToReturn.isPinned == true)
    }

    @Test
    fun `test, save task, should don't save task 'cause title and text is empty`() {
        assertEquals(true, fakeInsertingTaskRepository.dataToReturn != dataToSave)
        fragmentCreateTaskTextViewModel.saveCurrentTask()
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

        fragmentCreateTaskTextViewModel.saveCurrentTaskTitle(title = titleToSave)
        fragmentCreateTaskTextViewModel.saveCurrentTaskText(text = textToSave)
        fragmentCreateTaskTextViewModel.saveTaskCategory(taskCategory = taskCategory)
        fragmentCreateTaskTextViewModel.pinCurrentTask()
        fragmentCreateTaskTextViewModel.saveCurrentTask()

        fragmentCreateTaskTextViewModel.saveCurrentTask()

        assertEquals(true, fakeInsertingTaskRepository.dataToReturn.title == titleToSave)
        assertEquals(true, fakeInsertingTaskRepository.dataToReturn.text == textToSave)
        assertEquals(true, fakeInsertingTaskRepository.dataToReturn.taskCategoryId == taskCategory.id)
        assertEquals(true, fakeInsertingTaskRepository.dataToReturn.isPinned == true)

        val newText = "some new text"
        fragmentCreateTaskTextViewModel.saveCurrentTaskText(text = newText)
        fragmentCreateTaskTextViewModel.saveCurrentTask()
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

        fragmentCreateTaskTextViewModel.saveCurrentTaskTitle(title = titleToSave)
        fragmentCreateTaskTextViewModel.saveCurrentTaskText(text = textToSave)
        fragmentCreateTaskTextViewModel.saveTaskCategory(taskCategory = taskCategory)
        fragmentCreateTaskTextViewModel.pinCurrentTask()
        fragmentCreateTaskTextViewModel.saveCurrentTask()

        fragmentCreateTaskTextViewModel.saveCurrentTask()

        assertEquals(true, fakeInsertingTaskRepository.dataToReturn.title == titleToSave)
        assertEquals(true, fakeInsertingTaskRepository.dataToReturn.text == textToSave)
        assertEquals(true, fakeInsertingTaskRepository.dataToReturn.taskCategoryId == taskCategory.id)
        assertEquals(true, fakeInsertingTaskRepository.dataToReturn.isPinned == true)

        fragmentCreateTaskTextViewModel.saveCurrentTask()

        assertEquals(true, fakeInsertingTaskRepository.dataToReturn.title == titleToSave)
        assertEquals(true, fakeInsertingTaskRepository.dataToReturn.text == textToSave)
        assertEquals(true, fakeInsertingTaskRepository.dataToReturn.taskCategoryId == taskCategory.id)
        assertEquals(true, fakeInsertingTaskRepository.dataToReturn.isPinned == true)
    }

    @Test
    fun `test, save task, should show that was exception`() {
        fakeInsertingTaskRepository.isNeedToThrowException = true

        assertEquals(true, fakeInsertingTaskRepository.dataToReturn != dataToSave)
        assertEquals(true, fragmentCreateTaskTextViewModel.wasException().value == null)

        fragmentCreateTaskTextViewModel.saveCurrentTaskTitle(title = dataToSave.title)
        fragmentCreateTaskTextViewModel.saveCurrentTaskText(text = dataToSave.text)
        fragmentCreateTaskTextViewModel.saveTaskCategory(taskCategory = taskCategory)
        fragmentCreateTaskTextViewModel.pinCurrentTask()

        fragmentCreateTaskTextViewModel.saveCurrentTask()
        assertEquals(true, fragmentCreateTaskTextViewModel.wasException().value != null)
        assertEquals(true, fragmentCreateTaskTextViewModel.wasException().value?.message == "some exception")
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

private class FakeInsertingTaskRepository: TaskRepository.InsertingTask {

    var isNeedToThrowException = false
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