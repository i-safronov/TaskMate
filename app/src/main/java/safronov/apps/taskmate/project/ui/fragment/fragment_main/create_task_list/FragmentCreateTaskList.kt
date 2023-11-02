package safronov.apps.taskmate.project.ui.fragment.fragment_main.create_task_list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import safronov.apps.domain.model.task.Task
import safronov.apps.domain.model.task_category.TaskCategory
import safronov.apps.taskmate.R
import safronov.apps.taskmate.databinding.BottomSheetChooseItemBinding
import safronov.apps.taskmate.databinding.FragmentCreateTaskListBinding
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
import safronov.apps.taskmate.project.ui.fragment.fragment_main.create_task_list.rcv.RcvTaskListItem
import safronov.apps.taskmate.project.ui.fragment.fragment_main.create_task_list.rcv.RcvTaskListItemInt
import safronov.apps.taskmate.project.ui.fragment.fragment_main.create_task_list.view_model.FragmentCreateTaskListViewModel
import safronov.apps.taskmate.project.ui.fragment.fragment_main.create_task_list.view_model.FragmentCreateTaskListViewModelFactory
import safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.task_category.RcvTaskCategory
import safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.task_category.RcvTaskCategoryInt
import javax.inject.Inject


//TODO add empty task list item by click on button to add item

class FragmentCreateTaskList : FragmentBase(), RcvTaskCategoryInt, RcvTaskListItemInt {

    private var _binding: FragmentCreateTaskListBinding? = null
    private val binding get() = _binding!!
    private val rcvTaskCategory = RcvTaskCategory(this)
    private var rcvTaskListItem: RcvTaskListItem? = null

    @Inject
    lateinit var textWatcher: TextWatcher

    @Inject
    lateinit var bottomSheet: BottomSheet

    @Inject
    lateinit var fragmentCreateTaskListViewModelFactory: FragmentCreateTaskListViewModelFactory
    private var fragmentCreateTaskListViewModel: FragmentCreateTaskListViewModel? = null

    @Inject
    lateinit var dispatchersList: DispatchersList

    @Inject
    lateinit var homePageToolBarService: HomePageToolBarService

    @Inject
    lateinit var recyclerViewBuilder: RecyclerViewBuilder

    override fun createUI(inflater: LayoutInflater, container: ViewGroup?): View? {
        _binding = FragmentCreateTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setup() {
        requireAppComponent().inject(this)
        fragmentCreateTaskListViewModel = ViewModelProvider(this, fragmentCreateTaskListViewModelFactory)
            .get(FragmentCreateTaskListViewModel::class.java)
        rcvTaskListItem = RcvTaskListItem(rcvTaskListItemInt = this)
    }

    override fun uiCreated(view: View, savedInstanceState: Bundle?) {
        fragmentCreateTaskListViewModel?.loadDefaultCurrentTaskCategory()
        addItemMenuClickListenerOnHomePageToolBar()
        addTextWatcherToEdtvTitle()
        observeTaskSaved()
        observeWasException()
        includedAddButtonLayoutOnClickListener()
        recyclerViewBuilder.setupRcv(binding.rcvListTasks, rcvTaskListItem!!, LinearLayoutManager(requireContext()))
        binding.tvDate.text = fragmentCreateTaskListViewModel?.getCurrentTime()
    }

    private fun addItemMenuClickListenerOnHomePageToolBar() {
        homePageToolBarService.setOnMenuItemClickListener(
            toolBar = requireHomePageToolBar(),
            pinTask = {
                fragmentCreateTaskListViewModel?.pinCurrentTask()
            },
            chooseTaskCategory = {
                val bottomView = BottomSheetChooseItemBinding.inflate(layoutInflater)
                bottomView.tvTitle.text = getString(R.string.choose)
                recyclerViewBuilder.setupRcv(bottomView.rcvTypes, rcvTaskCategory, LinearLayoutManager(requireContext()))
                rcvTaskCategory.submitList(fragmentCreateTaskListViewModel?.getTaskCategories() ?: emptyList())
                bottomSheet.showBottomSheet(activityContext = requireContext(), view = bottomView.root)
            }, saveTask = {
                //TODO save current task
            }
        )
    }

    private fun addTextWatcherToEdtvTitle() {
        textWatcher.addTextWatcherToView(binding.edtvTitle, afterTextChanged = {
            fragmentCreateTaskListViewModel?.saveCurrentTaskTitle(it)
        })
    }

    private fun observeTaskSaved() = viewLifecycleOwner.lifecycleScope.launch(dispatchersList.ui()) {
        fragmentCreateTaskListViewModel?.getTaskSaved()?.collect {
            if (it == true) {
                Snackbar.make(requireView(), getString(R.string.saved), Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeWasException() = viewLifecycleOwner.lifecycleScope.launch(dispatchersList.ui()) {
        fragmentCreateTaskListViewModel?.isWasException()?.collect {
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

    override fun onTaskCategoryClick(taskCategory: TaskCategory) {
        bottomSheet.dismissBottomSheet()
        fragmentCreateTaskListViewModel?.saveCurrentTaskCategory(taskCategory)
    }

    override fun handeException(e: RuntimeException) {
        goToFragmentErrorFromHomePage(e.message.toString())
    }

    override fun taskListItemsChanged(list: List<Task.TaskListItem>) {
        fragmentCreateTaskListViewModel?.saveCurrentTaskListItems(list)
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
        fragmentCreateTaskListViewModel?.getIsCurrentTaskPin()?.collect {
            homePageToolBarService.changePinTaskIconByParam(toolBar = requireHomePageToolBar(), isPinned = it)
        }
    }

    private fun observeTaskCategory() = viewLifecycleOwner.lifecycleScope.launch(dispatchersList.ui()) {
        fragmentCreateTaskListViewModel?.getCurrentTaskCategory()?.collect {
            homePageToolBarService.changeTaskCategoryIcon(toolBar = requireHomePageToolBar(), taskCategory = it)
            it?.let {
                rcvTaskCategory.setSelectedTaskCategory(it)
            }
        }
    }


    override fun onStop() {
        super.onStop()
        removeMenuFromHomePageToolBar()
    }

    override fun removeUI() {
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentCreateTaskList()
    }

}