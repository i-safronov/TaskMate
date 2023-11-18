package safronov.apps.taskmate.project.ui.fragment.fragment_main.task_list_details

import android.os.Bundle
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
import safronov.apps.taskmate.databinding.FragmentTaskListDetailsBinding
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList
import safronov.apps.taskmate.project.system_settings.extension.fragment.clearFocusAndHideKeyboard
import safronov.apps.taskmate.project.system_settings.extension.fragment.goToFragmentErrorFromHomePage
import safronov.apps.taskmate.project.system_settings.extension.fragment.inflateMenuOnHomePageToolBar
import safronov.apps.taskmate.project.system_settings.extension.fragment.removeMenuFromHomePageToolBar
import safronov.apps.taskmate.project.system_settings.extension.fragment.requireAppComponent
import safronov.apps.taskmate.project.system_settings.extension.fragment.requireHomePageToolBar
import safronov.apps.taskmate.project.system_settings.fragment.FragmentBase
import safronov.apps.taskmate.project.system_settings.ui.bottom_sheet.BottomSheet
import safronov.apps.taskmate.project.system_settings.ui.rcv.RecyclerViewBuilder
import safronov.apps.taskmate.project.system_settings.ui.text_watcher.TextWatcher
import safronov.apps.taskmate.project.system_settings.ui.tool_bar.HomePageToolBarService
import safronov.apps.taskmate.project.ui.fragment.fragment_main.task_list_details.rcv.RcvTaskListItem
import safronov.apps.taskmate.project.ui.fragment.fragment_main.task_list_details.rcv.RcvTaskListItemInt
import safronov.apps.taskmate.project.ui.fragment.fragment_main.task_list_details.view_model.FragmentTaskListDetailsViewModel
import safronov.apps.taskmate.project.ui.fragment.fragment_main.task_list_details.view_model.FragmentTaskListDetailsViewModelFactory
import safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.task_category.RcvTaskCategory
import safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.task_category.RcvTaskCategoryInt
import javax.inject.Inject

class FragmentTaskListDetails : FragmentBase(), RcvTaskCategoryInt, RcvTaskListItemInt {

    private var _binding: FragmentTaskListDetailsBinding? = null
    private val binding get() = _binding!!
    private val rcvTaskCategory = RcvTaskCategory(this)
    private var rcvTaskListItem: RcvTaskListItem? = null

    private var forWhatThisFragment = FragmentTaskListDetails.FOR_CREATE_NEW_TASK
    private var existingTaskList: Task.TaskList? = null

    @Inject
    lateinit var textWatcher: TextWatcher

    @Inject
    lateinit var bottomSheet: BottomSheet

    @Inject
    lateinit var fragmentTaskListDetailsViewModelFactory: FragmentTaskListDetailsViewModelFactory
    private var fragmentTaskListDetailsViewModel: FragmentTaskListDetailsViewModel? = null

    @Inject
    lateinit var dispatchersList: DispatchersList

    @Inject
    lateinit var homePageToolBarService: HomePageToolBarService

    @Inject
    lateinit var recyclerViewBuilder: RecyclerViewBuilder

