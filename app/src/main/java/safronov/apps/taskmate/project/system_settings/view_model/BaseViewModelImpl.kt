package safronov.apps.taskmate.project.system_settings.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import safronov.apps.domain.exception.DomainException
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList

open class BaseViewModelImpl(
    private val dispatchersList: DispatchersList
): ViewModel(), BaseViewModel {

    override fun <T : Any> asyncWork(
        doWork: suspend () -> T, showUi: (T) -> Unit, wasException: (DomainException) -> Unit
    ): Job = viewModelScope.launch(dispatchersList.io()) {
        try {
            val result = doWork.invoke()
            withContext(dispatchersList.ui()) {
                showUi.invoke(result)
            }
        } catch (e: DomainException) {
            wasException.invoke(e)
        }
    }

}

interface BaseViewModel {
    fun <T: Any> asyncWork(
        doWork: suspend () -> T,
        showUi: (T) -> Unit,
        wasException: (DomainException) -> Unit
    ): Job
}