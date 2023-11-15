package safronov.apps.taskmate.project.ui.fragment.fragment_main

import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import safronov.apps.domain.model.task.Task
import safronov.apps.domain.model.task_category.TaskCategory
import safronov.apps.taskmate.R
import safronov.apps.taskmate.databinding.AskUserBinding
import safronov.apps.taskmate.databinding.BottomSheetChooseItemBinding
import safronov.apps.taskmate.databinding.BottomSheetChooseOrChangeTaskCategoriesBinding
import safronov.apps.taskmate.databinding.FragmentMainBinding
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList
import safronov.apps.taskmate.project.system_settings.extension.fragment.goToFragmentErrorFromHomePage
import safronov.apps.taskmate.project.system_settings.extension.fragment.inflateMenuOnHomePageToolBar
import safronov.apps.taskmate.project.system_settings.extension.fragment.navigate
import safronov.apps.taskmate.project.system_settings.extension.fragment.removeMenuFromHomePageToolBar
import safronov.apps.taskmate.project.system_settings.extension.fragment.requireAppComponent
import safronov.apps.taskmate.project.system_settings.extension.fragment.requireHomePageToolBar
import safronov.apps.taskmate.project.system_settings.fragment.FragmentBase
import safronov.apps.taskmate.project.system_settings.ui.bottom_sheet.BottomSheet
import safronov.apps.taskmate.project.system_settings.ui.rcv.RecyclerViewBuilder
import safronov.apps.taskmate.project.system_settings.ui.text_watcher.TextWatcher
import safronov.apps.taskmate.project.ui.fragment.fragment_main.task_list_details.FragmentTaskListDetails
import safronov.apps.taskmate.project.ui.fragment.fragment_main.task_text_details.FragmentTaskTextDetails
import safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.rcv_task_type.RcvTaskType
import safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.rcv_task_type.RcvTaskTypeInt
import safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.model.RcvTaskTypeModel
import safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.rcv_task.RcvTask
import safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.rcv_task.RcvTaskInt
import safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.task_category.changing.RcvChangingTaskCategory
import safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.task_category.changing.RcvChangingTaskCategoryInt
import safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.task_type.AllTaskTypes
import safronov.apps.taskmate.project.ui.fragment.fragment_main.view_model.FragmentMainViewModel
import safronov.apps.taskmate.project.ui.fragment.fragment_main.view_model.FragmentMainViewModelFactory
import java.lang.IllegalStateException
import javax.inject.Inject

class FragmentMain : FragmentBase(), RcvTaskTypeInt, RcvTaskInt, RcvChangingTaskCategoryInt {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val rcvTaskType = RcvTaskType(this)
    private val rcvTask = RcvTask(this)

    @Inject
    lateinit var textWatcher: TextWatcher

    @Inject
    lateinit var dispatchersList: DispatchersList

    @Inject
    lateinit var bottomSheet: BottomSheet

    @Inject
    lateinit var allTaskTypes: AllTaskTypes

    @Inject
    lateinit var recyclerViewBuilder: RecyclerViewBuilder

    @Inject
    lateinit var fragmentMainViewModelFactory: FragmentMainViewModelFactory
    private var fragmentMainViewModel: FragmentMainViewModel? = null

    override fun createUI(inflater: LayoutInflater, container: ViewGroup?): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setup() {
        requireAppComponent().inject(this)
        fragmentMainViewModel = ViewModelProvider(this, fragmentMainViewModelFactory)
            .get(FragmentMainViewModel::class.java)
        binding.rcvTasks.layoutManager = GridLayoutManager(requireContext(), RCV_TASKS_SPAN_COUNT)
        binding.rcvTasks.adapter = rcvTask
        fragmentMainViewModel?.loadTasks()
    }

