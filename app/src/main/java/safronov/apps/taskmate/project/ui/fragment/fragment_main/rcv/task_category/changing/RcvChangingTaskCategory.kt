package safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.task_category.changing

import android.text.Editable
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.recyclerview.widget.RecyclerView
import safronov.apps.domain.model.task_category.TaskCategory
import safronov.apps.taskmate.R
import safronov.apps.taskmate.databinding.RcvItemChangingTaskCategoryBinding


interface RcvChangingTaskCategoryInt {
    fun onTaskCategoryClick(taskCategory: TaskCategory)
}

class RcvChangingTaskCategory(
    private val rcvChangingTaskCategoryInt: RcvChangingTaskCategoryInt
): RecyclerView.Adapter<RcvChangingTaskCategory.TaskCategoryViewHolder>() {

    private var categories = listOf<TaskCategory>()
    private var selectedTaskCategory: TaskCategory? = null
    private var isChangingMode = false

    inner class TaskCategoryViewHolder(
        private val binding: RcvItemChangingTaskCategoryBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: TaskCategory) {
            val textWatcher = object: android.text.TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    item.categoryName = p0.toString()
                }
            }
            if (isChangingMode) {
                val view: View = binding.tvTitle
                val layoutParams: LayoutParams = view.layoutParams
                layoutParams.width = LayoutParams.MATCH_PARENT
                view.layoutParams = layoutParams
                binding.tvTitle.addTextChangedListener(textWatcher)
                binding.tvTitle.inputType = InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE
                binding.tvTitle.isEnabled = true
                binding.tvTitle.backgroundTintList = binding.root.context.resources.getColorStateList(
                    R.color.hint)
            } else {
                val view: View = binding.tvTitle
                val layoutParams: LayoutParams = view.layoutParams
                layoutParams.width = LayoutParams.WRAP_CONTENT
                view.layoutParams = layoutParams
                binding.tvTitle.removeTextChangedListener(textWatcher)
                binding.tvTitle.inputType = InputType.TYPE_NULL
                binding.tvTitle.isEnabled = false
                binding.tvTitle.backgroundTintList = binding.root.context.resources.getColorStateList(android.R.color.transparent)
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