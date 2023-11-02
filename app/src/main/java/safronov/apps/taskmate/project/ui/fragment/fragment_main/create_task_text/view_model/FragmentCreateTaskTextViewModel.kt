package safronov.apps.taskmate.project.ui.fragment.fragment_main.create_task_text.view_model

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task.Task
import safronov.apps.domain.model.task_category.TaskCategory
import safronov.apps.domain.use_case.task.create.InsertTaskTextUseCase
import safronov.apps.domain.use_case.task.update.ChangeTaskTextUseCase
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList
import safronov.apps.taskmate.project.system_settings.data.DefaultTaskCategories
import safronov.apps.taskmate.project.system_settings.date.Date
import safronov.apps.taskmate.project.system_settings.view_model.BaseViewModelImpl

class FragmentCreateTaskTextViewModel(
    dispatchersList: DispatchersList,
    date: Date,
    private val insertTaskTextUseCase: InsertTaskTextUseCase,
    private val changeTaskTextUseCase: ChangeTaskTextUseCase,
    private val defaultTaskCategories: DefaultTaskCategories
): BaseViewModelImpl(dispatchersList = dispatchersList) {

    private val _taskCategory = MutableStateFlow<TaskCategory?>(null)
    private val _taskIsPin = MutableStateFlow(false)
    private val _currentTaskTitle = MutableStateFlow("")
    private val _currentTaskText = MutableStateFlow("")
    private val _wasException = MutableStateFlow<DomainException?>(null)
    private val _taskSaved = MutableStateFlow<Boolean?>(null)

    private val currentTask = Task.TaskText(
        title = _currentTaskTitle.value,
        text = _currentTaskText.value,
        date = date.getCurrentTime(),
        taskCategoryId = _taskCategory.value?.id,
        taskType = Task.TaskType.Text,
        isPinned = _taskIsPin.value,
    )

    private var isTaskSaved = false

    init {
        currentTask.date = date.getCurrentTime()
    }

    fun getTaskCategory(): StateFlow<TaskCategory?> = _taskCategory
    fun getIsTaskPin(): StateFlow<Boolean> = _taskIsPin
    fun getCurrentTaskTitle(): StateFlow<String> = _currentTaskTitle
    fun getCurrentTaskText(): StateFlow<String> = _currentTaskText
    fun isWasException(): StateFlow<DomainException?> = _wasException
    fun getTaskSaved(): StateFlow<Boolean?> = _taskSaved

    fun getCurrentTime(): String {
        return currentTask.date.toString()
    }

    fun loadDefaultTaskCategory() {
        if (_taskCategory.value == null) {
            saveTaskCategory(defaultTaskCategories.getDefaultTaskCategory())
        }
    }

    fun saveTaskCategory(taskCategory: TaskCategory) {
        _taskCategory.value = taskCategory
        currentTask.taskCategoryId = _taskCategory.value?.id
    }

    fun pinCurrentTask() {
        _taskIsPin.value = !(_taskIsPin.value)
        currentTask.isPinned = _taskIsPin.value
    }

    fun saveCurrentTaskTitle(title: String?) {
        _currentTaskTitle.value = title.toString()
        currentTask.title = _currentTaskTitle.value
    }

    fun saveCurrentTaskText(text: String?) {
        _currentTaskText.value = text.toString()
        currentTask.text = _currentTaskText.value
    }

    fun saveCurrentTask() {
        asyncWork(
            showUiWorkStarted = {},
            doWork = {
                if (_currentTaskTitle.value.isEmpty() && _currentTaskText.value.isEmpty()) {
                    return@asyncWork false
                }
                if (isTaskSaved) {
                    changeTaskTextUseCase.execute(currentTask)
                } else {
                    currentTask.id = insertTaskTextUseCase.execute(currentTask)
                    isTaskSaved = true
                }
                return@asyncWork true
            },
            showUi = {
                _taskSaved.value = null
                _taskSaved.value = it
            },
            wasException = {
                _wasException.value = it
            }
        )
    }

    fun getTaskCategories(): List<TaskCategory> {
        return defaultTaskCategories.getDefaultTaskCategories()
    }

}
