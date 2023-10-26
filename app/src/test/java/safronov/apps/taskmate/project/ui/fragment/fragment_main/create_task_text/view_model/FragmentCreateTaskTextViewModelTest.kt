package safronov.apps.taskmate.project.ui.fragment.fragment_main.create_task_text.view_model

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.*
import org.junit.Before
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task.Task
import safronov.apps.domain.repository.task.TaskRepository
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

    @Before
    fun setup() {
        dataToSave = Task.TaskText(
            title = "some title3",
            text = "some tex2t",
            date = "today1",
            taskCategoryId = 55,
            taskType = Task.TaskType.Text,
            isPinned = true,
            id = 1
        )
    }

}

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