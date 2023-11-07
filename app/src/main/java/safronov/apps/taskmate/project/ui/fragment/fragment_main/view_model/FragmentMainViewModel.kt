package safronov.apps.taskmate.project.ui.fragment.fragment_main.view_model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task.Task
import safronov.apps.domain.use_case.task.read.GetTasksAsFlowUseCase
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList
import safronov.apps.taskmate.project.system_settings.view_model.BaseViewModelImpl

class FragmentMainViewModel(
     private val dispatchersList: DispatchersList,
     private val getTasksAsFlowUseCase: GetTasksAsFlowUseCase
): BaseViewModelImpl(dispatchersList = dispatchersList) {

    private val isLoading = MutableStateFlow<Boolean?>(null)
    private val tasks = MutableStateFlow<List<Task>?>(null)
    private val isException = MutableStateFlow<DomainException?>(null)

    fun getIsLoading(): StateFlow<Boolean?> = isLoading
    fun getTasks(): StateFlow<List<Task>?> = tasks
    fun getIsWasException(): StateFlow<DomainException?> = isException

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
                isLoading.value = true
            },
            doWork = {
                getTasksAsFlowUseCase.execute().collect {
                    tasks.value = it
                    withContext(dispatchersList.ui()) {
                        isLoading.value = false
                    }
                }
            }, showUi = { },
            wasException = {
                isException.value = it
            }
        )
    }

}