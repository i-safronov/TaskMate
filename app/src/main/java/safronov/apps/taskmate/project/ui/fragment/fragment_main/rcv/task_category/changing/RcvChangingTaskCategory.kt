package safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.task_category.changing

import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import safronov.apps.domain.model.task_category.TaskCategory
import safronov.apps.taskmate.R
import safronov.apps.taskmate.databinding.RcvItemChangingTaskCategoryBinding
import safronov.apps.taskmate.project.system_settings.ui.text_watcher.TextWatcher

interface RcvChangingTaskCategoryInt {
    fun onTaskCategoryClick(taskCategory: TaskCategory)
}

class RcvChangingTaskCategory(
    private val textWatcher: TextWatcher,
    private val rcvChangingTaskCategoryInt: RcvChangingTaskCategoryInt
): RecyclerView.Adapter<RcvChangingTaskCategory.TaskCategoryViewHolder>() {

    private var categories = listOf<TaskCategory>()
    private var selectedTaskCategory: TaskCategory? = null
    private var isChangingMode = false

    inner class TaskCategoryViewHolder(
        private val binding: RcvItemChangingTaskCategoryBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: TaskCategory) {
            if (isChangingMode) {
                textWatcher.addTextWatcherToView(binding.tvTitle, afterTextChanged = {
                    item.categoryName = it
                })
                binding.tvTitle.inputType = InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE
            } else {
                binding.tvTitle.inputType = InputType.TYPE_NULL
            }
            binding.img.setImageResource(item.icon ?: R.drawable.ic_block)
            binding.tvTitle.setText(item.categoryName)
            binding.imgIsPinned.visibility =
                if (item.id == selectedTaskCategory?.id) View.VISIBLE else View.GONE
            itemView.setOnClickListener {
                saveSelectedItem(item)
                rcvChangingTaskCategoryInt.onTaskCategoryClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskCategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RcvItemChangingTaskCategoryBinding.inflate(inflater, parent, false)
        return TaskCategoryViewHolder(binding = binding)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: TaskCategoryViewHolder, position: Int) {
        holder.bindView(categories[position])
    }

    fun submitList(list: List<TaskCategory>) {
        categories = list
        notifyDataSetChanged()
    }

    fun setSelectedTaskCategory(item: TaskCategory) {
        saveSelectedItem(item)
    }

    fun setChangingMode() {
        isChangingMode = true
        notifyDataSetChanged()
    }

    private fun saveSelectedItem(item: TaskCategory) {
        selectedTaskCategory = item
        notifyDataSetChanged()
    }

}