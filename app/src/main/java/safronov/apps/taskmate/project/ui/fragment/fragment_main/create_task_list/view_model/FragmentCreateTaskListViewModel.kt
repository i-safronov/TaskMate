package safronov.apps.taskmate.project.ui.fragment.fragment_main.create_task_list.view_model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task.Task
import safronov.apps.domain.model.task_category.TaskCategory
import safronov.apps.domain.use_case.task.create.InsertTaskListUseCase
import safronov.apps.domain.use_case.task.update.ChangeTaskListUseCase
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList
import safronov.apps.taskmate.project.system_settings.data.DefaultTaskCategories
import safronov.apps.taskmate.project.system_settings.date.Date
import safronov.apps.taskmate.project.system_settings.view_model.BaseViewModelImpl

class FragmentCreateTaskListViewModel(
    dispatchersList: DispatchersList,
    date: Date,
    private val insertTaskListUseCase: InsertTaskListUseCase,
    private val changeTaskListUseCase: ChangeTaskListUseCase,
    private val defaultTaskCategories: DefaultTaskCategories
): BaseViewModelImpl(dispatchersList = dispatchersList) {

    private val _currentTaskTitle = MutableStateFlow("")
    private val _taskIsPin = MutableStateFlow(false)
    private val _taskCategory = MutableStateFlow<TaskCategory?>(null)
    private val _wasException = MutableStateFlow<DomainException?>(null)
    private val _taskSaved = MutableStateFlow<Boolean?>(null)
    private var taskSaved = false

    private val currentTaskList = Task.TaskList(
        title = _currentTaskTitle.value,
        list = emptyList(),
        date = "",
        taskCategoryId = _taskCategory.value?.id,
        taskType = Task.TaskType.List,
        isPinned = _taskIsPin.value
    )

    init {
        currentTaskList.date = date.getCurrentTime()
    }

    fun getCurrentTaskTitle(): StateFlow<String> = _currentTaskTitle
    fun getIsCurrentTaskPin(): StateFlow<Boolean> = _taskIsPin
    fun getCurrentTaskCategory(): StateFlow<TaskCategory?> = _taskCategory
    fun isWasException(): StateFlow<DomainException?> = _wasException
    fun getTaskSaved(): StateFlow<Boolean?> = _taskSaved

    fun getCurrentTime(): String {
        return currentTaskList.date.toString()
    }

    fun saveCurrentTaskTitle(title: String) {
        _currentTaskTitle.value = title
        currentTaskList.title = _currentTaskTitle.value
    }

    fun pinCurrentTask() {
        _taskIsPin.value = !(_taskIsPin.value)
        currentTaskList.isPinned = _taskIsPin.value
    }

    fun saveCurrentTaskCategory(taskCategory: TaskCategory) {
        _taskCategory.value = taskCategory
        currentTaskList.taskCategoryId = _taskCategory.value?.id
    }

    fun loadDefaultCurrentTaskCategory() {
        _taskCategory.value = defaultTaskCategories.getDefaultTaskCategory()
        currentTaskList.taskCategoryId = _taskCategory.value?.id
    }

    fun getCurrentTaskListItems(): List<Task.TaskListItem> {
        return currentTaskList.list ?: emptyList()
    }

    fun saveCurrentTask(taskListItems: List<Task.TaskListItem>) {
        asyncWork(
            showUiWorkStarted = {},
            doWork = {
                if (taskListItems.isEmpty() && _currentTaskTitle.value.isEmpty()) {
                    return@asyncWork false
                }
                currentTaskList.list = taskListItems
                if (taskSaved) {
                    changeTaskListUseCase.execute(currentTaskList)
                } else {
                    insertTaskListUseCase.execute(currentTaskList)
                    taskSaved = true
                }
                return@asyncWork true
            },
            showUi = {
                _taskSaved.value = it
                _taskSaved.value = null
            }, wasException = {
                _wasException.value = it
            }
        )
    }

}
