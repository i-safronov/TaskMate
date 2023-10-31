package safronov.apps.data.data_source.local.app_db.core_sql

import androidx.room.Database
import androidx.room.RoomDatabase
import safronov.apps.data.data_source.local.app_db.dao_sql.TaskCategoryDao
import safronov.apps.data.data_source.local.app_db.dao_sql.TaskDao
import safronov.apps.data.data_source.local.model.task.TaskEntity
import safronov.apps.data.data_source.local.model.task_category.TaskCategoryEntity

const val APP_DB_NAME = "TaskMateAppDB.db"

@Database(
    entities = [TaskCategoryEntity::class, TaskEntity::class], version = 2
)
abstract class AppDB(): RoomDatabase() {
    abstract fun getTaskCategoryDao(): TaskCategoryDao
    abstract fun getTaskDao(): TaskDao
}