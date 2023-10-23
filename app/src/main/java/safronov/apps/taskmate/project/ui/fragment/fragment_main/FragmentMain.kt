package safronov.apps.taskmate.project.ui.fragment.fragment_main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import safronov.apps.taskmate.R
import safronov.apps.taskmate.databinding.FragmentMainBinding
import safronov.apps.taskmate.project.system_settings.extension.fragment.goToFragmentError
import safronov.apps.taskmate.project.system_settings.fragment.FragmentBase

class FragmentMain : FragmentBase() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun createUI(inflater: LayoutInflater, container: ViewGroup?): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setup() {

    }

    override fun uiCreated(view: View, savedInstanceState: Bundle?) {
        animateFbAddTask()
    }

    override fun handeException(e: RuntimeException) {
        goToFragmentError(e.message.toString())
    }

    override fun removeUI() {
        _binding = null
    }

    private fun animateFbAddTask() {
        binding.animateFbAddTask.startRippleAnimation()
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentMain()
    }

}