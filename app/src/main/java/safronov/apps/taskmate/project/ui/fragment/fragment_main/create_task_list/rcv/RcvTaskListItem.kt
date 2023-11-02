package safronov.apps.taskmate.project.ui.fragment.fragment_main.create_task_list.rcv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import safronov.apps.domain.model.task.Task
import safronov.apps.taskmate.databinding.RcvTaskListItemMainBinding
import safronov.apps.taskmate.project.system_settings.ui.text_watcher.TextWatcher

interface RcvTaskListItemInt {
    fun taskListItemsChanged(list: List<Task.TaskListItem>)
}

class RcvTaskListItem(
    private val textWatcher: TextWatcher,
    private val rcvTaskListItemInt: RcvTaskListItemInt
): RecyclerView.Adapter<RcvTaskListItem.TaskListItemViewHolder>() {

    private var taskListItems = mutableListOf<Task.TaskListItem>()

    inner class TaskListItemViewHolder(
        private val binding: RcvTaskListItemMainBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: Task.TaskListItem, position: Int) {
            binding.tvTitle.setText(item.title)
            binding.checkBoxTaskWasFinished.isChecked = item.isChecked == true
            binding.tvTitle.setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    binding.btnCancel.visibility = View.VISIBLE
                    binding.btnCancel.setOnClickListener {
                        taskListItems.removeAt(position)
                        rcvTaskListItemInt.taskListItemsChanged(taskListItems)
                        notifyDataSetChanged()
                    }
                } else {
                    binding.btnCancel.visibility = View.GONE
                    binding.btnCancel.setOnClickListener(null)
                }
            }
            textWatcher.addTextWatcherToView(binding.tvTitle, afterTextChanged = {
                item.title = it
                rcvTaskListItemInt.taskListItemsChanged(taskListItems)
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RcvTaskListItemMainBinding.inflate(inflater, parent, false)
        return TaskListItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return taskListItems.size
    }

    override fun onBindViewHolder(holder: TaskListItemViewHolder, position: Int) {
        holder.bindView(item = taskListItems[position], position)
    }

    fun submitList(list: MutableList<Task.TaskListItem>) {
        taskListItems = list
        notifyDataSetChanged()
    }

    fun addTaskListItem(item: Task.TaskListItem) {
        taskListItems.add(item)
        notifyDataSetChanged()
    }

}