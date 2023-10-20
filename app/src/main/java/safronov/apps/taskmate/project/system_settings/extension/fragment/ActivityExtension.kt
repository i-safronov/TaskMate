package safronov.apps.taskmate.project.system_settings.extension.fragment

import android.app.Activity
import safronov.apps.taskmate.project.app.App
import safronov.apps.taskmate.project.di.component.AppComponent

fun Activity.requireAppComponent(): AppComponent {
    return (this.applicationContext as App).getAppComponent()
}