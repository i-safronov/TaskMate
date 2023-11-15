package safronov.apps.taskmate.project.ui.fragment.fragment_main.task_text_details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import safronov.apps.domain.model.task.Task
import safronov.apps.domain.model.task_category.TaskCategory
import safronov.apps.taskmate.R
import safronov.apps.taskmate.databinding.BottomSheetChooseItemBinding
import safronov.apps.taskmate.databinding.FragmentTaskTextDetailsBinding
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList
import safronov.apps.taskmate.project.system_settings.extension.fragment.goToFragmentErrorFromHomePage
import safronov.apps.taskmate.project.system_settings.extension.fragment.removeMenuFromHomePageToolBar
import safronov.apps.taskmate.project.system_settings.extension.fragment.inflateMenuOnHomePageToolBar
import safronov.apps.taskmate.project.system_settings.extension.fragment.requireAppComponent
import safronov.apps.taskmate.project.system_settings.extension.fragment.requireHomePageToolBar
import safronov.apps.taskmate.project.system_settings.fragment.FragmentBase
import safronov.apps.taskmate.project.system_settings.ui.bottom_sheet.BottomSheet
import safronov.apps.taskmate.project.system_settings.ui.rcv.RecyclerViewBuilder
import safronov.apps.taskmate.project.system_settings.ui.text_watcher.TextWatcher
import safronov.apps.taskmate.project.system_settings.ui.tool_bar.HomePageToolBarService
import safronov.apps.taskmate.project.ui.fragment.fragment_main.task_text_details.view_model.FragmentTaskTextDetailsViewModel
import safronov.apps.taskmate.project.ui.fragment.fragment_main.task_text_details.view_model.FragmentTaskTextDetailsViewModelFactory
import safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.task_category.RcvTaskCategory
import safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.task_category.RcvTaskCategoryInt
import javax.inject.Inject

class FragmentTaskTextDetails : FragmentBase(), RcvTaskCategoryInt {

    private var _binding: FragmentTaskTextDetailsBinding? = null
    private val binding get() = _binding!!
    private val rcvTaskCategory = RcvTaskCategory(this)
    private var forWhatThisFragment = FOR_CREATE_NEW_TASK
    private var existingTaskText: Task.TaskText? = null

    @Inject
    lateinit var textWatcher: TextWatcher

    @Inject
    lateinit var bottomSheet: BottomSheet

    @Inject
    lateinit var fragmentTaskTextDetailsViewModelFactory: FragmentTaskTextDetailsViewModelFactory
    private var fragmentTaskTextDetailsViewModel: FragmentTaskTextDetailsViewModel? = null

    @Inject
    lateinit var dispatchersList: DispatchersList

    @Inject
    lateinit var homePageToolBarService: HomePageToolBarService

    @Inject
    lateinit var recyclerViewBuilder: RecyclerViewBuilder

