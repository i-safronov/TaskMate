package safronov.apps.taskmate.project.ui.fragment.start

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import safronov.apps.taskmate.R
import safronov.apps.taskmate.databinding.FragmentStartBinding
import safronov.apps.taskmate.project.system_settings.extension.fragment.goToFragmentError
import safronov.apps.taskmate.project.system_settings.extension.fragment.requireAppComponent
import safronov.apps.taskmate.project.system_settings.fragment.FragmentBase
import safronov.apps.taskmate.project.ui.fragment.start.view_model.FragmentStartViewModel
import safronov.apps.taskmate.project.ui.fragment.start.view_model.FragmentStartViewModelFactory
import javax.inject.Inject

class FragmentStart : FragmentBase() {

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var fragmentStartViewModelFactory: FragmentStartViewModelFactory
    private var fragmentStartViewModel: FragmentStartViewModel? = null

    override fun starting() {
        setup()
    }

    override fun createUI(inflater: LayoutInflater, container: ViewGroup?): View? {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun prepareArguments() {  }

    override fun uiCreated(view: View, savedInstanceState: Bundle?) {
    }

    override fun handeException(e: RuntimeException) {
        goToFragmentError(e.message.toString())
    }

    override fun removeUI() {
        _binding = null
    }

    private fun setup() {
        requireAppComponent().inject(this)
        fragmentStartViewModel = ViewModelProvider(this, fragmentStartViewModelFactory)
            .get(FragmentStartViewModel::class.java)
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentStart()
    }

}