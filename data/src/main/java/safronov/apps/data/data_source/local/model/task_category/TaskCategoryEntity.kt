package safronov.apps.data.data_source.local.model.task_category

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import safronov.apps.domain.model.task_category.TaskCategory
import safronov.apps.domain.model.task_category.category_type.CategoryTypes

@Entity(tableName = TaskCategoryEntity.TASK_CATEGORY_TABLE_NAME)
data class TaskCategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    @ColumnInfo val icon: Int?,
    @ColumnInfo val backgroundColor: Int?,
    @ColumnInfo val categoryName: String?,
    @ColumnInfo val categoryType: CategoryTypes?
) {
    companion object {
        const val TASK_CATEGORY_TABLE_NAME = "Task category"

        fun convertTaskCategoryEntityToTaskCategory(taskCategoryEntity: TaskCategoryEntity): TaskCategory {
            return TaskCategory(
                id = taskCategoryEntity.id,
                icon = taskCategoryEntity.icon,
                backgroundColor = taskCategoryEntity.backgroundColor,
                categoryType = taskCategoryEntity.categoryType,
                categoryName = taskCategoryEntity.categoryName
            )
        }

    }
}
