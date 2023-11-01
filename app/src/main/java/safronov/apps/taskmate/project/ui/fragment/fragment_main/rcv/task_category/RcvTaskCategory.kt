package safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.task_category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import safronov.apps.domain.model.task_category.TaskCategory
import safronov.apps.taskmate.R
import safronov.apps.taskmate.databinding.RcvItemTaskCategoryBinding

class RcvTaskCategory: RecyclerView.Adapter<RcvTaskCategory.TaskCategoryViewHolder>() {

    private var categories = mutableListOf<TaskCategory>()
    private var selectedTaskCategory: TaskCategory? = null

    inner class TaskCategoryViewHolder(
        private val binding: RcvItemTaskCategoryBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: TaskCategory) {
            binding.linearLayout.setBackgroundColor(item.backgroundColor ?: R.color.back)
            binding.img.setImageResource(item.icon ?: R.drawable.ic_block)
            binding.tvTitle.text = item.categoryName
            binding.imgIsPinned.visibility =
                if (item.id == selectedTaskCategory?.id) View.VISIBLE else View.GONE
            itemView.setOnClickListener {
                saveSelectedItem(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskCategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RcvItemTaskCategoryBinding.inflate(inflater, parent, false)
        return TaskCategoryViewHolder(binding = binding)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: TaskCategoryViewHolder, position: Int) {
        holder.bindView(categories[position])
    }

    fun setSelectedTaskCategory(item: TaskCategory) {
        saveSelectedItem(item)
    }

    private fun saveSelectedItem(item: TaskCategory) {
        selectedTaskCategory = item
        notifyDataSetChanged()
    }

}