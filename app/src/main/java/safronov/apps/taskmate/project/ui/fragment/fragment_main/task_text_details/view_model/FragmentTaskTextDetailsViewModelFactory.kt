package safronov.apps.taskmate.project.ui.fragment.fragment_main.task_text_details.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import safronov.apps.domain.use_case.task.create.InsertTaskTextUseCase
import safronov.apps.domain.use_case.task.update.ChangeTaskTextUseCase
import safronov.apps.domain.use_case.task_category.read.GetTaskCategoryByIdUseCase
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList
import safronov.apps.taskmate.project.system_settings.data.DefaultTaskCategories
import safronov.apps.taskmate.project.system_settings.date.Date

class FragmentTaskTextDetailsViewModelFactory(
    private val dispatchersList: DispatchersList,
    private val date: Date,
    private val insertTaskTextUseCase: InsertTaskTextUseCase,
    private val changeTaskTextUseCase: ChangeTaskTextUseCase,
    private val defaultTaskCategories: DefaultTaskCategories,
    private val getTaskCategoryByIdUseCase: GetTaskCategoryByIdUseCase
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FragmentTaskTextDetailsViewModel(
            dispatchersList = dispatchersList,
            date = date,
            insertTaskTextUseCase = insertTaskTextUseCase,
            changeTaskTextUseCase = changeTaskTextUseCase,
            defaultTaskCategories = defaultTaskCategories,
            getTaskCategoryByIdUseCase = getTaskCategoryByIdUseCase
        ) as T
    }

}