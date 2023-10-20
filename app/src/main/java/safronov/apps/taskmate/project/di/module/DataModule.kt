package safronov.apps.taskmate.project.di.module

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import safronov.apps.data.data_source.local.app_db.core_sql.APP_DB_NAME
import safronov.apps.data.data_source.local.app_db.core_sql.AppDB
import safronov.apps.data.data_source.local.app_db.dao_sql.TaskCategoryDao
import safronov.apps.data.data_source.local.service.task_category.TaskCategoryService
import safronov.apps.data.data_source.local.service.task_category.TaskCategoryServiceImpl
import safronov.apps.data.data_source.local.service.user_login.UserLoginService
import safronov.apps.data.data_source.local.service.user_login.UserLoginServiceImpl
import safronov.apps.data.data_source.local.service.user_login.UserLoginServiceImpl.Companion.SHARED_PREFERENCES_NAME
import safronov.apps.data.repository_impl.task_category.TaskCategoryRepositoryImpl
import safronov.apps.data.repository_impl.user_login.UserLoginRepositoryImpl
import safronov.apps.domain.repository.task_category.TaskCategoryRepository
import safronov.apps.domain.repository.user_login.UserLoginRepository

@Module
class DataModule {

    @Provides
    fun provideAppDB(context: Context): AppDB {
        return Room.databaseBuilder(
            context = context,
            AppDB::class.java, APP_DB_NAME
            ).build()
    }

    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    fun provideTaskCategoryDao(appDB: AppDB): TaskCategoryDao {
        return appDB.getTaskCategoryDao()
    }

    @Provides
    fun provideTaskCategoryService(taskCategoryDao: TaskCategoryDao): TaskCategoryService {
        return TaskCategoryServiceImpl(
            taskCategoryDao = taskCategoryDao
        )
    }

    @Provides
    fun provideUserLoginService(sharedPreferences: SharedPreferences): UserLoginService {
        return UserLoginServiceImpl(
            sharedPreferences = sharedPreferences
        )
    }

}