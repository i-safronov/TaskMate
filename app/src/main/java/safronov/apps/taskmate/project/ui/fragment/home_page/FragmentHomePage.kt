package safronov.apps.taskmate.project.ui.fragment.home_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavHost
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import safronov.apps.taskmate.R
import safronov.apps.taskmate.databinding.FragmentHomePageBinding
import safronov.apps.taskmate.project.system_settings.fragment.FragmentBase
import java.lang.IllegalStateException

class FragmentHomePage : FragmentBase() {

    private var _binding: FragmentHomePageBinding? = null
    private val binding get() = _binding!!

    override fun createUI(inflater: LayoutInflater, container: ViewGroup?): View? {
        _binding = FragmentHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setup() {
        val navHostFragment = childFragmentManager.findFragmentById(R.id.home_page_container)
                as NavHost?
        binding.includedFragmentHomePageToolbar.root.setupWithNavController(
            navController = navHostFragment?.navController ?: throw IllegalStateException("don't found nav controller"),
        )
    }

    override fun uiCreated(view: View, savedInstanceState: Bundle?) { }

    override fun handeException(e: RuntimeException) { }

    override fun removeUI() {
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentHomePage()
    }

}