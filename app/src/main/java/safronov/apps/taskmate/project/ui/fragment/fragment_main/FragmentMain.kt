package safronov.apps.taskmate.project.ui.fragment.fragment_main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import safronov.apps.taskmate.R
import safronov.apps.taskmate.databinding.BottomSheetChooseTaskTypeBinding
import safronov.apps.taskmate.databinding.FragmentMainBinding
import safronov.apps.taskmate.project.system_settings.extension.fragment.goToFragmentError
import safronov.apps.taskmate.project.system_settings.extension.fragment.inflateMenuOnHomePageToolBar
import safronov.apps.taskmate.project.system_settings.extension.fragment.requireAppComponent
import safronov.apps.taskmate.project.system_settings.fragment.FragmentBase
import safronov.apps.taskmate.project.system_settings.ui.bottom_sheet.BottomSheet
import javax.inject.Inject

class FragmentMain : FragmentBase() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var bottomSheet: BottomSheet

    override fun createUI(inflater: LayoutInflater, container: ViewGroup?): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setup() {
        requireAppComponent().inject(this)
    }

    override fun uiCreated(view: View, savedInstanceState: Bundle?) {
        inflateMenuOnHomePageToolBar(menuId = R.menu.fragment_main_toolbar_menu)
        binding.animateFbAddTask.startRippleAnimation()
        fbAddTaskOnClickListener()
    }

    override fun handeException(e: RuntimeException) {
        goToFragmentError(e.message.toString())
    }

    override fun removeUI() {
        _binding = null
    }

    private fun fbAddTaskOnClickListener() {
        binding.fbAddTask.setOnClickListener {
            val bottomView = BottomSheetChooseTaskTypeBinding.inflate(layoutInflater)
            val bottomSheet = bottomSheet.createBottomSheet(view = bottomView.root)

        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentMain()
    }

}