    override fun uiCreated(view: View, savedInstanceState: Bundle?) {
        binding.animateFbAddTask.startRippleAnimation()
        observeOnToolBarMenuItemClick()
        observeStateLoading()
        observeTasks()
        observeException()
        fbAddTaskOnClickListener()
        searchOnClickListener()
        onBackPressListener()
    }

    private fun observeOnToolBarMenuItemClick() {
        requireHomePageToolBar().setOnMenuItemClickListener {
            var handled = false

            if (it.itemId == R.id.choose_category) {
                //TODO show bottom sheet with categories
                val rcvCHangingTaskCategory = RcvChangingTaskCategory(textWatcher, this)
                val bottomView = BottomSheetChooseOrChangeTaskCategoriesBinding.inflate(layoutInflater)
                bottomView.tvTitle.text = getString(R.string.sort)
                bottomView.tvAction.text = getString(R.string.change)
                bottomView.rcvTypes.layoutManager = LinearLayoutManager(requireContext())
                bottomView.rcvTypes.adapter = rcvCHangingTaskCategory
                bottomSheet.showBottomSheet(requireContext(), bottomView.root)
                rcvCHangingTaskCategory.submitList(fragmentMainViewModel?.getCategories()?.value ?: emptyList())
                handled = true
            } else if (it.itemId == R.id.view_of_tasks) {
                //TODO show alert dialog to choose view of recycler view of tasks
                handled = true
            }

            handled
        }
    }

    private fun observeStateLoading() = viewLifecycleOwner.lifecycleScope.launch(dispatchersList.ui()) {
        fragmentMainViewModel?.getIsLoading()?.collect {
            if (it != null) {
                if (it) {
                    loadingState()
                } else {
                    loadedState()
                }
            }
        }
    }

    private fun observeTasks() = viewLifecycleOwner.lifecycleScope.launch(dispatchersList.ui()) {
        fragmentMainViewModel?.getTasks()?.collect {
            if (it != null) {
                if (it.isEmpty()) {
                    binding.includedNoTasksLayout.root.visibility = View.VISIBLE
                } else {
                    binding.includedNoTasksLayout.root.visibility = View.GONE
                    rcvTask.submitList(it)
                }
            }
        }
    }

    private fun observeException() = viewLifecycleOwner.lifecycleScope.launch(dispatchersList.ui()) {
        fragmentMainViewModel?.getIsWasException()?.collect {
            if (it != null) {
                handeException(it)
            }
        }
    }

    private fun loadingState() {
        binding.rcvTasks.visibility = View.GONE
        binding.progress.visibility = View.VISIBLE
    }

    private fun loadedState() {
        binding.rcvTasks.visibility = View.VISIBLE
        binding.progress.visibility = View.GONE
    }

    override fun handeException(e: RuntimeException) {
        goToFragmentErrorFromHomePage(e.message.toString())
    }

    override fun onStart() {
        super.onStart()
        inflateMenuOnHomePageToolBar(menuId = R.menu.fragment_main_toolbar_menu)
    }

    override fun onStop() {
        super.onStop()
        removeMenuFromHomePageToolBar()
    }

    override fun removeUI() {
        _binding = null
    }

    private fun fbAddTaskOnClickListener() {
        binding.fbAddTask.setOnClickListener {
            val bottomView = BottomSheetChooseItemBinding.inflate(layoutInflater)
            bottomView.tvTitle.text = getString(R.string.add)
            recyclerViewBuilder.setupRcv(bottomView.rcvTypes, rcvTaskType, GridLayoutManager(requireContext(), RCV_TYPES_SPAN_COUNT))
            rcvTaskType.submitList(allTaskTypes.getTaskTypes())
            bottomSheet.showBottomSheet(activityContext = requireContext(), view = bottomView.root)
        }
    }

    private fun searchOnClickListener() {
        binding.search.setOnClickListener {
            navigate(R.id.action_fragmentMain_to_fragmentSearchTasks)
        }
    }

