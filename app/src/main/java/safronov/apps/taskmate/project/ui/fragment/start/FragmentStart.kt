package safronov.apps.taskmate.project.ui.fragment.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import safronov.apps.taskmate.R
import safronov.apps.taskmate.databinding.FragmentStartBinding
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList
import safronov.apps.taskmate.project.system_settings.extension.fragment.navigateAndDeletePrevFragment
import safronov.apps.taskmate.project.system_settings.extension.fragment.requireAppComponent
import safronov.apps.taskmate.project.system_settings.fragment.FragmentBase
import safronov.apps.taskmate.project.ui.fragment.error.FragmentError
import safronov.apps.taskmate.project.ui.fragment.start.view_model.FragmentStartViewModel
import safronov.apps.taskmate.project.ui.fragment.start.view_model.FragmentStartViewModelFactory
import javax.inject.Inject

class FragmentStart : FragmentBase() {

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var fragmentStartViewModelFactory: FragmentStartViewModelFactory
    private var fragmentStartViewModel: FragmentStartViewModel? = null

    @Inject
    lateinit var dispatchersList: DispatchersList

    override fun createUI(inflater: LayoutInflater, container: ViewGroup?): View? {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setup() {
        requireAppComponent().inject(this)
        fragmentStartViewModel = ViewModelProvider(this, fragmentStartViewModelFactory)
            .get(FragmentStartViewModel::class.java)
        fragmentStartViewModel?.checkIsUserLoggedIn()
    }

    override fun uiCreated(view: View, savedInstanceState: Bundle?) {
        observeIsUserLoggedIn()
    }

    override fun handeException(e: RuntimeException) {
        findNavController().navigate(
            R.id.action_fragmentStart_to_fragmentError,
            bundleOf(
                FragmentError.ERROR_MESSAGE to e.message
            )
        )
    }

    override fun removeUI() {
        _binding = null
    }

    private fun observeIsUserLoggedIn() = viewLifecycleOwner.lifecycleScope.launch(dispatchersList.ui()) {
        fragmentStartViewModel?.isUserLoggedIn()?.collect {
            if (it != null) {
                fragmentStartViewModel?.whichFragmentToGoByUserLoggedOrNot(
                    isUserLogged = it,
                    welcome = {
                        navigateAndDeletePrevFragment(
                            actionId = R.id.action_fragmentStart_to_fragmentWelcome,
                            currentFragmentId = R.id.fragmentStart
                        )
                    }, homePage = {
                        navigateAndDeletePrevFragment(
                            actionId = R.id.action_fragmentStart_to_fragmentHomePage,
                            currentFragmentId = R.id.fragmentStart
                        )
                    }
                )
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentStart()
    }

}