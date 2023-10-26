package safronov.apps.taskmate.project.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
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
        dispatchersList: DispatchersList
    ): FragmentMainViewModelFactory {
        return FragmentMainViewModelFactory(
            dispatchersList = dispatchersList
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

}