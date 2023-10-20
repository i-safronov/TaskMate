package safronov.apps.taskmate.project.system_settings.extension.fragment

import androidx.fragment.app.Fragment
import safronov.apps.taskmate.project.app.App
import safronov.apps.taskmate.project.di.component.AppComponent

fun Fragment.requireAppComponent(): AppComponent {
    return (requireContext().applicationContext as App).getAppComponent()
}