    override fun createUI(inflater: LayoutInflater, container: ViewGroup?): View? {
        _binding = FragmentTaskTextDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setup() {
        requireAppComponent().inject(this)
        fragmentTaskTextDetailsViewModel = ViewModelProvider(this, fragmentTaskTextDetailsViewModelFactory)
            .get(FragmentTaskTextDetailsViewModel::class.java)
        forWhatThisFragment = requireArguments().getString(THIS_FRAGMENT_FOR, FOR_CREATE_NEW_TASK)
        if (forWhatThisFragment == FOR_UPDATE_EXISTING_TASK) {
            setupDefaultValues()
        }
    }

    private fun setupDefaultValues() {
        existingTaskText = requireArguments().getSerializable(EXISTING_TASK_TEXT) as Task.TaskText
        fragmentTaskTextDetailsViewModel?.prepareToChangeExistingTask(existingTaskText!!)
        binding.edtvTitle.setText(existingTaskText?.title)
        binding.edtvText.setText(existingTaskText?.text)
    }

    //TODO refactor this code
    override fun uiCreated(view: View, savedInstanceState: Bundle?) {
        fragmentTaskTextDetailsViewModel?.loadTaskCategories()
        fragmentTaskTextDetailsViewModel?.loadDefaultTaskCategory()
        addItemMenuClickListenerOnHomePageToolBar()
        addTextWatcherToEdtvTitle()
        addTextWatcherToEdtvText()
        observeTaskSaved()
        observeWasException()
        observeTaskCategories()
        binding.tvDate.text = fragmentTaskTextDetailsViewModel?.getCurrentTime()
    }

    private fun addItemMenuClickListenerOnHomePageToolBar() {
        homePageToolBarService.setOnMenuItemClickListener(
            toolBar = requireHomePageToolBar(),
            pinTask = {
                fragmentTaskTextDetailsViewModel?.pinCurrentTask()
            },
            chooseTaskCategory = {
                val bottomView = BottomSheetChooseItemBinding.inflate(layoutInflater)
                bottomView.tvTitle.text = getString(R.string.choose)
                recyclerViewBuilder.setupRcv(bottomView.rcvTypes, rcvTaskCategory, LinearLayoutManager(requireContext()))
                bottomSheet.showBottomSheet(activityContext = requireContext(), view = bottomView.root)
            }, saveTask = {
                fragmentTaskTextDetailsViewModel?.saveCurrentTask()
            }
        )
    }

    private fun addTextWatcherToEdtvTitle() {
        textWatcher.addTextWatcherToView(binding.edtvTitle, afterTextChanged = {
            fragmentTaskTextDetailsViewModel?.saveCurrentTaskTitle(it)
        })
    }

    private fun addTextWatcherToEdtvText() {
        textWatcher.addTextWatcherToView(binding.edtvText, afterTextChanged = {
            fragmentTaskTextDetailsViewModel?.saveCurrentTaskText(it)
        })
    }

    private fun observeTaskSaved() = viewLifecycleOwner.lifecycleScope.launch(dispatchersList.ui()) {
        fragmentTaskTextDetailsViewModel?.getTaskSaved()?.collect {
            if (it == true) {
                Toast.makeText(requireContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeWasException() = viewLifecycleOwner.lifecycleScope.launch(dispatchersList.ui()) {
        fragmentTaskTextDetailsViewModel?.isWasException()?.collect {
            if (it != null) {
                handeException(it)
            }
        }
    }

    private fun observeTaskCategories() = viewLifecycleOwner.lifecycleScope.launch(dispatchersList.ui()) {
        fragmentTaskTextDetailsViewModel?.getCategories()?.collect {
            Log.d("sfrLog", "Data: ${it}")
            rcvTaskCategory.submitList(it)
        }
    }

    override fun onStart() {
        super.onStart()
        try {
            inflateMenuOnHomePageToolBar(menuId = R.menu.fragment_create_task_toolbar_menu)
            observeTaskPin()
            observeTaskCategory()
        } catch (e: RuntimeException) {
            handeException(e)
        }
    }

    private fun observeTaskPin() = viewLifecycleOwner.lifecycleScope.launch(dispatchersList.ui()) {
        fragmentTaskTextDetailsViewModel?.getIsTaskPin()?.collect {
            homePageToolBarService.changePinTaskIconByParam(toolBar = requireHomePageToolBar(), isPinned = it)
        }
    }

    private fun observeTaskCategory() = viewLifecycleOwner.lifecycleScope.launch(dispatchersList.ui()) {
        fragmentTaskTextDetailsViewModel?.getTaskCategory()?.collect {
            homePageToolBarService.changeTaskCategoryIcon(toolBar = requireHomePageToolBar(), taskCategory = it)
            it?.let {
                rcvTaskCategory.setSelectedTaskCategory(it)
            }
        }
    }

    override fun onTaskCategoryClick(taskCategory: TaskCategory) {
        bottomSheet.dismissBottomSheet()
        fragmentTaskTextDetailsViewModel?.saveTaskCategory(taskCategory)
    }

    override fun onStop() {
        fragmentTaskTextDetailsViewModel?.saveCurrentTask()
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
        fun newInstance() = FragmentTaskTextDetails()
        const val THIS_FRAGMENT_FOR = "ThisFragmentFor"
        const val FOR_CREATE_NEW_TASK = "ForCreateNewTask"
        const val FOR_UPDATE_EXISTING_TASK = "ForUpdateExistingTask"
        const val EXISTING_TASK_TEXT = "ExistingTaskText"
    }

}