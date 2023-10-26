package safronov.apps.taskmate.project.system_settings.date

import java.text.DateFormat
import java.util.Calendar

interface Date {

    fun getCurrentTime(): String

    class Base(
        private val calendar: Calendar
    ): Date {
        override fun getCurrentTime(): String {
            return DateFormat.getTimeInstance().format(calendar.time)
        }
    }

}