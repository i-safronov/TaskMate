package safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.rcv_task.task_list_items

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import safronov.apps.domain.model.task.Task
import safronov.apps.taskmate.databinding.RcvTaskListItemSmallBinding

class RcvTaskListItemSmall: ListAdapter<Task.TaskListItem, RcvTaskListItemSmall.TaskListItemsViewHolder>(
    TaskListItemsDiffUtil()
) {

    class TaskListItemsViewHolder(
        private val binding: RcvTaskListItemSmallBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Task.TaskListItem) {
            binding.tvTitle.text = item.title
            binding.checkBoxTaskWasFinished.isChecked = item.isChecked == true
        }
    }

    class TaskListItemsDiffUtil: DiffUtil.ItemCallback<Task.TaskListItem>() {
        override fun areItemsTheSame(
            oldItem: Task.TaskListItem,
            newItem: Task.TaskListItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Task.TaskListItem,
            newItem: Task.TaskListItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListItemsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RcvTaskListItemSmallBinding.inflate(inflater, parent, false)
        return TaskListItemsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskListItemsViewHolder, position: Int) {
        holder.bind(currentList[holder.absoluteAdapterPosition])
    }

}