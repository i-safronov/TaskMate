package safronov.apps.taskmate.project.ui.fragment.fragment_main.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import safronov.apps.domain.use_case.task.delete.DeleteTasksUseCase
import safronov.apps.domain.use_case.task.read.GetTasksAsFlowUseCase
import safronov.apps.domain.use_case.task_category.read.GetTaskCategoriesUseCase
import safronov.apps.domain.use_case.task_category.update.UpdateTaskCategoriesUseCase
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList

class FragmentMainViewModelFactory(
    private val dispatchersList: DispatchersList,
    private val getTasksAsFlowUseCase: GetTasksAsFlowUseCase,
    private val deleteTasksUseCase: DeleteTasksUseCase,
    private val getTaskCategoriesUseCase: GetTaskCategoriesUseCase,
    private val updateTaskCategoriesUseCase: UpdateTaskCategoriesUseCase
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FragmentMainViewModel(
            dispatchersList = dispatchersList,
            getTasksAsFlowUseCase = getTasksAsFlowUseCase,
            deleteTasksUseCase = deleteTasksUseCase,
            getTaskCategoriesUseCase = getTaskCategoriesUseCase,
            updateTaskCategoriesUseCase = updateTaskCategoriesUseCase
        ) as T
    }

}