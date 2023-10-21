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
            val ui = createUI(inflater, container)
            setup()
            return ui
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

    /**
     * [createUI] - the method that expects to receive a view that needs to be drawn on the screen
     * Important: do not perform any operations that are not important for the [View] display:
     * for the preparatory stages of work there is a [setup] method */
    abstract fun createUI(inflater: LayoutInflater, container: ViewGroup?): View?
    /**
     * [setup] - the method that is designed to perform light preparatory stages of the program
     * (for example: preparing dependencies or creating a request in the ViewModel)
     * Important: Do not use this method for long-running operations on the main thread,
     * as such an operation will slow down the rendering of the view.
     *
     * [setup] - is called after the [createUI] method but before passing
     * the view (which was received from [createUI]) to the [onCreateView] method  */
    abstract fun setup()
    /**
     * [uiCreated] - the method that is called when [View] has been successfully drawn
     * on the screen and in this method you can perform any operations */
    abstract fun uiCreated(view: View, savedInstanceState: Bundle?)
    /**
     * [handeException] - the method in which you are recommended to handle an error
     * that occurred during program execution, you can display any messages to the user */
    abstract fun handeException(e: RuntimeException)
    /**
     * [removeUI] - the method that is called to remove a reference to the Fragment's [View] that
     * you used during program execution
     *
     * Important: Do not ignore the execution of the [removeUI] method,
     * as this behavior can lead to memory leaks. */
    abstract fun removeUI()

    private fun wasException(e: RuntimeException) {
        handeException(e)
        logError(e)
    }

    private fun logError(e: RuntimeException) {
        Log.e(App.TAG, e.message.toString())
    }

}