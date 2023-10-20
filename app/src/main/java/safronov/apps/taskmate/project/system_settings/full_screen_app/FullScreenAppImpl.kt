package safronov.apps.taskmate.project.system_settings.full_screen_app

import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

class FullScreenAppImpl(): FullScreenApp {

    override fun showAppInFullScreenMode(activity: AppCompatActivity) {
        activity.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

}