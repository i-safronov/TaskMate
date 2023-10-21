package safronov.apps.taskmate.project.ui.fragment.welcome.view_model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.use_case.user_login.create.UserLogInUseCase
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList
import safronov.apps.taskmate.project.system_settings.view_model.BaseViewModelImpl

class FragmentWelcomeViewModel(
    dispatchersList: DispatchersList,
    private val userLoginUseCase: UserLogInUseCase
): BaseViewModelImpl(dispatchersList = dispatchersList) {

    private val _isLoading = MutableStateFlow<Boolean?>(null)
    private val _wasException = MutableStateFlow<DomainException?>(null)
    private val _userLoggedIn = MutableStateFlow<Boolean?>(null)

    fun requestToLogIn() {
        asyncWork(
            showUiWorkStarted = {
                _isLoading.value = true
            },
            doWork = {
                userLoginUseCase.execute()
            }, showUi = {
                _isLoading.value = false
                _userLoggedIn.value = it
            },
            wasException = {
                _isLoading.value = false
                _userLoggedIn.value = false
                _wasException.value = it
            }
        )
    }

    fun isLoading(): StateFlow<Boolean?> = _isLoading
    fun wasException(): StateFlow<DomainException?> = _wasException
    fun userLoggedIn(): StateFlow<Boolean?> = _userLoggedIn

}
