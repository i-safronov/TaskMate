package safronov.apps.taskmate.project.system_settings.ui.bottom_sheet

import android.content.Context
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialog
import safronov.apps.taskmate.R

interface BottomSheet {

    fun showBottomSheet(view: View)
    fun dismissBottomSheet()

    //TODO check, this class works correctly?
    class Base(
        private val context: Context,
        private val bottomSheetTheme: Int = R.style.bottom_sheet_dialog_theme
    ): BottomSheet {

        private var bottomSheet: BottomSheetDialog? = null

        override fun showBottomSheet(view: View) {
            bottomSheet = BottomSheetDialog(context, bottomSheetTheme)
            bottomSheet!!.setContentView(view)
            bottomSheet!!.show()
        }

        override fun dismissBottomSheet() {
            bottomSheet?.dismiss()
        }

    }

}