package safronov.apps.taskmate.project.ui.fragment.fragment_main.task_list_details.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import safronov.apps.domain.use_case.task.create.InsertTaskListUseCase
import safronov.apps.domain.use_case.task.update.ChangeTaskListUseCase
import safronov.apps.domain.use_case.task_category.read.GetTaskCategoryByIdUseCase
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList
import safronov.apps.taskmate.project.system_settings.data.DefaultTaskCategories
import safronov.apps.taskmate.project.system_settings.date.Date

class FragmentTaskListDetailsViewModelFactory(
    private val dispatchersList: DispatchersList,
    private val date: Date,
    private val insertTaskListUseCase: InsertTaskListUseCase,
    private val changeTaskListUseCase: ChangeTaskListUseCase,
    private val defaultTaskCategories: DefaultTaskCategories,
    private val getTaskCategoryByIdUseCase: GetTaskCategoryByIdUseCase
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FragmentTaskListDetailsViewModel(
            dispatchersList = dispatchersList,
            date = date,
            insertTaskListUseCase = insertTaskListUseCase,
            changeTaskListUseCase = changeTaskListUseCase,
            defaultTaskCategories = defaultTaskCategories,
            getTaskCategoryByIdUseCase = getTaskCategoryByIdUseCase
        ) as T
    }

}