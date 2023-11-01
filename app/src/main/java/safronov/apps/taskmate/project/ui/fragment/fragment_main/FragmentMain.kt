package safronov.apps.taskmate.project.ui.fragment.fragment_main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import safronov.apps.taskmate.R
import safronov.apps.taskmate.databinding.BottomSheetChooseItemBinding
import safronov.apps.taskmate.databinding.FragmentMainBinding
import safronov.apps.taskmate.project.system_settings.extension.fragment.goToFragmentErrorFromHomePage
import safronov.apps.taskmate.project.system_settings.extension.fragment.inflateMenuOnHomePageToolBar
import safronov.apps.taskmate.project.system_settings.extension.fragment.navigate
import safronov.apps.taskmate.project.system_settings.extension.fragment.removeMenuFromHomePageToolBar
import safronov.apps.taskmate.project.system_settings.extension.fragment.requireAppComponent
import safronov.apps.taskmate.project.system_settings.fragment.FragmentBase
import safronov.apps.taskmate.project.system_settings.ui.bottom_sheet.BottomSheet
import safronov.apps.taskmate.project.system_settings.ui.rcv.RecyclerViewBuilder
import safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.RcvTaskType
import safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.RcvTaskTypeInt
import safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.model.RcvTaskTypeModel
import safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.task_type.AllTaskTypes
import safronov.apps.taskmate.project.ui.fragment.fragment_main.view_model.FragmentMainViewModel
import safronov.apps.taskmate.project.ui.fragment.fragment_main.view_model.FragmentMainViewModelFactory
import javax.inject.Inject

class FragmentMain : FragmentBase(), RcvTaskTypeInt {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val rcvTaskType = RcvTaskType(this)

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
    }

    override fun uiCreated(view: View, savedInstanceState: Bundle?) {
        binding.animateFbAddTask.startRippleAnimation()
        fbAddTaskOnClickListener()
        searchOnClickListener()
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
    }

}