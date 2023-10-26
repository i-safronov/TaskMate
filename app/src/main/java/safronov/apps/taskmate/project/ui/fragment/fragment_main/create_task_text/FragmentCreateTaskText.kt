package safronov.apps.taskmate.project.ui.fragment.fragment_main.create_task_text

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import safronov.apps.taskmate.R
import safronov.apps.taskmate.databinding.FragmentCreateTaskTextBinding
import safronov.apps.taskmate.project.system_settings.extension.fragment.goToFragmentErrorFromHomePage
import safronov.apps.taskmate.project.system_settings.extension.fragment.inflateMenuOnHomePageToolBar
import safronov.apps.taskmate.project.system_settings.extension.fragment.removeMenuFromHomePageToolBar
import safronov.apps.taskmate.project.system_settings.extension.fragment.requireHomePageToolBar
import safronov.apps.taskmate.project.system_settings.fragment.FragmentBase

class FragmentCreateTaskText : FragmentBase() {

    private var _binding: FragmentCreateTaskTextBinding? = null
    private val binding get() = _binding!!

    override fun createUI(inflater: LayoutInflater, container: ViewGroup?): View? {
        _binding = FragmentCreateTaskTextBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setup() {

    }

    override fun uiCreated(view: View, savedInstanceState: Bundle?) {

    }

    override fun handeException(e: RuntimeException) {
        goToFragmentErrorFromHomePage(e.message.toString())
    }

    override fun removeUI() {
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentCreateTaskText()
    }

}