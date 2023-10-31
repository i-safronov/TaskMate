package safronov.apps.taskmate.project.ui.fragment.fragment_main.create_task_list.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import safronov.apps.domain.use_case.task.create.InsertTaskListUseCase
import safronov.apps.domain.use_case.task.update.ChangeTaskListUseCase
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList
import safronov.apps.taskmate.project.system_settings.data.DefaultTaskCategories
import safronov.apps.taskmate.project.system_settings.date.Date

class FragmentCreateTaskListViewModelFactory(
    private val dispatchersList: DispatchersList,
    private val date: Date,
    private val insertTaskListUseCase: InsertTaskListUseCase,
    private val changeTaskListUseCase: ChangeTaskListUseCase,
    private val defaultTaskCategories: DefaultTaskCategories
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FragmentCreateTaskListViewModel(
            dispatchersList = dispatchersList,
            date = date,
            insertTaskListUseCase = insertTaskListUseCase,
            changeTaskListUseCase = changeTaskListUseCase,
            defaultTaskCategories = defaultTaskCategories
        ) as T
    }

}