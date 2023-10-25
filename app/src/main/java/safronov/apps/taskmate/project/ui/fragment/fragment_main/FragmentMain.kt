package safronov.apps.taskmate.project.ui.fragment.fragment_main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHost
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import safronov.apps.taskmate.R
import safronov.apps.taskmate.databinding.BottomSheetChooseTaskTypeBinding
import safronov.apps.taskmate.databinding.FragmentMainBinding
import safronov.apps.taskmate.project.system_settings.extension.fragment.findHomePageToolBar
import safronov.apps.taskmate.project.system_settings.extension.fragment.goToFragmentError
import safronov.apps.taskmate.project.system_settings.extension.fragment.inflateMenuOnHomePageToolBar
import safronov.apps.taskmate.project.system_settings.extension.fragment.navigate
import safronov.apps.taskmate.project.system_settings.extension.fragment.removeMenuFromHomePageToolBar
import safronov.apps.taskmate.project.system_settings.extension.fragment.requireAppComponent
import safronov.apps.taskmate.project.system_settings.fragment.FragmentBase
import safronov.apps.taskmate.project.system_settings.ui.bottom_sheet.BottomSheet
import safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.RcvTaskType
import safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.RcvTaskTypeInt
import safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.model.RcvTaskTypeModel
import safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.task_type.AllTaskTypes
import safronov.apps.taskmate.project.ui.fragment.fragment_main.view_model.FragmentMainViewModel
import safronov.apps.taskmate.project.ui.fragment.fragment_main.view_model.FragmentMainViewModelFactory
import java.lang.IllegalStateException
import javax.inject.Inject

class FragmentMain : FragmentBase(), RcvTaskTypeInt {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val rcvTaskType = RcvTaskType(this)
    private var bottomSheetDialog: BottomSheetDialog? = null

    @Inject
    lateinit var bottomSheet: BottomSheet

    @Inject
    lateinit var allTaskTypes: AllTaskTypes

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
        inflateMenuOnHomePageToolBar(menuId = R.menu.fragment_main_toolbar_menu)
        binding.animateFbAddTask.startRippleAnimation()
        fbAddTaskOnClickListener()
    }

    override fun handeException(e: RuntimeException) {
        goToFragmentError(e.message.toString())
    }

    override fun removeUI() {
        removeMenuFromHomePageToolBar()
        _binding = null
    }

    //TODO do refactor
    private fun fbAddTaskOnClickListener() {
        binding.fbAddTask.setOnClickListener {
            bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.bottom_sheet_dialog_theme)
            val bottomView = BottomSheetChooseTaskTypeBinding.inflate(layoutInflater)
            bottomView.rcvTypes.adapter = rcvTaskType
            bottomView.rcvTypes.layoutManager = GridLayoutManager(requireContext(), 2)
            rcvTaskType.submitList(allTaskTypes.getTaskTypes())
            bottomSheetDialog?.setContentView(bottomView.root)
            bottomSheetDialog?.show()
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
        bottomSheetDialog?.dismiss()
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentMain()
    }

}