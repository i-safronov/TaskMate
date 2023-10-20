package safronov.apps.taskmate.project.ui.fragment.start

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import safronov.apps.taskmate.R
import safronov.apps.taskmate.databinding.FragmentStartBinding
import safronov.apps.taskmate.project.system_settings.extension.fragment.requireAppComponent
import safronov.apps.taskmate.project.ui.fragment.start.view_model.FragmentStartViewModel
import safronov.apps.taskmate.project.ui.fragment.start.view_model.FragmentStartViewModelFactory
import javax.inject.Inject

class FragmentStart : Fragment() {

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var fragmentStartViewModelFactory: FragmentStartViewModelFactory
    private var fragmentStartViewModel: FragmentStartViewModel? = null

    override fun onStart() {
        super.onStart()
        try {
            setup()
        } catch (e: RuntimeException) {
            Log.e(TAG, e.message.toString())
        }
    }

    private fun setup() {
        requireAppComponent().inject(this)
        fragmentStartViewModel = ViewModelProvider(this, fragmentStartViewModelFactory)
            .get(FragmentStartViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentStart()
        const val TAG = "sfrLog"
    }

}