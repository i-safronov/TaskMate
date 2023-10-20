package safronov.apps.taskmate.project.di.module

import dagger.Module
import dagger.Provides
import safronov.apps.data.data_source.local.service.task_category.TaskCategoryService
import safronov.apps.data.data_source.local.service.user_login.UserLoginService
import safronov.apps.data.repository_impl.task_category.TaskCategoryRepositoryImpl
import safronov.apps.data.repository_impl.user_login.UserLoginRepositoryImpl
import safronov.apps.domain.repository.task_category.TaskCategoryRepository
import safronov.apps.domain.repository.user_login.UserLoginRepository
import safronov.apps.domain.use_case.task_category.create.InsertTaskCategoriesUseCase
import safronov.apps.domain.use_case.task_category.read.GetTaskCategoriesUseCase
import safronov.apps.domain.use_case.task_category.read.GetTaskCategoryByIdUseCase
import safronov.apps.domain.use_case.task_category.update.UpdateTaskCategoryUseCase
import safronov.apps.domain.use_case.user_login.create.UserLogInUseCase
import safronov.apps.domain.use_case.user_login.read.IsUserLoggedInUseCase

@Module
class DomainModule {

    @Provides
    fun provideTaskCategoryRepository(taskCategoryService: TaskCategoryService): TaskCategoryRepository {
        return TaskCategoryRepositoryImpl(
            taskCategoryService = taskCategoryService
        )
    }

    @Provides
    fun provideUserLoginRepository(userLoginService: UserLoginService): UserLoginRepository {
        return UserLoginRepositoryImpl(
            userLoginService = userLoginService
        )
    }

    @Provides
    fun provideGetTaskCategoriesUseCase(
        taskCategoryRepository: TaskCategoryRepository
    ): GetTaskCategoriesUseCase {
        return GetTaskCategoriesUseCase(
            taskCategoryRepository = taskCategoryRepository
        )
    }

    @Provides
    fun provideGetTaskCategoryByIdUseCase(
        taskCategoryRepository: TaskCategoryRepository
    ): GetTaskCategoryByIdUseCase {
        return GetTaskCategoryByIdUseCase(
            taskCategoryRepository = taskCategoryRepository
        )
    }

    @Provides
    fun provideInsertTaskCategoriesUseCase(
        taskCategoryRepository: TaskCategoryRepository,
        getTaskCategoriesUseCase: GetTaskCategoriesUseCase
    ): InsertTaskCategoriesUseCase {
        return InsertTaskCategoriesUseCase(
            taskCategoryRepository = taskCategoryRepository,
            getTaskCategoriesUseCase = getTaskCategoriesUseCase
        )
    }

    @Provides
    fun provideUpdateTaskCategoryUseCase(
        taskCategoryRepository: TaskCategoryRepository,
        getTaskCategoriesUseCase: GetTaskCategoriesUseCase,
        getTaskCategoryByIdUseCase: GetTaskCategoryByIdUseCase
    ): UpdateTaskCategoryUseCase {
        return UpdateTaskCategoryUseCase(
            taskCategoryRepository = taskCategoryRepository,
            getTaskCategoriesUseCase = getTaskCategoriesUseCase,
            getTaskCategoryByIdUseCase = getTaskCategoryByIdUseCase
        )
    }

    @Provides
    fun provideUserLoginUseCase(
        userLoginRepository: UserLoginRepository
    ): UserLogInUseCase {
        return UserLogInUseCase(
            userLoginRepository = userLoginRepository
        )
    }

    @Provides
    fun isUserLoggedUseCase(
        userLoginRepository: UserLoginRepository
    ): IsUserLoggedInUseCase {
        return IsUserLoggedInUseCase(
            userLoginRepository = userLoginRepository
        )
    }

}