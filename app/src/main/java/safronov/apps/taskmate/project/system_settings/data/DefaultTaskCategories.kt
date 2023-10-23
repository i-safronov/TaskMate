package safronov.apps.taskmate.project.system_settings.data

import android.content.Context
import safronov.apps.domain.model.task_category.TaskCategory
import safronov.apps.domain.model.task_category.category_type.CategoryTypes
import safronov.apps.taskmate.R

interface DefaultTaskCategories {

    fun getDefaultTaskCategories(): List<TaskCategory>

    class Base(
        private val context: Context
    ): DefaultTaskCategories {

        override fun getDefaultTaskCategories(): List<TaskCategory> {
            return listOf(
                TaskCategory(
                    icon = R.drawable.ic_block,
                    backgroundColor = R.color.back,
                    categoryName = context.getString(R.string.all_tasks),
                    categoryType = CategoryTypes.System
                ),
                TaskCategory(
                    icon = R.drawable.green_task_category,
                    backgroundColor = R.color.green_transparent,
                    categoryName = context.getString(R.string.work),
                    categoryType = CategoryTypes.User
                ),
                TaskCategory(
                    icon = R.drawable.orange_task_category,
                    backgroundColor = R.color.orange_transparent,
                    categoryName = context.getString(R.string.personal),
                    categoryType = CategoryTypes.User
                ),
                TaskCategory(
                    icon = R.drawable.blue_task_category,
                    backgroundColor = R.color.blue_transparent,
                    categoryName = context.getString(R.string.other),
                    categoryType = CategoryTypes.User
                ),
                TaskCategory(
                    icon = R.drawable.red_task_category,
                    backgroundColor = R.color.red_transparent,
                    categoryName = "",
                    categoryType = CategoryTypes.User
                ),
                TaskCategory(
                    icon = R.drawable.yellow_task_category,
                    backgroundColor = R.color.yellow_transparent,
                    categoryName = "",
                    categoryType = CategoryTypes.User
                ),
                TaskCategory(
                    icon = R.drawable.purple_task_category,
                    backgroundColor = R.color.purple_transparent,
                    categoryName = "",
                    categoryType = CategoryTypes.User
                )
            )
        }

    }

}