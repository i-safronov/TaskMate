package safronov.apps.taskmate.project.di.module

import android.content.Context
import android.util.Log
import dagger.Module
import dagger.Provides
import safronov.apps.domain.use_case.task_category.create.InsertTaskCategoriesUseCase
import safronov.apps.domain.use_case.user_login.create.UserLogInUseCase
import safronov.apps.domain.use_case.user_login.read.IsUserLoggedInUseCase
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList
import safronov.apps.taskmate.project.system_settings.data.DefaultTaskCategories
import safronov.apps.taskmate.project.system_settings.full_screen_app.FullScreenApp
import safronov.apps.taskmate.project.system_settings.full_screen_app.FullScreenAppImpl
import safronov.apps.taskmate.project.system_settings.ui.bottom_sheet.BottomSheet
import safronov.apps.taskmate.project.ui.fragment.start.view_model.FragmentStartViewModelFactory
import safronov.apps.taskmate.project.ui.fragment.welcome.view_model.FragmentWelcomeViewModelFactory

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
    fun provideBottomSheet(): BottomSheet {
        return BottomSheet.Base(context = context)
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

}