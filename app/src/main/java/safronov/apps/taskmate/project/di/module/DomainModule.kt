package safronov.apps.taskmate.project.di.module

import dagger.Module
import dagger.Provides
import safronov.apps.data.data_source.local.model.converter.task.TaskEntityConverter
import safronov.apps.data.data_source.local.service.task.TaskService
import safronov.apps.data.data_source.local.service.task_category.TaskCategoryService
import safronov.apps.data.data_source.local.service.user_login.SharedPreferencesService
import safronov.apps.data.repository_impl.task.TaskRepositoryImpl
import safronov.apps.data.repository_impl.task_category.TaskCategoryRepositoryImpl
import safronov.apps.data.repository_impl.user_login.UserLoginRepositoryImpl
import safronov.apps.domain.repository.task.TaskRepository
import safronov.apps.domain.repository.task_category.TaskCategoryRepository
import safronov.apps.domain.repository.user_login.UserLoginRepository
import safronov.apps.domain.use_case.task.create.InsertTaskListUseCase
import safronov.apps.domain.use_case.task.create.InsertTaskTextUseCase
import safronov.apps.domain.use_case.task.delete.DeleteTaskListUseCase
import safronov.apps.domain.use_case.task.delete.DeleteTaskTextUseCase
import safronov.apps.domain.use_case.task.delete.DeleteTasksUseCase
import safronov.apps.domain.use_case.task.read.GetTasksAsFlowUseCase
import safronov.apps.domain.use_case.task.read.GetTasksByTextUseCase
import safronov.apps.domain.use_case.task.read.GetTasksUseCase
import safronov.apps.domain.use_case.task.update.ChangeTaskListUseCase
import safronov.apps.domain.use_case.task.update.ChangeTaskTextUseCase
import safronov.apps.domain.use_case.task.update.ChangeTasksUseCase
import safronov.apps.domain.use_case.task_category.create.InsertTaskCategoriesUseCase
import safronov.apps.domain.use_case.task_category.read.GetTaskCategoriesUseCase
import safronov.apps.domain.use_case.task_category.read.GetTaskCategoryByIdUseCase
import safronov.apps.domain.use_case.task_category.update.UpdateTaskCategoriesUseCase
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
    fun provideUserLoginRepository(sharedPreferencesService: SharedPreferencesService): UserLoginRepository {
        return UserLoginRepositoryImpl(
            sharedPreferencesService = sharedPreferencesService
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

    @Provides
    fun provideTaskRepository(
        taskService: TaskService,
        taskEntityConverter: TaskEntityConverter
    ): TaskRepository.TaskRepositoryMutable {
        return TaskRepositoryImpl(
            taskService = taskService,
            taskEntityConverter = taskEntityConverter
        )
    }

    @Provides
    fun provideInsertTaskListUseCase(
        taskRepository: TaskRepository.TaskRepositoryMutable
    ): InsertTaskListUseCase {
        return InsertTaskListUseCase(insertingTask = taskRepository)
    }

    @Provides
    fun provideInsertTaskTextUseCase(
        taskRepository: TaskRepository.TaskRepositoryMutable
    ): InsertTaskTextUseCase {
        return InsertTaskTextUseCase(insertingTaskRepository = taskRepository)
    }

    @Provides
    fun provideDeleteTaskListUseCase(
        taskRepository: TaskRepository.TaskRepositoryMutable
    ): DeleteTaskListUseCase {
        return DeleteTaskListUseCase(deletingTaskRepository = taskRepository)
    }

    @Provides
    fun provideDeleteTasksUseCase(
        taskRepository: TaskRepository.TaskRepositoryMutable
    ): DeleteTasksUseCase {
        return DeleteTasksUseCase(deletingTaskRepository = taskRepository)
    }

    @Provides
    fun provideUpdateTaskCategoriesUseCase(
        taskCategoryRepository: TaskCategoryRepository
    ): UpdateTaskCategoriesUseCase {
        return UpdateTaskCategoriesUseCase(taskCategoryRepository)
    }

    @Provides
    fun provideDeleteTaskTextUseCase(
        taskRepository: TaskRepository.TaskRepositoryMutable
    ): DeleteTaskTextUseCase {
        return DeleteTaskTextUseCase(deletingTaskRepository = taskRepository)
    }

    @Provides
    fun provideGetTasksAsFlowUseCase(
        taskRepository: TaskRepository.TaskRepositoryMutable
    ): GetTasksAsFlowUseCase {
        return GetTasksAsFlowUseCase(gettingTaskRepository = taskRepository)
    }

    @Provides
    fun provideGetTasksByTextUseCase(
        taskRepository: TaskRepository.TaskRepositoryMutable
    ): GetTasksByTextUseCase {
        return GetTasksByTextUseCase(gettingTasksByParametersRepository = taskRepository)
    }

    @Provides
    fun provideGetTasksUseCase(
        taskRepository: TaskRepository.TaskRepositoryMutable
    ): GetTasksUseCase {
        return GetTasksUseCase(gettingTaskRepository = taskRepository)
    }

    @Provides
    fun provideChangeTaskListUseCase(
        taskRepository: TaskRepository.TaskRepositoryMutable
    ): ChangeTaskListUseCase {
        return ChangeTaskListUseCase(changingTaskRepository = taskRepository)
    }

    @Provides
    fun provideChangeTasksUseCase(
        taskRepository: TaskRepository.TaskRepositoryMutable
    ): ChangeTasksUseCase {
        return ChangeTasksUseCase(changingTasksUseCase = taskRepository)
    }

    @Provides
    fun provideChangeTaskTextUseCase(
        taskRepository: TaskRepository.TaskRepositoryMutable
    ): ChangeTaskTextUseCase {
        return ChangeTaskTextUseCase(changingTaskRepository = taskRepository)
    }

}