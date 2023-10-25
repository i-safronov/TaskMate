package safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.task_type

import android.content.Context
import safronov.apps.domain.model.task.Task
import safronov.apps.taskmate.R
import safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.model.RcvTaskTypeModel

interface AllTaskTypes {

    fun getTaskTypes(): List<RcvTaskTypeModel>

    class Base(
        private val context: Context
    ): AllTaskTypes {

        override fun getTaskTypes(): List<RcvTaskTypeModel> {
            return listOf(
                RcvTaskTypeModel(
                    title = context.getString(R.string.task_type_text),
                    img = R.drawable.ic_text_task_type,
                    taskType = Task.TaskType.Text
                ),
                RcvTaskTypeModel(
                    title = context.getString(R.string.task_type_list),
                    img = R.drawable.ic_list_task_type,
                    taskType = Task.TaskType.List
                )
            )
        }

    }

}