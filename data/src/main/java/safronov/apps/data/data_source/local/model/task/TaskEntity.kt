package safronov.apps.data.data_source.local.model.task

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import safronov.apps.domain.model.task.Task

@Entity(tableName = TaskEntity.TASK_ENTITY_TABLE_NAME)
data class TaskEntity(
    @ColumnInfo val title: String?,
    @ColumnInfo val content: String?,
    @ColumnInfo val date: String?,
    @ColumnInfo val taskCategoryId: Long?,
    @ColumnInfo val taskType: Task.TaskType?,
    @ColumnInfo val isPinned: Boolean?,
    @PrimaryKey(autoGenerate = true) val id: Long?
) {

    companion object {
        const val TASK_ENTITY_TABLE_NAME = "TaskEntityTableName"
    }

}
