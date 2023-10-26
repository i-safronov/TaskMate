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
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList
import java.lang.IllegalStateException

/*
* actions:
* saving temp data(for example user choose color this choice need to save in ViewModel):
*   task category, pinned or not, title, text,
* showing temp data(for example user choose color this choice need to save in ViewModel and show in ui):
*   task category, pinned or not
* saving task after request and if data has been saved yet so update this data,
*
* so:
* save task category, save task pinned, save title, save text,
* show task category, show pinned task or not,
* save task, save task(was exception), double save task(should update prev task but now save again),
*
* */

class FragmentCreateTaskTextViewModelTest {

    private lateinit var dataToSave: Task.TaskText
    private lateinit var taskCategory: TaskCategory
    private lateinit var fakeInsertingTaskRepository: FakeInsertingTaskRepository
    private lateinit var insertTaskTextUseCase: InsertTaskTextUseCase
    private lateinit var testDispatchersList: TestDispatchersList
    private lateinit var fragmentCreateTaskTextViewModel: FragmentCreateTaskTextViewModel

    @Before
    fun setup() {
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
            date = "today",
            taskCategoryId = 5,
            taskType = Task.TaskType.Text,
            isPinned = false,
            id = 2
        )
        fakeInsertingTaskRepository = FakeInsertingTaskRepository()
        insertTaskTextUseCase = InsertTaskTextUseCase(insertingTaskRepository = fakeInsertingTaskRepository)
        testDispatchersList = TestDispatchersList()
        fragmentCreateTaskTextViewModel = FragmentCreateTaskTextViewModel(
            dispatchersList = testDispatchersList,
            insertTaskTextUseCase = insertTaskTextUseCase
        )
    }

    @Test
    fun `test, save task category`() {
        assertEquals(true, fragmentCreateTaskTextViewModel.getTaskCategory().value?.first() != taskCategory)
        fragmentCreateTaskTextViewModel.saveTaskCategory(
            taskCategory = taskCategory
        )
        assertEquals(true, fragmentCreateTaskTextViewModel.getTaskCategory().value?.first() == taskCategory)
    }

    @Test
    fun `test, save task pin`() {
        assertEquals(true, fragmentCreateTaskTextViewModel.getIsTaskPin().value?.first() != true)
        fragmentCreateTaskTextViewModel.pinCurrentTask()
        assertEquals(true, fragmentCreateTaskTextViewModel.getIsTaskPin().value?.first() == true)
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
        assertEquals(true, fragmentCreateTaskTextViewModel.getTaskCategory().value?.first() != taskCategory)
        fragmentCreateTaskTextViewModel.saveTaskCategory(
            taskCategory = taskCategory
        )
        assertEquals(true, fragmentCreateTaskTextViewModel.getTaskCategory().value?.first() == taskCategory)
    }

    @Test
    fun `test, show task pin`() {
        assertEquals(true, fragmentCreateTaskTextViewModel.getIsTaskPin().value?.first() != true)
        fragmentCreateTaskTextViewModel.pinCurrentTask()
        assertEquals(true, fragmentCreateTaskTextViewModel.getIsTaskPin().value?.first() == true)
    }

    @Test
    fun `test, save task, should save task`() {
        assertEquals(true, fakeInsertingTaskRepository.dataToReturn != dataToSave)

        fragmentCreateTaskTextViewModel.saveCurrentTaskTitle(title = dataToSave.title)
        fragmentCreateTaskTextViewModel.saveCurrentTaskText(title = dataToSave.text)
        fragmentCreateTaskTextViewModel.saveTaskCategory(taskCategory = taskCategory)
        fragmentCreateTaskTextViewModel.pinCurrentTask()

        fragmentCreateTaskTextViewModel.saveCurrentTask()

        assertEquals(true, fakeInsertingTaskRepository.dataToReturn == dataToSave)
    }

    @Test
    fun `test, save task, should don't save task 'cause title and text is empty`() {
        assertEquals(true, fakeInsertingTaskRepository.dataToReturn != dataToSave)
        fragmentCreateTaskTextViewModel.saveCurrentTask()
        assertEquals(true, fakeInsertingTaskRepository.dataToReturn != dataToSave)
    }

    @Test
    fun `test, save task twice, should update prev task`() {

        assertEquals(true, fakeInsertingTaskRepository.dataToReturn != dataToSave)

        fragmentCreateTaskTextViewModel.saveCurrentTaskTitle(title = dataToSave.title)
        fragmentCreateTaskTextViewModel.saveCurrentTaskText(text = dataToSave.text)
        fragmentCreateTaskTextViewModel.saveTaskCategory(taskCategory = taskCategory)
        fragmentCreateTaskTextViewModel.pinCurrentTask()

        fragmentCreateTaskTextViewModel.saveCurrentTask()

        assertEquals(true, fakeInsertingTaskRepository.dataToReturn == dataToSave)

        val newText = "some new text"
        fragmentCreateTaskTextViewModel.saveCurrentTaskText(text = newText)
        fragmentCreateTaskTextViewModel.saveCurrentTask()
        assertEquals(true, fakeInsertingTaskRepository.dataToReturn.text == ""))
    }

    @Test
    fun `test, save task and request save task when nothing has been changed`() {
        assertEquals(true, fakeInsertingTaskRepository.dataToReturn != dataToSave)

        fragmentCreateTaskTextViewModel.saveCurrentTaskTitle(title = dataToSave.title)
        fragmentCreateTaskTextViewModel.saveCurrentTaskText(text = dataToSave.text)
        fragmentCreateTaskTextViewModel.saveTaskCategory(taskCategory = taskCategory)
        fragmentCreateTaskTextViewModel.pinCurrentTask()
        fragmentCreateTaskTextViewModel.saveCurrentTask()

        assertEquals(true, fakeInsertingTaskRepository.dataToReturn == dataToSave)

        fragmentCreateTaskTextViewModel.saveCurrentTask()

        assertEquals(true, fakeInsertingTaskRepository.dataToReturn == dataToSave)
    }

    @Test
    fun `test, save task, should show that was exception`() {
        fakeInsertingTaskRepository.isNeedToThrowException = true

        assertEquals(true, fakeInsertingTaskRepository.dataToReturn != dataToSave)
        assertEquals(true, fragmentCreateTaskTextViewModel.wasException()?.value == null)

        fragmentCreateTaskTextViewModel.saveCurrentTaskTitle(title = dataToSave.title)
        fragmentCreateTaskTextViewModel.saveCurrentTaskText(text = dataToSave.text)
        fragmentCreateTaskTextViewModel.saveTaskCategory(taskCategory = taskCategory)
        fragmentCreateTaskTextViewModel.pinCurrentTask()

        fragmentCreateTaskTextViewModel.saveCurrentTask()
        assertEquals(true, fragmentCreateTaskTextViewModel.wasException()?.value != null)
        assertEquals(true, fragmentCreateTaskTextViewModel.wasException()?.value.message == "some exception")
    }

}

//TODO write interface to get current date

private class FakeInsertingTaskRepository: TaskRepository.InsertingTask {

    var isNeedToThrowException = false
    var dataToReturn = Task.TaskText(
        title = "some title",
        text = "some text",
        date = "today",
        taskCategoryId = 5,
        taskType = Task.TaskType.Text,
        isPinned = false,
        id = 2
    )

    override suspend fun insertTaskText(task: Task.TaskText) {
        if (isNeedToThrowException) throw DomainException("some exception")
        dataToReturn = task
    }

    override suspend fun insertTaskList(task: Task.TaskList) {
        throw IllegalStateException("don't use this method")
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