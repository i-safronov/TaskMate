package safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.task_category

import safronov.apps.domain.model.task_category.TaskCategory

interface RcvTaskCategoryInt {
    fun onTaskCategoryClick(taskCategory: TaskCategory)
}