package safronov.apps.taskmate.project.system_settings.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface DispatchersList {

    fun io(): CoroutineDispatcher
    fun ui(): CoroutineDispatcher

    class Base: DispatchersList {
        override fun io(): CoroutineDispatcher {
            return Dispatchers.IO
        }

        override fun ui(): CoroutineDispatcher {
            return Dispatchers.Main
        }
    }

}