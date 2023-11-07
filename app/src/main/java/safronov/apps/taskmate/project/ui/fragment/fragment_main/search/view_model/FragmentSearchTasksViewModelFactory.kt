package safronov.apps.taskmate.project.ui.fragment.fragment_main.search.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import safronov.apps.domain.use_case.task.read.GetTasksByTextUseCase
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList

class FragmentSearchTasksViewModelFactory(
    private val dispatchersList: DispatchersList,
    private val getTasksByTextUseCase: GetTasksByTextUseCase
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FragmentSearchTasksViewModel(
            dispatchersList, getTasksByTextUseCase
        ) as T
    }

}