package safronov.apps.taskmate.project.system_settings.ui.tool_bar

import androidx.appcompat.widget.Toolbar
import safronov.apps.domain.model.task_category.TaskCategory
import safronov.apps.taskmate.R

interface HomePageToolBarService {

    fun setOnMenuItemClickListener(
        toolBar: Toolbar,
        pinTask: () -> Unit,
        chooseTaskCategory: () -> Unit,
        saveTask: () -> Unit
    )
    fun changePinTaskIconByParam(
        toolBar: Toolbar,
        isPinned: Boolean
    )
    fun changeTaskCategoryIcon(
        toolBar: Toolbar,
        taskCategory: TaskCategory?,
        defaultTaskCategoryIcon: Int = R.drawable.ic_multi_color
    )

    class Base: HomePageToolBarService {
        override fun setOnMenuItemClickListener(
            toolBar: Toolbar,
            pinTask: () -> Unit,
            chooseTaskCategory: () -> Unit,
            saveTask: () -> Unit
        ) {
            toolBar.setOnMenuItemClickListener {
                var handled = false
                if (it.itemId == R.id.pin_task) {
                    pinTask.invoke()
                    handled = true
                } else if (it.itemId == R.id.choose_category) {
                    chooseTaskCategory.invoke()
                    handled = true
                } else if (it.itemId == R.id.save_task) {
                    saveTask.invoke()
                    handled = true
                }
                handled
            }
        }

        override fun changePinTaskIconByParam(toolBar: Toolbar, isPinned: Boolean) {
            if (isPinned) {
                toolBar.menu.findItem(R.id.pin_task).setIcon(R.drawable.ic_pinned)
            } else {
                toolBar.menu.findItem(R.id.pin_task).setIcon(R.drawable.ic_not_pinned)
            }
        }

        override fun changeTaskCategoryIcon(
            toolBar: Toolbar,
            taskCategory: TaskCategory?,
            defaultTaskCategoryIcon: Int
        ) {
            toolBar.menu.findItem(R.id.choose_category).setIcon(taskCategory?.icon ?: defaultTaskCategoryIcon)
        }

    }

}