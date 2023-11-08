package safronov.apps.taskmate.project.ui.fragment.fragment_main.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.launch
import safronov.apps.domain.model.task.Task
import safronov.apps.taskmate.R
import safronov.apps.taskmate.databinding.FragmentSearchTasksBinding
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList
import safronov.apps.taskmate.project.system_settings.extension.fragment.focusOnViewAndShowKeyboard
import safronov.apps.taskmate.project.system_settings.extension.fragment.goToFragmentErrorFromHomePage
import safronov.apps.taskmate.project.system_settings.extension.fragment.navigate
import safronov.apps.taskmate.project.system_settings.extension.fragment.requireAppComponent
import safronov.apps.taskmate.project.system_settings.fragment.FragmentBase
import safronov.apps.taskmate.project.ui.fragment.fragment_main.task_list_details.FragmentTaskListDetails
import safronov.apps.taskmate.project.ui.fragment.fragment_main.task_text_details.FragmentTaskTextDetails
import safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.rcv_task.RcvTask
import safronov.apps.taskmate.project.ui.fragment.fragment_main.rcv.rcv_task.RcvTaskInt
import safronov.apps.taskmate.project.ui.fragment.fragment_main.search.rcv.RcvTaskForSearching
import safronov.apps.taskmate.project.ui.fragment.fragment_main.search.rcv.RcvTaskForSearchingInt
import safronov.apps.taskmate.project.ui.fragment.fragment_main.search.view_model.FragmentSearchTasksViewModel
import safronov.apps.taskmate.project.ui.fragment.fragment_main.search.view_model.FragmentSearchTasksViewModelFactory
import javax.inject.Inject


class FragmentSearchTasks : FragmentBase(), RcvTaskForSearchingInt {

    private var _binding: FragmentSearchTasksBinding? = null
    private val binding get() = _binding!!
    private val rcvTask = RcvTaskForSearching(this)

    @Inject
    lateinit var fragmentSearchTasksViewModelFactory: FragmentSearchTasksViewModelFactory
    private var fragmentSearchTasksViewModel: FragmentSearchTasksViewModel? = null

    @Inject
    lateinit var dispatchersList: DispatchersList

    override fun createUI(inflater: LayoutInflater, container: ViewGroup?): View? {
        _binding = FragmentSearchTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setup() {
        requireAppComponent().inject(this)
        fragmentSearchTasksViewModel = ViewModelProvider(this, fragmentSearchTasksViewModelFactory)
            .get(FragmentSearchTasksViewModel::class.java)
        binding.rcvTasks.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rcvTasks.adapter = rcvTask
    }

    override fun uiCreated(view: View, savedInstanceState: Bundle?) {
        focusOnViewAndShowKeyboard(binding.includedSearchView.root)
        observeTasks()
        observeTextChangedInSearchView()
    }

    override fun handeException(e: RuntimeException) {
        goToFragmentErrorFromHomePage(e.message.toString())
    }

    private fun observeTasks() = viewLifecycleOwner.lifecycleScope.launch(dispatchersList.ui()) {
        fragmentSearchTasksViewModel?.getTasks()?.collect {
            rcvTask.submitList(it)
        }
    }

    private fun observeTextChangedInSearchView() {
        binding.includedSearchView.root.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                fragmentSearchTasksViewModel?.getTasksByText(query.toString().trim())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                fragmentSearchTasksViewModel?.getTasksByText(newText.toString().trim())
                return true
            }
        })
    }

    override fun onTaskTextClick(task: Task.TaskText) {
        navigate(
            R.id.action_fragmentSearchTasks_to_fragmentTaskTextDetails,
            bundleOf(
                FragmentTaskTextDetails.THIS_FRAGMENT_FOR to FragmentTaskTextDetails.FOR_UPDATE_EXISTING_TASK,
                FragmentTaskTextDetails.EXISTING_TASK_TEXT to task
            )
        )
    }

    override fun onTaskListClick(task: Task.TaskList) {
        navigate(
            R.id.action_fragmentSearchTasks_to_fragmentTaskListDetails,
            bundleOf(
                FragmentTaskListDetails.THIS_FRAGMENT_FOR to FragmentTaskListDetails.FOR_UPDATE_EXISTING_TASK,
                FragmentTaskListDetails.EXISTING_TASK_LIST to task
            )
        )
    }


    override fun removeUI() {
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentSearchTasks()
    }

}