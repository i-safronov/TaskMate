package safronov.apps.taskmate.project.ui.fragment.fragment_main

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.widget.ToolbarWidgetWrapper
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
        setupToolBar()
        animateFbAddTask()
    }

    override fun handeException(e: RuntimeException) {
        goToFragmentError(e.message.toString())
    }

    override fun removeUI() {
        _binding = null
    }

    private fun setupToolBar() {
        val toolBar = requireActivity().findViewById<Toolbar>(R.id.included_fragment_main_toolbar)
        toolBar.inflateMenu(R.menu.fragment_main_toolbar_menu)
    }

    private fun animateFbAddTask() {
        binding.animateFbAddTask.startRippleAnimation()
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentMain()
    }

}