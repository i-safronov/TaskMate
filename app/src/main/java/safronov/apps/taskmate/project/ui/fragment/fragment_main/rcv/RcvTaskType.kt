package safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import safronov.apps.taskmate.databinding.RcvTaskTypeBinding
import safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.model.RcvTaskTypeModel

class RcvTaskType(
    private val rcvTaskTypeInt: RcvTaskTypeInt
): ListAdapter<RcvTaskTypeModel, RcvTaskType.TaskTypeViewHolder>(TaskTypeDiffUtil()) {

    inner class TaskTypeViewHolder(
        private val binding: RcvTaskTypeBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bindView(taskType: RcvTaskTypeModel) {
            binding.tvTitle.text = taskType.title
            binding.img.setImageResource(taskType.img)
            itemView.setOnClickListener {
                rcvTaskTypeInt.onTaskTypeClick(taskType = taskType)
            }
        }
    }

    class TaskTypeDiffUtil: DiffUtil.ItemCallback<RcvTaskTypeModel>() {
        override fun areItemsTheSame(
            oldItem: RcvTaskTypeModel,
            newItem: RcvTaskTypeModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: RcvTaskTypeModel,
            newItem: RcvTaskTypeModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskTypeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RcvTaskTypeBinding.inflate(inflater, parent, false)
        return TaskTypeViewHolder(binding = binding)
    }

    override fun onBindViewHolder(holder: TaskTypeViewHolder, position: Int) {
        holder.bindView(taskType = currentList[holder.absoluteAdapterPosition])
    }

}