package safronov.apps.taskmate.project.system_settings.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import safronov.apps.taskmate.project.app.App

abstract class FragmentBase(): Fragment() {

    override fun onStart() {
        super.onStart()
        try {
            starting()
        } catch (e: RuntimeException) {
            wasException(e)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return try {
            prepareArguments()
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

    abstract fun starting()
    abstract fun createUI(inflater: LayoutInflater, container: ViewGroup?): View?
    abstract fun prepareArguments()
    abstract fun uiCreated(view: View, savedInstanceState: Bundle?)
    abstract fun handeException(e: RuntimeException)
    abstract fun removeUI()

    override fun onDestroyView() {
        removeUI()
        super.onDestroyView()
    }

    private fun wasException(e: RuntimeException) {
        handeException(e)
        logError(e)
    }

    private fun logError(e: RuntimeException) {
        Log.e(App.TAG, e.message.toString())
    }

}