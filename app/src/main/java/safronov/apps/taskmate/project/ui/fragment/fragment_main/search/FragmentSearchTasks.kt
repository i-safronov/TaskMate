package safronov.apps.taskmate.project.ui.fragment.fragment_main.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import safronov.apps.taskmate.databinding.FragmentSearchTasksBinding
import safronov.apps.taskmate.project.system_settings.extension.fragment.focusOnViewAndShowKeyboard
import safronov.apps.taskmate.project.system_settings.fragment.FragmentBase


class FragmentSearchTasks : FragmentBase() {

    private var _binding: FragmentSearchTasksBinding? = null
    private val binding get() = _binding!!

    override fun createUI(inflater: LayoutInflater, container: ViewGroup?): View? {
        _binding = FragmentSearchTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setup() {

    }

    override fun uiCreated(view: View, savedInstanceState: Bundle?) {
        focusOnViewAndShowKeyboard(binding.includedSearchView.root)
    }

    override fun handeException(e: RuntimeException) {

    }

    override fun removeUI() {
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentSearchTasks()
    }

}