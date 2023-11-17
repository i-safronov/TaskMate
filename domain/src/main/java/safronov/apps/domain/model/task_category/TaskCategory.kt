package safronov.apps.domain.model.task_category

import safronov.apps.domain.model.task_category.category_type.CategoryTypes

data class TaskCategory(
    val id: Long? = null,
    val icon: Int?,
    val backgroundColor: Int?,
    var categoryName: String?,
    var categoryType: CategoryTypes?
)
