package safronov.apps.taskmate.project.ui.fragment.fragment_main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import safronov.apps.taskmate.R
import safronov.apps.taskmate.databinding.BottomSheetChooseItemBinding
import safronov.apps.taskmate.databinding.FragmentMainBinding
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList
import safronov.apps.taskmate.project.system_settings.extension.fragment.goToFragmentErrorFromHomePage
import safronov.apps.taskmate.project.system_settings.extension.fragment.inflateMenuOnHomePageToolBar
import safronov.apps.taskmate.project.system_settings.extension.fragment.navigate
import safronov.apps.taskmate.project.system_settings.extension.fragment.removeMenuFromHomePageToolBar
import safronov.apps.taskmate.project.system_settings.extension.fragment.requireAppComponent
import safronov.apps.taskmate.project.system_settings.fragment.FragmentBase
import safronov.apps.taskmate.project.system_settings.ui.bottom_sheet.BottomSheet
import safronov.apps.taskmate.project.system_settings.ui.rcv.RecyclerViewBuilder
import safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.rcv_task_type.RcvTaskType
import safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.rcv_task_type.RcvTaskTypeInt
import safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.model.RcvTaskTypeModel
import safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.rcv_task.RcvTask
import safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.task_type.AllTaskTypes
import safronov.apps.taskmate.project.ui.fragment.fragment_main.view_model.FragmentMainViewModel
import safronov.apps.taskmate.project.ui.fragment.fragment_main.view_model.FragmentMainViewModelFactory
import javax.inject.Inject

class FragmentMain : FragmentBase(), RcvTaskTypeInt {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val rcvTaskType = RcvTaskType(this)
    private val rcvTask = RcvTask()

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
        observeStateLoading()
        observeTasks()
        observeException()
        fbAddTaskOnClickListener()
        searchOnClickListener()
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
                navigate(R.id.action_fragmentMain_to_fragmentCreateTaskText)
            }, taskList = {
                navigate(R.id.action_fragmentMain_to_fragmentCreateTaskList)
            }
        )
        bottomSheet.dismissBottomSheet()
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentMain()
        private const val RCV_TYPES_SPAN_COUNT = 2
        private const val RCV_TASKS_SPAN_COUNT = 2
    }

}