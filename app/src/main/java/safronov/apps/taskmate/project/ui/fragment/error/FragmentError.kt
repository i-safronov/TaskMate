package safronov.apps.taskmate.project.ui.fragment.error

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import safronov.apps.taskmate.databinding.FragmentErrorBinding

class FragmentError : Fragment() {

    private var _binding: FragmentErrorBinding? = null
    private val binding get() = _binding!!
    private var errorMessage: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentErrorBinding.inflate(inflater, container, false)
        try {
            errorMessage = requireArguments().getString(ERROR_MESSAGE, "")
            Log.e(TAG, errorMessage)
        } catch (e: RuntimeException) {
            Log.e(TAG, e.message.toString())
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            btnTryAgainOnClickListener()
        } catch (e: RuntimeException) {
            Log.e(TAG, e.message.toString())
        }
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