    override fun createUI(inflater: LayoutInflater, container: ViewGroup?): View? {
        _binding = FragmentTaskListDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setup() {
        requireAppComponent().inject(this)
        fragmentTaskListDetailsViewModel = ViewModelProvider(this, fragmentTaskListDetailsViewModelFactory)
            .get(FragmentTaskListDetailsViewModel::class.java)
        rcvTaskListItem = RcvTaskListItem(rcvTaskListItemInt = this)
        forWhatThisFragment = requireArguments().getString(THIS_FRAGMENT_FOR, FOR_CREATE_NEW_TASK)
        if (forWhatThisFragment == FOR_UPDATE_EXISTING_TASK) {
            setupDefaultValues()
        }
    }

    private fun setupDefaultValues() {
        existingTaskList = requireArguments().getSerializable(EXISTING_TASK_LIST) as Task.TaskList
        fragmentTaskListDetailsViewModel?.prepareToChangeExistingTask(existingTaskList!!)
        binding.edtvTitle.setText(existingTaskList?.title)
        rcvTaskListItem?.submitList(existingTaskList?.list?.toMutableList() ?: mutableListOf())
    }

    //TODO refactor this code
    override fun uiCreated(view: View, savedInstanceState: Bundle?) {
        fragmentTaskListDetailsViewModel?.loadTaskCategories()
        fragmentTaskListDetailsViewModel?.loadDefaultCurrentTaskCategory()
        addItemMenuClickListenerOnHomePageToolBar()
        addTextWatcherToEdtvTitle()
        observeTaskSaved()
        observeWasException()
        includedAddButtonLayoutOnClickListener()
        observeTaskCategories()
        recyclerViewBuilder.setupRcv(binding.rcvListTasks, rcvTaskListItem!!, LinearLayoutManager(requireContext()))
        binding.tvDate.text = fragmentTaskListDetailsViewModel?.getCurrentTime()
        rcvTaskListItem?.submitList(fragmentTaskListDetailsViewModel?.getCurrentTaskListItems()?.toMutableList() ?: mutableListOf())
    }

    private fun addItemMenuClickListenerOnHomePageToolBar() {
        homePageToolBarService.setOnMenuItemClickListener(
            toolBar = requireHomePageToolBar(),
            pinTask = {
                fragmentTaskListDetailsViewModel?.pinCurrentTask()
            },
            chooseTaskCategory = {
                val bottomView = BottomSheetChooseItemBinding.inflate(layoutInflater)
                bottomView.tvTitle.text = getString(R.string.choose)
                recyclerViewBuilder.setupRcv(bottomView.rcvTypes, rcvTaskCategory, LinearLayoutManager(requireContext()))
                bottomSheet.showBottomSheet(activityContext = requireContext(), view = bottomView.root)
            }, saveTask = {
                fragmentTaskListDetailsViewModel?.saveCurrentTask()
            }
        )
    }

    private fun addTextWatcherToEdtvTitle() {
        textWatcher.addTextWatcherToView(binding.edtvTitle, afterTextChanged = {
            fragmentTaskListDetailsViewModel?.saveCurrentTaskTitle(it)
        })
    }

    private fun observeTaskSaved() = viewLifecycleOwner.lifecycleScope.launch(dispatchersList.ui()) {
        fragmentTaskListDetailsViewModel?.getTaskSaved()?.collect {
            if (it == true) {
                Toast.makeText(requireContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeWasException() = viewLifecycleOwner.lifecycleScope.launch(dispatchersList.ui()) {
        fragmentTaskListDetailsViewModel?.isWasException()?.collect {
            if (it != null) {
                handeException(it)
            }
        }
    }

    private fun includedAddButtonLayoutOnClickListener() {
        binding.includedAddButtonLayout.root.setOnClickListener {
            clearFocusAndHideKeyboard()
            rcvTaskListItem?.addTaskListItem(item = Task.TaskListItem(title = "", isChecked = false))
        }
    }

    private fun observeTaskCategories() = viewLifecycleOwner.lifecycleScope.launch(dispatchersList.ui()) {
        fragmentTaskListDetailsViewModel?.getCategories()?.collect {
            rcvTaskCategory.submitList(it)
        }
    }

    override fun onTaskCategoryClick(taskCategory: TaskCategory) {
        bottomSheet.dismissBottomSheet()
        fragmentTaskListDetailsViewModel?.saveCurrentTaskCategory(taskCategory)
    }

    override fun handeException(e: RuntimeException) {
        goToFragmentErrorFromHomePage(e.message.toString())
    }

    override fun taskListItemsChanged(list: List<Task.TaskListItem>) {
        fragmentTaskListDetailsViewModel?.saveCurrentTaskListItems(list)
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
        fragmentTaskListDetailsViewModel?.getIsCurrentTaskPin()?.collect {
            homePageToolBarService.changePinTaskIconByParam(toolBar = requireHomePageToolBar(), isPinned = it)
        }
    }

    private fun observeTaskCategory() = viewLifecycleOwner.lifecycleScope.launch(dispatchersList.ui()) {
        fragmentTaskListDetailsViewModel?.getCurrentTaskCategory()?.collect {
            homePageToolBarService.changeTaskCategoryIcon(toolBar = requireHomePageToolBar(), taskCategory = it)
            it?.let {
                rcvTaskCategory.setSelectedTaskCategory(it)
            }
        }
    }

    override fun onStop() {
        fragmentTaskListDetailsViewModel?.saveCurrentTask()
        super.onStop()
        removeMenuFromHomePageToolBar()
    }

    override fun removeUI() {
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentTaskListDetails()
        const val THIS_FRAGMENT_FOR = "ThisFragmentFor"
        const val FOR_CREATE_NEW_TASK = "ForCreateNewTask"
        const val FOR_UPDATE_EXISTING_TASK = "ForUpdateExistingTask"
        const val EXISTING_TASK_LIST = "ExistingTaskList"
    }

}