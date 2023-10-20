package safronov.apps.taskmate.project.ui.fragment.start.view_model

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.use_case.user_login.read.IsUserLoggedInUseCase
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList
import safronov.apps.taskmate.project.system_settings.view_model.BaseViewModelImpl

class FragmentStartViewModel(
    dispatchersList: DispatchersList,
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase,
): BaseViewModelImpl(dispatchersList) {

    private val _isUserLoggedIn = MutableStateFlow<Boolean?>(null)
    private val _wasException = MutableStateFlow<DomainException?>(null)

    fun checkIsUserLoggedIn() {
        asyncWork(
            doWork = {
                isUserLoggedInUseCase.execute()
            },
            showUi = {
                _isUserLoggedIn.value = it
            },
            wasException = {
                _wasException.value = it
            }
        )
    }

    fun isUserLoggedIn(): StateFlow<Boolean?> = _isUserLoggedIn.asStateFlow()
    fun wasException(): StateFlow<DomainException?> = _wasException.asStateFlow()

}