package safronov.apps.taskmate.project.ui.fragment.fragment_main.view_model

import safronov.apps.domain.model.task.Task
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList
import safronov.apps.taskmate.project.system_settings.view_model.BaseViewModelImpl

class FragmentMainViewModel(
     dispatchersList: DispatchersList
): BaseViewModelImpl(dispatchersList = dispatchersList) {

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

}