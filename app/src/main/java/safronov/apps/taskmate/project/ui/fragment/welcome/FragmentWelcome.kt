package safronov.apps.taskmate.project.ui.fragment.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import safronov.apps.taskmate.R
import safronov.apps.taskmate.databinding.FragmentWelcomeBinding
import safronov.apps.taskmate.project.system_settings.coroutines.DispatchersList
import safronov.apps.taskmate.project.system_settings.data.DefaultTaskCategories
import safronov.apps.taskmate.project.system_settings.extension.fragment.goToFragmentError
import safronov.apps.taskmate.project.system_settings.extension.fragment.navigate
import safronov.apps.taskmate.project.system_settings.extension.fragment.requireAppComponent
import safronov.apps.taskmate.project.system_settings.fragment.FragmentBase
import safronov.apps.taskmate.project.ui.fragment.welcome.view_model.FragmentWelcomeViewModel
import safronov.apps.taskmate.project.ui.fragment.welcome.view_model.FragmentWelcomeViewModelFactory
import javax.inject.Inject

class FragmentWelcome : FragmentBase() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var dispatchersList: DispatchersList

    @Inject
    lateinit var defaultTaskCategories: DefaultTaskCategories

    @Inject
    lateinit var fragmentWelcomeViewModelFactory: FragmentWelcomeViewModelFactory
    private var fragmentWelcomeViewModel: FragmentWelcomeViewModel? = null

    override fun createUI(inflater: LayoutInflater, container: ViewGroup?): View? {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setup() {
        requireAppComponent().inject(this)
        fragmentWelcomeViewModel = ViewModelProvider(this, fragmentWelcomeViewModelFactory)
            .get(FragmentWelcomeViewModel::class.java)
        binding.btnLogIn.tvLoading.text = getString(R.string.continuee)
    }

    override fun uiCreated(view: View, savedInstanceState: Bundle?) {
        observeIsLoading()
        observeWasException()
        observeUserLoggedIn()
        btnLogInOnClickListener()
    }

    override fun handeException(e: RuntimeException) {
        goToFragmentError(e.message.toString())
    }

    override fun removeUI() {
        _binding = null
    }

    private fun observeIsLoading() = viewLifecycleOwner.lifecycleScope.launch(dispatchersList.ui()) {
        fragmentWelcomeViewModel?.isLoading()?.onEach {
            if (it != null) {
                if (it) {
                    binding.btnLogIn.tvLoading.text = getString(R.string.loading)
                    binding.btnLogIn.loadingProgressBar.visibility = View.VISIBLE
                } else {
                    binding.btnLogIn.tvLoading.text = getString(R.string.continuee)
                    binding.btnLogIn.loadingProgressBar.visibility = View.GONE
                }
            }
        }?.collect()
    }

    private fun observeWasException() = viewLifecycleOwner.lifecycleScope.launch(dispatchersList.ui()) {
        fragmentWelcomeViewModel?.wasException()?.onEach {
            if (it != null) {
                handeException(it)
            }
        }?.collect()
    }

    private fun observeUserLoggedIn() = viewLifecycleOwner.lifecycleScope.launch(dispatchersList.ui()) {
        fragmentWelcomeViewModel?.userLoggedIn()?.onEach {
            if (it == true) {
                navigate(R.id.action_fragmentWelcome_to_fragmentMain)
            }
        }?.collect()
    }

    private fun btnLogInOnClickListener() {
        binding.btnLogIn.root.setOnClickListener {
            fragmentWelcomeViewModel?.requestToLogIn(
                defaultTaskCategories = defaultTaskCategories.getDefaultTaskCategories()
            )
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentWelcome()
    }

}