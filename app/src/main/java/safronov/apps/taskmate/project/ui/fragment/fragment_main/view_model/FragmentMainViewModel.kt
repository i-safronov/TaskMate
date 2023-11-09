package safronov.apps.taskmate.project.ui.fragment.fragment_main.view_model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task.Task
import safronov.apps.domain.use_case.task.delete.DeleteTaskListUseCase
import safronov.apps.domain.use_case.task.delete.DeleteTasksUseCase
import safronov.apps.domain.use_case.task.read.GetTasksAsFlowUseCase
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList
import safronov.apps.taskmate.project.system_settings.view_model.BaseViewModelImpl

class FragmentMainViewModel(
     private val dispatchersList: DispatchersList,
     private val getTasksAsFlowUseCase: GetTasksAsFlowUseCase,
     private val deleteTasksUseCase: DeleteTasksUseCase
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
                    tasks.value = sortTasksByPinned(it)
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

    fun deleteTasks(deleted: List<Task>) {
        asyncWork(showUiWorkStarted = {}, doWork = {
            deleteTasksUseCase.execute(deleted)
        }, showUi = {}, wasException = {
            isException.value = it
        })
    }

}