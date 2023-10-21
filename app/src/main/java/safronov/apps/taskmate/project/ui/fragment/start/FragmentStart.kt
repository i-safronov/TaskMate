package safronov.apps.taskmate.project.ui.fragment.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import kotlinx.coroutines.launch
import safronov.apps.taskmate.R
import safronov.apps.taskmate.databinding.FragmentStartBinding
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList
import safronov.apps.taskmate.project.system_settings.extension.fragment.goToFragmentError
import safronov.apps.taskmate.project.system_settings.extension.fragment.requireAppComponent
import safronov.apps.taskmate.project.system_settings.fragment.FragmentBase
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

    override fun setup() {
        requireAppComponent().inject(this)
        fragmentStartViewModel = ViewModelProvider(this, fragmentStartViewModelFactory)
            .get(FragmentStartViewModel::class.java)
    }

    override fun createUI(inflater: LayoutInflater, container: ViewGroup?): View? {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        setup()
        fragmentStartViewModel?.checkIsUserLoggedIn()
        return binding.root
    }

    override fun uiCreated(view: View, savedInstanceState: Bundle?) {
        observeIsUserLoggedIn()
    }

    override fun handeException(e: RuntimeException) {
        goToFragmentError(e.message.toString())
    }

    override fun removeUI() {
        _binding = null
    }

    private fun observeIsUserLoggedIn() = viewLifecycleOwner.lifecycleScope.launch(dispatchersList.ui()) {
        fragmentStartViewModel?.isUserLoggedIn()?.collect {
            if (it != null) {
                if (it) {
                    //TODO пуляй пользователя на главный экран
                } else {
                    findNavController().navigate(
                        R.id.action_fragmentStart_to_fragmentWelcome,
                        null,
                        navOptions = navOptions {
                            popUpTo(R.id.fragmentStart) {
                                inclusive = true
                            }
                        }
                    )
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentStart()
    }

}