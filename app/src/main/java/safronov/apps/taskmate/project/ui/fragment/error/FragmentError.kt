package safronov.apps.taskmate.project.ui.fragment.error

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import safronov.apps.taskmate.databinding.FragmentErrorBinding
import safronov.apps.taskmate.project.system_settings.extension.fragment.goToFragmentErrorFromHomePage
import safronov.apps.taskmate.project.system_settings.fragment.FragmentBase

class FragmentError : FragmentBase() {

    private var _binding: FragmentErrorBinding? = null
    private val binding get() = _binding!!
    private var errorMessage: String = ""

    override fun createUI(inflater: LayoutInflater, container: ViewGroup?): View? {
        _binding = FragmentErrorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setup() {
        errorMessage = requireArguments().getString(ERROR_MESSAGE, "")
    }

    override fun uiCreated(view: View, savedInstanceState: Bundle?) {
        btnTryAgainOnClickListener()
    }

    override fun handeException(e: RuntimeException) {
        goToFragmentErrorFromHomePage(e.message.toString())
    }

    override fun removeUI() {
        _binding = null
    }

    private fun btnTryAgainOnClickListener() {
        binding.btnTryAgain.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentError()
        const val ERROR_MESSAGE = "Exception"
        private const val TAG = "sfrLog"
    }

}