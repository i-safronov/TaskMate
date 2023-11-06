package safronov.apps.taskmate.project.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import safronov.apps.domain.use_case.task.create.InsertTaskListUseCase
import safronov.apps.domain.use_case.task.create.InsertTaskTextUseCase
import safronov.apps.domain.use_case.task.read.GetTasksAsFlowUseCase
import safronov.apps.domain.use_case.task.update.ChangeTaskListUseCase
import safronov.apps.domain.use_case.task.update.ChangeTaskTextUseCase
import safronov.apps.domain.use_case.task_category.create.InsertTaskCategoriesUseCase
import safronov.apps.domain.use_case.user_login.create.UserLogInUseCase
import safronov.apps.domain.use_case.user_login.read.IsUserLoggedInUseCase
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList
import safronov.apps.taskmate.project.system_settings.data.DefaultTaskCategories
import safronov.apps.taskmate.project.system_settings.date.Date
import safronov.apps.taskmate.project.system_settings.full_screen_app.FullScreenApp
import safronov.apps.taskmate.project.system_settings.full_screen_app.FullScreenAppImpl
import safronov.apps.taskmate.project.system_settings.ui.bottom_sheet.BottomSheet
import safronov.apps.taskmate.project.system_settings.ui.rcv.RecyclerViewBuilder
import safronov.apps.taskmate.project.system_settings.ui.text_watcher.TextWatcher
import safronov.apps.taskmate.project.system_settings.ui.tool_bar.HomePageToolBarService
import safronov.apps.taskmate.project.ui.fragment.fragment_main.create_task_list.view_model.FragmentCreateTaskListViewModelFactory
import safronov.apps.taskmate.project.ui.fragment.fragment_main.create_task_text.view_model.FragmentCreateTaskTextViewModelFactory
import safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.task_type.AllTaskTypes
import safronov.apps.taskmate.project.ui.fragment.fragment_main.view_model.FragmentMainViewModelFactory
import safronov.apps.taskmate.project.ui.fragment.start.view_model.FragmentStartViewModelFactory
import safronov.apps.taskmate.project.ui.fragment.welcome.view_model.FragmentWelcomeViewModelFactory
import java.util.Calendar

@Module
class AppModule(
    private val context: Context
) {

    @Provides
    fun provideAppContext() = context

    @Provides
    fun provideDispatchersList(): DispatchersList = DispatchersList.Base()

    @Provides
    fun provideDefaultTaskCategories(): DefaultTaskCategories {
        return DefaultTaskCategories.Base(
            context = context
        )
    }

    @Provides
    fun provideHomePageToolBarService(): HomePageToolBarService {
        return HomePageToolBarService.Base()
    }

    @Provides
    fun provideTextWatcher(): TextWatcher {
        return TextWatcher.Base()
    }

    @Provides
    fun provideFullScreenApp(): FullScreenApp {
        return FullScreenAppImpl()
    }

    @Provides
    fun provideRecyclerViewBuilder(): RecyclerViewBuilder {
        return RecyclerViewBuilder.Base()
    }

    @Provides
    fun provideBottomSheet(): BottomSheet {
        return BottomSheet.Base()
    }

    @Provides
    fun provideAllTaskTypes(): AllTaskTypes {
        return AllTaskTypes.Base(context = context)
    }

    @Provides
    fun provideFragmentMainViewModelFactory(
        dispatchersList: DispatchersList,
        getTasksAsFlowUseCase: GetTasksAsFlowUseCase
    ): FragmentMainViewModelFactory {
        return FragmentMainViewModelFactory(
            dispatchersList = dispatchersList,
            getTasksAsFlowUseCase = getTasksAsFlowUseCase
        )
    }

    @Provides
    fun provideFragmentStartViewModelFactory(
        dispatchersList: DispatchersList,
        isUserLoggedInUseCase: IsUserLoggedInUseCase
    ): FragmentStartViewModelFactory {
        return FragmentStartViewModelFactory(
            dispatchersList = dispatchersList,
            isUserLoggedInUseCase = isUserLoggedInUseCase
        )
    }

    @Provides
    fun provideFragmentWelcomeViewModelFactory(
        dispatchersList: DispatchersList,
        userLoginUseCase: UserLogInUseCase,
        insertTaskCategoriesUseCase: InsertTaskCategoriesUseCase
    ): FragmentWelcomeViewModelFactory {
        return FragmentWelcomeViewModelFactory(
            dispatchersList = dispatchersList,
            userLoginUseCase = userLoginUseCase,
            insertTaskCategoriesUseCase = insertTaskCategoriesUseCase
        )
    }

    @Provides
    fun provideCalendar(): Calendar {
        return Calendar.getInstance()
    }

    @Provides
    fun provideDate(
        calendar: Calendar
    ): Date {
        return Date.Base(calendar = calendar)
    }

    @Provides
    fun provideFragmentCreateTaskListViewModelFactory(
        dispatchersList: DispatchersList,
        date: Date,
        insertTaskListUseCase: InsertTaskListUseCase,
        changeTaskListUseCase: ChangeTaskListUseCase,
        defaultTaskCategories: DefaultTaskCategories
    ): FragmentCreateTaskListViewModelFactory {
        return FragmentCreateTaskListViewModelFactory(
            dispatchersList = dispatchersList,
            date = date,
            insertTaskListUseCase = insertTaskListUseCase,
            changeTaskListUseCase = changeTaskListUseCase,
            defaultTaskCategories = defaultTaskCategories
        )
    }

    @Provides
    fun provideFragmentCreateTaskTextViewModelFactory(
        dispatchersList: DispatchersList,
        date: Date,
        insertTaskTextUseCase: InsertTaskTextUseCase,
        changeTaskTextUseCase: ChangeTaskTextUseCase,
        defaultTaskCategories: DefaultTaskCategories
    ): FragmentCreateTaskTextViewModelFactory {
        return FragmentCreateTaskTextViewModelFactory(
            dispatchersList = dispatchersList,
            date = date,
            insertTaskTextUseCase = insertTaskTextUseCase,
            changeTaskTextUseCase = changeTaskTextUseCase,
            defaultTaskCategories = defaultTaskCategories
        )
    }

}