package safronov.apps.taskmate.project.ui.fragment.welcome.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import safronov.apps.domain.use_case.task_category.create.InsertTaskCategoriesUseCase
import safronov.apps.domain.use_case.user_login.create.UserLogInUseCase
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList

class FragmentWelcomeViewModelFactory(
    private val dispatchersList: DispatchersList,
    private val userLoginUseCase: UserLogInUseCase,
    private val insertTaskCategoriesUseCase: InsertTaskCategoriesUseCase
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FragmentWelcomeViewModel(
            dispatchersList = dispatchersList,
            userLoginUseCase = userLoginUseCase,
            insertTaskCategoriesUseCase = insertTaskCategoriesUseCase
        ) as T
    }

}