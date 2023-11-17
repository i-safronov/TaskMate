package safronov.apps.taskmate.project.ui.fragment.fragment_main.view_model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task.Task
import safronov.apps.domain.model.task_category.TaskCategory
import safronov.apps.domain.use_case.task.delete.DeleteTasksUseCase
import safronov.apps.domain.use_case.task.read.GetTasksAsFlowByTaskCategoryUseCase
import safronov.apps.domain.use_case.task.read.GetTasksAsFlowUseCase
import safronov.apps.domain.use_case.task_category.read.GetTaskCategoriesUseCase
import safronov.apps.domain.use_case.task_category.update.UpdateTaskCategoriesUseCase
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList
import safronov.apps.taskmate.project.system_settings.view_model.BaseViewModelImpl

//TODO load default task category and after this load tasks by category, so add method to change category

class FragmentMainViewModel(
     private val dispatchersList: DispatchersList,
     private val getTasksAsFlowUseCase: GetTasksAsFlowUseCase,
     private val deleteTasksUseCase: DeleteTasksUseCase,
     private val getTaskCategoriesUseCase: GetTaskCategoriesUseCase,
     private val updateTaskCategoriesUseCase: UpdateTaskCategoriesUseCase,
     private val getTasksAsFlowByTaskCategoryUseCase: GetTasksAsFlowByTaskCategoryUseCase
): BaseViewModelImpl(dispatchersList = dispatchersList) {

    private val _isLoading = MutableStateFlow<Boolean?>(null)
    private val _tasks = MutableStateFlow<List<Task>?>(null)
    private val _isException = MutableStateFlow<DomainException?>(null)
    private val _taskCategories = MutableStateFlow<List<TaskCategory>>(emptyList())
    private val _taskCategory = MutableStateFlow<TaskCategory?>(null)

    fun getIsLoading(): StateFlow<Boolean?> = _isLoading
    fun getTasks(): StateFlow<List<Task>?> = _tasks
    fun getIsWasException(): StateFlow<DomainException?> = _isException
    fun getCategories(): StateFlow<List<TaskCategory>> = _taskCategories
    fun getCategory(): StateFlow<TaskCategory?> = _taskCategory

    fun whichFragmentToGoByTaskType(
        taskType: Task.TaskType,
        taskText: () -> Unit,
        taskList: () -> Unit
    ) {
        if (taskType == Task.TaskType.Text) {
            taskText.invoke()
        } else if (taskType == Task.TaskType.List) {
            taskList.invoke()
        }
    }

    fun loadTasks() {
        asyncWork(
            showUiWorkStarted = {
                _isLoading.value = true
            },
            doWork = {
                _taskCategories.value = getTaskCategoriesUseCase.execute().first()
                if (_taskCategory.value == null && _taskCategories.value.isNotEmpty()) {
                    _taskCategory.value = _taskCategories.value.first()
                }
                getTasksAsFlowByTaskCategoryUseCase.execute(
                    taskCategory = _taskCategory.value ?: throw DomainException("couldn't found task category")
                ).collect {
                    _tasks.value = sortTasksByPinned(it)
                    withContext(dispatchersList.ui()) {
                        _isLoading.value = false
                    }
                }
            }, showUi = { },
            wasException = {
                _isException.value = it
            }
        )
    }

    fun updateTaskCategories(categories: List<TaskCategory>) {
        asyncWork(
            showUiWorkStarted = {},
            doWork = {
                updateTaskCategoriesUseCase.execute(categories)
            }, showUi = {}, wasException = {
                _isException.value = it
            }
        )
    }

    fun deleteTasks(deleted: List<Task>) {
        asyncWork(showUiWorkStarted = {}, doWork = {
            deleteTasksUseCase.execute(deleted)
        }, showUi = { loadTasks() }, wasException = {
            _isException.value = it
        })
    }

    //TODO refactor this code
    private fun sortTasksByPinned(list: List<Task>): List<Task>? {
        val mList = mutableListOf<Task>()
        if (list.isNotEmpty()) {
            val mList1 = mutableListOf<Task>()
            val mList2 = mutableListOf<Task>()
            list.forEach {
                if (it is Task.TaskText && it.isPinned == true) {
                    mList1.add(0, it)
                } else if (it is Task.TaskList && it.isPinned == true) {
                    mList1.add(0, it)
                } else {
                    mList2.add(it)
                }
            }
            mList.addAll(mList1)
            mList.addAll(mList2)
        }
        return mList
    }

}