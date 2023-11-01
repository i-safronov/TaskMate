package safronov.apps.taskmate.project.system_settings.ui.tool_bar

import android.widget.Toolbar
import safronov.apps.taskmate.R

interface HomePageToolBarService {

    fun setOnMenuItemClickListener(
        toolBar: Toolbar,
        pinTask: () -> Unit,
        chooseTaskCategory: () -> Unit
    )
    fun changePinTaskIconByParam(
        toolBar: Toolbar,
        isPinned: Boolean
    )

    class Base: HomePageToolBarService {
        override fun setOnMenuItemClickListener(
            toolBar: Toolbar,
            pinTask: () -> Unit,
            chooseTaskCategory: () -> Unit
        ) {
            toolBar.setOnMenuItemClickListener {
                var handled = false
                if (it.itemId == R.id.pin_task) {
                    pinTask.invoke()
                    handled = true
                } else if (it.itemId == R.id.choose_category) {
                    chooseTaskCategory.invoke()
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
    }

}