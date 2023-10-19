package safronov.apps.data.data_source.local.app_db.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import safronov.apps.data.data_source.local.model.task_category.TaskCategoryEntity

@Dao
interface TaskCategoryDao {

    @Insert
    fun insertTaskCategories(list: List<TaskCategoryEntity>)

    @Query("SELECT * FROM ${TaskCategoryEntity.TASK_CATEGORY_TABLE_NAME}")
    fun getTaskCategories(): Flow<List<TaskCategoryEntity>>

    @Query("SELECT * FROM ${TaskCategoryEntity.TASK_CATEGORY_TABLE_NAME} WHERE id=:id")
    fun getTaskCategoryById(id: String): TaskCategoryEntity?

    @Update
    fun updateTaskCategory(taskCategory: TaskCategoryEntity)

    @Query("DELETE FROM ${TaskCategoryEntity.TASK_CATEGORY_TABLE_NAME}")
    fun clearTaskCategories()

}

class TaskCategoryDaoImpl