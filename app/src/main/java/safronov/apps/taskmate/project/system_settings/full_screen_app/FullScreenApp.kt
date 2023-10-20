package safronov.apps.taskmate.project.system_settings.full_screen_app

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity

interface FullScreenApp {

    fun showAppInFullScreenMode(activity: FragmentActivity)
    fun showAppInFullScreenMode(activity: AppCompatActivity)

}