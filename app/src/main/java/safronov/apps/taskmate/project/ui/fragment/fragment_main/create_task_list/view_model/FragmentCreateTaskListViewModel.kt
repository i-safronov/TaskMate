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
    private val date: Date,
    private val insertTaskListUseCase: InsertTaskListUseCase,
    private val changeTaskListUseCase: ChangeTaskListUseCase,
    private val defaultTaskCategories: DefaultTaskCategories
): BaseViewModelImpl(dispatchersList = dispatchersList) {

    private val _currentTaskTitle = MutableStateFlow("")
    private val _isCurrentTaskPin = MutableStateFlow(false)
    private val _taskIsPin = MutableStateFlow(false)
    private val _taskCategory = MutableStateFlow<TaskCategory?>(null)
    private val _wasException = MutableStateFlow<DomainException?>(null)

    private val currentTaskList = Task.TaskList(
        title = _currentTaskTitle.value,
        list = emptyList(),
        date = "",
        taskCategoryId = _taskCategory.value?.id,
        taskType = Task.TaskType.List,
        isPinned = _isCurrentTaskPin.value
    )

    init {
        currentTaskList.date = date.getCurrentTime()
    }

    fun getCurrentTaskTitle(): StateFlow<String> = _currentTaskTitle
    fun getIsCurrentTaskPin(): StateFlow<Boolean> = _isCurrentTaskPin
    fun getCurrentTaskCategory(): StateFlow<TaskCategory?> = _taskCategory
    fun isWasException(): StateFlow<DomainException?> = _wasException

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
        //TODO save current task
    }

    fun getCurrentTime(): String {
        return currentTaskList.date.toString()
    }

}
