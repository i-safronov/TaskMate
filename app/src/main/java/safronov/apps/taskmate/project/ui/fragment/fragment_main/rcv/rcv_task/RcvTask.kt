package safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.rcv_task

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import safronov.apps.domain.model.task.Task
import safronov.apps.taskmate.R
import safronov.apps.taskmate.databinding.RcvTaskListBinding
import safronov.apps.taskmate.databinding.RcvTaskTextBinding
import safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.rcv_task.task_list_items.RcvTaskListItemSmall
import java.lang.IllegalStateException

interface RcvTaskInt {
    fun onTaskClick(task: Task)
    fun onTaskSelectionMode()
    fun selectionTasksChanged(list: List<Task>)
}

class RcvTask(
    private val rcvTaskInt: RcvTaskInt
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var tasks = emptyList<Task>()
    private var isSelectionMode = false
    private var selectedTasks = mutableListOf<Task>()

    inner class TaskTextViewHolder(
        private val binding: RcvTaskTextBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(taskText: Task.TaskText, position: Int) {
            binding.tvTitle.text = taskText.title
            binding.tvDate.text = taskText.date
            binding.tvMainText.text = taskText.text
            if (taskText.isPinned == true) {
                binding.imgIsPinned.visibility = View.VISIBLE
            } else {
                binding.imgIsPinned.visibility = View.GONE
            }
            setSelectionModeToCurrentTask(taskText, position, itemView = itemView, rootView = binding.root)
        }

    }

    inner class TaskListViewHolder(
        private val binding: RcvTaskListBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(taskList: Task.TaskList, position: Int) {
            val rcvTaskListItems = RcvTaskListItemSmall()
            binding.rcvTasks.layoutManager = LinearLayoutManager(binding.root.context)
            binding.rcvTasks.adapter = rcvTaskListItems
            binding.tvTitle.text = taskList.title
            binding.tvDate.text = taskList.date
            rcvTaskListItems.submitList(taskList.list)
            if (taskList.isPinned == true) {
                binding.imgIsPinned.visibility = View.VISIBLE
            } else {
                binding.imgIsPinned.visibility = View.GONE
            }
            setSelectionModeToCurrentTask(taskList, position, itemView = itemView, rootView = binding.root)
        }
    }

    private fun setSelectionModeToCurrentTask(
        task: Task,
        position: Int,
        itemView: View,
        rootView: View
    ) {
        itemView.setOnClickListener {
            if (isSelectionMode) {
                if (selectedTasks.contains(task)) {
                    selectedTasks.remove(task)
                    rootView.setBackgroundDrawable(AppCompatResources.getDrawable(rootView.context, R.drawable.back_shape_with_ripple))
                } else {
                    selectedTasks.add(task)
                    rootView.setBackgroundDrawable(AppCompatResources.getDrawable(rootView.context, R.drawable.selected_task))
                }
                rcvTaskInt.selectionTasksChanged(selectedTasks)
            } else {
                rootView.setBackgroundDrawable(AppCompatResources.getDrawable(rootView.context, R.drawable.back_shape_with_ripple))
                rcvTaskInt.onTaskClick(task)
            }
        }
        itemView.setOnLongClickListener {
            if (!isSelectionMode) {
                isSelectionMode = true
                selectedTasks.add(task)
                rcvTaskInt.onTaskSelectionMode()
                rcvTaskInt.selectionTasksChanged(selectedTasks)
                notifyItemChanged(position)
            }
            true
        }

        if (selectedTasks.contains(task)) {
            rootView.setBackgroundDrawable(AppCompatResources.getDrawable(rootView.context, R.drawable.selected_task))
        } else {
            rootView.setBackgroundDrawable(AppCompatResources.getDrawable(rootView.context, R.drawable.back_shape_with_ripple))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        if (viewType == TASK_TEXT_VIEW_TYPE) {
            return TaskTextViewHolder(RcvTaskTextBinding.inflate(inflater, parent, false))
        } else if (viewType == TASK_LIST_VIEW_TYPE) {
            return TaskListViewHolder(RcvTaskListBinding.inflate(inflater, parent, false))
        }
        throw IllegalStateException("could not find view holder")
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentTask = tasks[holder.absoluteAdapterPosition]
        if (holder is TaskTextViewHolder) {
            holder.bind(currentTask as Task.TaskText, holder.absoluteAdapterPosition)
        } else if (holder is TaskListViewHolder) {
            holder.bind(currentTask as Task.TaskList, holder.absoluteAdapterPosition)
        } else {
            throw IllegalStateException("could not find view holder")
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentTask = tasks[position]
        return if (currentTask is Task.TaskText) {
            TASK_TEXT_VIEW_TYPE
        } else if (currentTask is Task.TaskList) {
            TASK_LIST_VIEW_TYPE
        } else {
            throw IllegalStateException("could not find view type")
        }
    }

    fun submitList(list: List<Task>) {
        tasks = list
        notifyDataSetChanged()
    }

    fun clearSelectionMode() {
        isSelectionMode = false
        selectedTasks.clear()
        notifyDataSetChanged()
    }

    fun isSelectionMode() = isSelectionMode

    fun getSelectedTasks() = selectedTasks.toList()

    companion object {
        private const val TASK_TEXT_VIEW_TYPE = 0
        private const val TASK_LIST_VIEW_TYPE = 1
    }

}