    override fun onTaskTypeClick(taskType: RcvTaskTypeModel) {
        fragmentMainViewModel?.whichFragmentToGoByTaskType(
            taskType = taskType.taskType,
            taskText = {
                navigate(
                    R.id.action_fragmentMain_to_fragmentTaskTextDetails,
                    bundleOf(
                        FragmentTaskTextDetails.THIS_FRAGMENT_FOR to FragmentTaskTextDetails.FOR_CREATE_NEW_TASK
                    )
                )
            }, taskList = {
                navigate(
                    R.id.action_fragmentMain_to_fragmentTaskListDetails,
                    bundleOf(
                        FragmentTaskListDetails.THIS_FRAGMENT_FOR to FragmentTaskListDetails.FOR_CREATE_NEW_TASK
                    )
                )
            }
        )
        bottomSheet.dismissBottomSheet()
    }

    override fun onTaskClick(task: Task) {
        if (task is Task.TaskList) {
            navigate(
                R.id.action_fragmentMain_to_fragmentTaskListDetails,
                bundleOf(
                    FragmentTaskListDetails.THIS_FRAGMENT_FOR to FragmentTaskListDetails.FOR_UPDATE_EXISTING_TASK,
                    FragmentTaskListDetails.EXISTING_TASK_LIST to task
                )
            )
        } else if (task is Task.TaskText) {
            navigate(
                R.id.action_fragmentMain_to_fragmentTaskTextDetails,
                bundleOf(
                    FragmentTaskTextDetails.THIS_FRAGMENT_FOR to FragmentTaskTextDetails.FOR_UPDATE_EXISTING_TASK,
                    FragmentTaskTextDetails.EXISTING_TASK_TEXT to task
                )
            )
        } else {
            throw IllegalStateException("not found task")
        }
    }

    override fun onTaskSelectionMode() {
        removeMenuFromHomePageToolBar()
        inflateMenuOnHomePageToolBar(R.menu.fragment_home_page_tool_bar_selection_tasks_menu)
        requireHomePageToolBar().setOnMenuItemClickListener {
            var handled = false
            if (it.itemId == R.id.delete_tasks) {
                val alertDialog = AlertDialog.Builder(requireContext()).create()
                val alertView = AskUserBinding.inflate(layoutInflater)
                alertDialog.window?.setBackgroundDrawable(ColorDrawable(resources.getColor(android.R.color.transparent)))
                alertView.tvTitle.text = getString(R.string.delete_tasks)
                alertView.btnNo.setOnClickListener {
                    alertDialog.dismiss()
                }
                alertView.btnYes.setOnClickListener {
                    fragmentMainViewModel?.deleteTasks(rcvTask.getSelectedTasks())
                    clearSelectionModeOnTasks()
                    alertDialog.dismiss()
                }
                alertDialog.setView(alertView.root)
                alertDialog.setCancelable(false)
                alertDialog.show()
                handled = true
            }
            handled
        }
    }

    override fun selectionTasksChanged(list: List<Task>) {
        val title = "${getString(R.string.selected)}: ${list.size}"
        requireHomePageToolBar().title = title
    }

    override fun onTaskCategoryClick(taskCategory: TaskCategory) {
        //TODO save selected task category
    }

    private fun onBackPressListener() {
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (rcvTask.isSelectionMode()) {
                        clearSelectionModeOnTasks()
                    } else {
                        if (isEnabled) {
                            isEnabled = false
                            requireActivity().onBackPressed()
                        }
                    }
                }
            })
    }

    private fun clearSelectionModeOnTasks() {
        removeMenuFromHomePageToolBar()
        inflateMenuOnHomePageToolBar(R.menu.fragment_main_toolbar_menu)
        requireHomePageToolBar().title = getString(R.string.app_name)
        rcvTask.clearSelectionMode()
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentMain()
        private const val RCV_TYPES_SPAN_COUNT = 2
        private const val RCV_TASKS_SPAN_COUNT = 2
    }


}