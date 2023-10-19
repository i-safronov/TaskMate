package safronov.apps.data.data_source.local.app_db.core

import androidx.room.Database
import androidx.room.RoomDatabase
import safronov.apps.data.data_source.local.app_db.dao.TaskCategoryDao
import safronov.apps.data.data_source.local.model.task_category.TaskCategoryEntity

@Database(
    entities = [TaskCategoryEntity::class], version = 1
)
abstract class AppDB(): RoomDatabase() {
    abstract fun getTaskCategoryDao(): TaskCategoryDao
}