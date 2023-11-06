package safronov.apps.taskmate.project.ui.fragment.fragment_main.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import safronov.apps.domain.use_case.task.read.GetTasksAsFlowUseCase
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList

class FragmentMainViewModelFactory(
    private val dispatchersList: DispatchersList,
    private val getTasksAsFlowUseCase: GetTasksAsFlowUseCase
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FragmentMainViewModel(
            dispatchersList = dispatchersList,
            getTasksAsFlowUseCase = getTasksAsFlowUseCase
        ) as T
    }

}