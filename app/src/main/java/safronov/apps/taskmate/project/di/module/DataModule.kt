package safronov.apps.taskmate.project.di.module

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import safronov.apps.data.data_source.local.app_db.core_sql.APP_DB_NAME
import safronov.apps.data.data_source.local.app_db.core_sql.AppDB
import safronov.apps.data.data_source.local.app_db.dao_sql.TaskCategoryDao
import safronov.apps.data.data_source.local.app_db.dao_sql.TaskDao
import safronov.apps.data.data_source.local.model.converter.task.TaskEntityConverter
import safronov.apps.data.data_source.local.model.converter.task.TaskEntityConverterImpl
import safronov.apps.data.data_source.local.service.task.TaskService
import safronov.apps.data.data_source.local.service.task.TaskServiceImpl
import safronov.apps.data.data_source.local.service.task_category.TaskCategoryService
import safronov.apps.data.data_source.local.service.task_category.TaskCategoryServiceImpl
import safronov.apps.data.data_source.local.service.user_login.SharedPreferencesService
import safronov.apps.data.data_source.local.service.user_login.SharedPreferencesServiceImpl
import safronov.apps.data.data_source.local.service.user_login.SharedPreferencesServiceImpl.Companion.SHARED_PREFERENCES_NAME
import safronov.apps.data.repository_impl.task_layout_manager.TaskLayoutManagerRepositoryImpl
import safronov.apps.domain.repository.task_layout_manager.TaskLayoutManagerRepository

@Module
class DataModule {

    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

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
    fun provideSharedPreferencesService(sharedPreferences: SharedPreferences): SharedPreferencesService {
        return SharedPreferencesServiceImpl(
            sharedPreferences = sharedPreferences
        )
    }

    @Provides
    fun provideTaskDao(appDB: AppDB): TaskDao {
        return appDB.getTaskDao()
    }

    @Provides
    fun provideTaskService(taskDao: TaskDao): TaskService {
        return TaskServiceImpl(
            taskDao = taskDao
        )
    }

    @Provides
    fun provideTaskEntityConverter(gson: Gson): TaskEntityConverter {
        return TaskEntityConverterImpl(gson = gson)
    }

    @Provides
    fun provideMutableTaskLayoutManagerRepository(
        sharedPreferencesService: SharedPreferencesService
    ): TaskLayoutManagerRepository.MutableTaskLayoutManagerRepository {
        return TaskLayoutManagerRepositoryImpl(
            sharedPreferencesService = sharedPreferencesService
        )
    }

}