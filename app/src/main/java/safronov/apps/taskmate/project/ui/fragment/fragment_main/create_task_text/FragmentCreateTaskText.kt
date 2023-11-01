package safronov.apps.taskmate.project.ui.fragment.fragment_main.create_task_text

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import safronov.apps.taskmate.R
import safronov.apps.taskmate.databinding.FragmentCreateTaskTextBinding
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList
import safronov.apps.taskmate.project.system_settings.extension.fragment.goToFragmentErrorFromHomePage
import safronov.apps.taskmate.project.system_settings.extension.fragment.removeMenuFromHomePageToolBar
import safronov.apps.taskmate.project.system_settings.extension.fragment.inflateMenuOnHomePageToolBar
import safronov.apps.taskmate.project.system_settings.extension.fragment.requireAppComponent
import safronov.apps.taskmate.project.system_settings.extension.fragment.requireHomePageToolBar
import safronov.apps.taskmate.project.system_settings.fragment.FragmentBase
import safronov.apps.taskmate.project.system_settings.ui.tool_bar.HomePageToolBarService
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

    @Inject
    lateinit var dispatchersList: DispatchersList

    @Inject
    lateinit var homePageToolBarService: HomePageToolBarService

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
        fragmentCreateTaskTextViewModel?.loadDefaultTaskCategory()
        addItemMenuClickListenerOnHomePageToolBar()
        addTextWatcherToEdtvTitle()
        addTextWatcherToEdtvText()
    }

    private fun addItemMenuClickListenerOnHomePageToolBar() {
        homePageToolBarService.setOnMenuItemClickListener(
            toolBar = requireHomePageToolBar(),
            pinTask = {
                fragmentCreateTaskTextViewModel?.pinCurrentTask()
            },
            chooseTaskCategory = {
                //TODO something
            }
        )
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
        observeTaskPin()
        observeTaskCategory()
    }

    private fun observeTaskPin() = viewLifecycleOwner.lifecycleScope.launch(dispatchersList.ui()) {
        fragmentCreateTaskTextViewModel?.getIsTaskPin()?.collect {
            homePageToolBarService.changePinTaskIconByParam(toolBar = requireHomePageToolBar(), isPinned = it)
        }
    }

    private fun observeTaskCategory() = viewLifecycleOwner.lifecycleScope.launch(dispatchersList.ui()) {
        fragmentCreateTaskTextViewModel?.getTaskCategory()?.collect {
            homePageToolBarService.changeTaskCategoryIcon(toolBar = requireHomePageToolBar(), taskCategory = it)
        }
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