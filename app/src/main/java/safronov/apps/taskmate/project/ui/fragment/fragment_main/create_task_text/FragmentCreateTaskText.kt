package safronov.apps.taskmate.project.ui.fragment.fragment_main.create_task_text

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import safronov.apps.domain.model.task_category.TaskCategory
import safronov.apps.taskmate.R
import safronov.apps.taskmate.databinding.BottomSheetChooseItemBinding
import safronov.apps.taskmate.databinding.FragmentCreateTaskTextBinding
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
import safronov.apps.taskmate.project.ui.fragment.fragment_main.FragmentMain
import safronov.apps.taskmate.project.ui.fragment.fragment_main.create_task_text.view_model.FragmentCreateTaskTextViewModel
import safronov.apps.taskmate.project.ui.fragment.fragment_main.create_task_text.view_model.FragmentCreateTaskTextViewModelFactory
import safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.task_category.RcvTaskCategory
import safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.task_category.RcvTaskCategoryInt
import javax.inject.Inject

class FragmentCreateTaskText : FragmentBase(), RcvTaskCategoryInt {

    private var _binding: FragmentCreateTaskTextBinding? = null
    private val binding get() = _binding!!
    private val rcvTaskCategory = RcvTaskCategory(this)

    @Inject
    lateinit var textWatcher: TextWatcher

    @Inject
    lateinit var bottomSheet: BottomSheet

    @Inject
    lateinit var fragmentCreateTaskTextViewModelFactory: FragmentCreateTaskTextViewModelFactory
    private var fragmentCreateTaskTextViewModel: FragmentCreateTaskTextViewModel? = null

    @Inject
    lateinit var dispatchersList: DispatchersList

    @Inject
    lateinit var homePageToolBarService: HomePageToolBarService

    @Inject
    lateinit var recyclerViewBuilder: RecyclerViewBuilder

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
                val bottomView = BottomSheetChooseItemBinding.inflate(layoutInflater)
                bottomView.tvTitle.text = getString(R.string.choose)
                recyclerViewBuilder.setupRcv(bottomView.rcvTypes, rcvTaskCategory, LinearLayoutManager(requireContext()))
                rcvTaskCategory.submitList(fragmentCreateTaskTextViewModel?.getTaskCategories() ?: emptyList())
                bottomSheet.showBottomSheet(activityContext = requireContext(), view = bottomView.root)
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

    override fun onTaskCategoryClick(taskCategory: TaskCategory) {
        //TODO save current task category
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