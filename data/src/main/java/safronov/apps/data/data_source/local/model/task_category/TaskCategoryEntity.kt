package safronov.apps.data.data_source.local.model.task_category

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import safronov.apps.domain.model.task_category.TaskCategory
import safronov.apps.domain.model.task_category.category_type.CategoryTypes

@Entity(tableName = TaskCategoryEntity.TASK_CATEGORY_TABLE_NAME)
data class TaskCategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo
    val icon: Int?,
    @ColumnInfo
    val backgroundColor: Int?,
    @ColumnInfo
    val categoryName: String?,
    @ColumnInfo
    val categoryType: CategoryTypes?
) {
    companion object {
        const val TASK_CATEGORY_TABLE_NAME = "TaskCategoriesTableName"

        fun convertTaskCategoryEntityToTaskCategory(taskCategoryEntity: TaskCategoryEntity): TaskCategory {
            return TaskCategory(
                id = taskCategoryEntity.id?.toLong(),
                icon = taskCategoryEntity.icon,
                backgroundColor = taskCategoryEntity.backgroundColor,
                categoryType = taskCategoryEntity.categoryType,
                categoryName = taskCategoryEntity.categoryName
            )
        }

        fun convertTaskCategoryToTaskCategoryEntity(
            taskCategory: TaskCategory
        ): TaskCategoryEntity {
            return TaskCategoryEntity(
                id = taskCategory.id?.toInt(),
                icon = taskCategory.icon,
                backgroundColor = taskCategory.backgroundColor,
                categoryType = taskCategory.categoryType,
                categoryName = taskCategory.categoryName
            )
        }

        fun convertListOfTaskCategoryEntityToListOfTaskCategory(
            list: List<TaskCategoryEntity>
        ): List<TaskCategory> {
            val mList = mutableListOf<TaskCategory>()
            list.forEach {
                mList.add(TaskCategoryEntity.convertTaskCategoryEntityToTaskCategory(it))
            }
            return mList
        }

        fun convertListOfTaskCategoryToListOfTaskCategoryEntity(
            list: List<TaskCategory>
        ): List<TaskCategoryEntity> {
            val mList = mutableListOf<TaskCategoryEntity>()
            list.forEach {
                mList.add(TaskCategoryEntity.convertTaskCategoryToTaskCategoryEntity(taskCategory = it))
            }
            return mList
        }

    }
}
