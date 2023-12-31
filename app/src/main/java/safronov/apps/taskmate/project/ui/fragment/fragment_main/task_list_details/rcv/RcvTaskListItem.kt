package safronov.apps.taskmate.project.ui.fragment.fragment_main.task_list_details.rcv

import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import safronov.apps.domain.model.task.Task
import safronov.apps.taskmate.databinding.RcvTaskListItemMainBinding

interface RcvTaskListItemInt {
    fun taskListItemsChanged(list: List<Task.TaskListItem>)
}

class RcvTaskListItem(
    private val rcvTaskListItemInt: RcvTaskListItemInt
): RecyclerView.Adapter<RcvTaskListItem.TaskListItemViewHolder>() {

    private var taskListItems = mutableListOf<Task.TaskListItem>()

    inner class TaskListItemViewHolder(
        private val binding: RcvTaskListItemMainBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: Task.TaskListItem, position: Int) {
            binding.tvTitle.setText(item.title)
            binding.checkBoxTaskWasFinished.isChecked = item.isChecked == true
            val textWatcher = object: android.text.TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    item.title = p0.toString()
                    rcvTaskListItemInt.taskListItemsChanged(taskListItems)
                }
            }
            binding.tvTitle.setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    binding.tvTitle.addTextChangedListener(textWatcher)
                    binding.btnCancel.visibility = View.VISIBLE
                    binding.btnCancel.setOnClickListener {
                        taskListItems.removeAt(position)
                        updated()
                    }
                } else {
                    binding.tvTitle.removeTextChangedListener(textWatcher)
                    binding.btnCancel.visibility = View.GONE
                }
            }
            binding.checkBoxTaskWasFinished.setOnClickListener {
                item.isChecked = binding.checkBoxTaskWasFinished.isChecked
                rcvTaskListItemInt.taskListItemsChanged(taskListItems)
            }
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
        holder.bindView(item = taskListItems[holder.absoluteAdapterPosition], holder.absoluteAdapterPosition)
    }

    fun submitList(items: MutableList<Task.TaskListItem>) {
        taskListItems = items
        updated()
    }

    fun addTaskListItem(item: Task.TaskListItem) {
        taskListItems.add(item)
        updated()
    }

    private fun updated() {
        rcvTaskListItemInt.taskListItemsChanged(taskListItems)
        notifyDataSetChanged()
    }

}