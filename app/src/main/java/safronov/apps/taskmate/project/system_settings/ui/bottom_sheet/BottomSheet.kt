package safronov.apps.taskmate.project.system_settings.ui.bottom_sheet

import android.content.Context
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialog
import safronov.apps.taskmate.R

interface BottomSheet {

    fun createBottomSheet(view: View): BottomSheetDialog

    class Base(
        private val context: Context,
        private val bottomSheetTheme: Int = R.style.bottom_sheet_dialog_theme
    ): BottomSheet {

        override fun createBottomSheet(view: View): BottomSheetDialog {
            val bottomSheet = BottomSheetDialog(context, bottomSheetTheme)
            bottomSheet.setContentView(view)
            return bottomSheet
        }

    }

}