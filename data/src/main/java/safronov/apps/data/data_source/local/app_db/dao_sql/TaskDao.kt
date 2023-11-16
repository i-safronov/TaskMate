package safronov.apps.data.data_source.local.app_db.dao_sql

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import safronov.apps.data.data_source.local.model.task.TaskEntity

@Dao
interface TaskDao {

    @Insert
    fun insertTask(task: TaskEntity): Long?

    @Query("SELECT * FROM ${TaskEntity.TASK_ENTITY_TABLE_NAME}")
    fun getTasksAsFlow(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM ${TaskEntity.TASK_ENTITY_TABLE_NAME} WHERE taskCategoryId=:taskCategoryId")
    fun getTasksAsFlowByTaskCategoryId(taskCategoryId: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM ${TaskEntity.TASK_ENTITY_TABLE_NAME}")
    fun getTasks(): List<TaskEntity>

    @Query("SELECT * FROM ${TaskEntity.TASK_ENTITY_TABLE_NAME} " +
            "WHERE title LIKE '%' || :text || '%' " +
            "OR content LIKE '%' || :text || '%'")
    fun getTasksByText(text: String): List<TaskEntity>

    @Update
    fun changeTask(task: TaskEntity)

    @Update
    fun changeTasks(tasks: List<TaskEntity>)

    @Delete
    fun deleteTask(task: TaskEntity)

    @Delete
    fun deleteTasks(tasks: List<TaskEntity>)

}