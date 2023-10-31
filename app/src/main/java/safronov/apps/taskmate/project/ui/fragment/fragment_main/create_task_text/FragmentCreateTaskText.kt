package safronov.apps.taskmate.project.ui.fragment.fragment_main.create_task_text

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import safronov.apps.taskmate.R
import safronov.apps.taskmate.databinding.FragmentCreateTaskTextBinding
import safronov.apps.taskmate.project.system_settings.extension.fragment.goToFragmentErrorFromHomePage
import safronov.apps.taskmate.project.system_settings.extension.fragment.removeMenuFromHomePageToolBar
import safronov.apps.taskmate.project.system_settings.extension.fragment.inflateMenuOnHomePageToolBar
import safronov.apps.taskmate.project.system_settings.extension.fragment.requireAppComponent
import safronov.apps.taskmate.project.system_settings.fragment.FragmentBase
import safronov.apps.taskmate.project.ui.fragment.fragment_main.create_task_text.view_model.FragmentCreateTaskTextViewModel
import safronov.apps.taskmate.project.ui.fragment.fragment_main.create_task_text.view_model.FragmentCreateTaskTextViewModelFactory
import javax.inject.Inject

class FragmentCreateTaskText : FragmentBase() {

    private var _binding: FragmentCreateTaskTextBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var textWatcher: safronov.apps.taskmate.project.system_settings.ui.text_watcher.TextWatcher

    @Inject
    lateinit var fragmentCreateTaskTextViewModelFactory: FragmentCreateTaskTextViewModelFactory
    private var fragmentCreateTaskTextViewModel: FragmentCreateTaskTextViewModel? = null

    override fun createUI(inflater: LayoutInflater, container: ViewGroup?): View? {
        _binding = FragmentCreateTaskTextBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setup() {
        requireAppComponent().inject(this)
        fragmentCreateTaskTextViewModel = ViewModelProvider(this, fragmentCreateTaskTextViewModelFactory)
            .get(FragmentCreateTaskTextViewModel::class.java)
    }

    override fun uiCreated(view: View, savedInstanceState: Bundle?) {
        addTextWatcherToEdtvTitle()
        addTextWatcherToEdtvText()
    }

    private fun addTextWatcherToEdtvTitle() {
        textWatcher.addTextWatcherToView(binding.edtvTitle, afterTextChanged = {
            fragmentCreateTaskTextViewModel?.saveCurrentTaskTitle(it)
        })
    }

    private fun addTextWatcherToEdtvText() {
        textWatcher.addTextWatcherToView(binding.edtvText, afterTextChanged = {
            fragmentCreateTaskTextViewModel?.saveCurrentTaskText(it)
        })
    }

    override fun onStart() {
        super.onStart()
        inflateMenuOnHomePageToolBar(menuId = R.menu.fragment_create_task_toolbar_menu)
    }

    override fun onStop() {
        super.onStop()
        removeMenuFromHomePageToolBar()
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