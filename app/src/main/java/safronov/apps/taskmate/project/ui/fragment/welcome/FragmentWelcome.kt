package safronov.apps.taskmate.project.ui.fragment.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import safronov.apps.taskmate.databinding.FragmentWelcomeBinding
import safronov.apps.taskmate.project.system_settings.extension.fragment.goToFragmentError
import safronov.apps.taskmate.project.system_settings.fragment.FragmentBase

class FragmentWelcome : FragmentBase() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    override fun createUI(inflater: LayoutInflater, container: ViewGroup?): View? {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setup() {  }

    override fun uiCreated(view: View, savedInstanceState: Bundle?) {

    }

    override fun handeException(e: RuntimeException) {
        goToFragmentError(e.message.toString())
    }

    override fun removeUI() {
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentWelcome()
    }

}