package safronov.apps.taskmate.project.ui.fragment.fragment_main.search.view_model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import safronov.apps.domain.exception.DomainException
import safronov.apps.domain.model.task.Task
import safronov.apps.domain.use_case.task.read.GetTasksByTextUseCase
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList
import safronov.apps.taskmate.project.system_settings.view_model.BaseViewModelImpl

class FragmentSearchTasksViewModel(
    dispatchersList: DispatchersList,
    private val getTasksByTextUseCase: GetTasksByTextUseCase
): BaseViewModelImpl(dispatchersList = dispatchersList) {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    private val _wasException = MutableStateFlow<DomainException?>(null)

    fun getTasks(): StateFlow<List<Task>> = _tasks
    fun isWasException(): StateFlow<DomainException?> = _wasException

    fun getTasksByText(text: String) {
        asyncWork(
            showUiWorkStarted = {},
            doWork = {
                getTasksByTextUseCase.execute(text = text)
            }, showUi = {
                _tasks.value = it
            }, wasException = {
                _wasException.value = it
            }
        )
    }

}
