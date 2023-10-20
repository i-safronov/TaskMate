package safronov.apps.taskmate.project.ui.fragment.start.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import safronov.apps.domain.use_case.user_login.read.IsUserLoggedInUseCase
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList

class FragmentStartViewModelFactory(
    private val dispatchersList: DispatchersList,
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FragmentStartViewModel(
            dispatchersList = dispatchersList,
            isUserLoggedInUseCase = isUserLoggedInUseCase
        ) as T
    }

}