package safronov.apps.taskmate.project.system_settings.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import safronov.apps.taskmate.project.app.App

abstract class FragmentBase(): Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return try {
            setup()
            createUI(inflater, container)
        } catch (e: RuntimeException) {
            wasException(e)
            null
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            uiCreated(view, savedInstanceState)
        } catch (e: RuntimeException) {
            wasException(e)
        }
    }

    override fun onDestroyView() {
        try {
            removeUI()
        } catch (e: RuntimeException) {
            logError(e)
        }
        super.onDestroyView()
    }

    abstract fun setup()
    abstract fun createUI(inflater: LayoutInflater, container: ViewGroup?): View?
    abstract fun uiCreated(view: View, savedInstanceState: Bundle?)
    abstract fun handeException(e: RuntimeException)
    abstract fun removeUI()

    private fun wasException(e: RuntimeException) {
        handeException(e)
        logError(e)
    }

    private fun logError(e: RuntimeException) {
        Log.e(App.TAG, e.message.toString())
    }

}