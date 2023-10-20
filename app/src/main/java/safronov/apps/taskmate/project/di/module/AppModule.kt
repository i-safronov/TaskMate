package safronov.apps.taskmate.project.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import safronov.apps.domain.use_case.user_login.read.IsUserLoggedInUseCase
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList
import safronov.apps.taskmate.project.ui.fragment.start.view_model.FragmentStartViewModelFactory
import javax.inject.Singleton

@Module
class AppModule(
    private val context: Context
) {

    @Provides
    fun provideAppContext() = context

    @Provides @Singleton
    fun provideDispatchersList(): DispatchersList = DispatchersList.Base()

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

}