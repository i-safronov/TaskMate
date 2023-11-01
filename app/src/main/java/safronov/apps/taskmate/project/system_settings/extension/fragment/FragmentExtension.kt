package safronov.apps.taskmate.project.system_settings.extension.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import safronov.apps.taskmate.R
import safronov.apps.taskmate.project.app.App
import safronov.apps.taskmate.project.di.component.AppComponent
import safronov.apps.taskmate.project.ui.fragment.error.FragmentError

fun Fragment.requireAppComponent(): AppComponent {
    return (requireContext().applicationContext as App).getAppComponent()
}

fun Fragment.requireMainContentNavController(): NavController {
    val topLevelHost = requireActivity().supportFragmentManager.findFragmentById(R.id.all_content_container) as NavHostFragment?
    return topLevelHost?.navController ?: findNavController()
}

fun Fragment.goToFragmentErrorFromHomePage(errorMessage: String) {
    requireMainContentNavController().navigate(
        R.id.action_fragmentHomePage_to_fragmentError,
        bundleOf(
            FragmentError.ERROR_MESSAGE to errorMessage
        )
    )
}

fun Fragment.navigate(actionId: Int, args: Bundle? = null) {
    findNavController().navigate(actionId, args)
}

fun Fragment.navigateAndDeletePrevFragment(
    actionId: Int, args: Bundle? = null, currentFragmentId: Int
) {
    findNavController().navigate(
        resId = actionId,
        args = args,
        navOptions = navOptions {
            popUpTo(currentFragmentId) {
                inclusive = true
            }
        }
    )
}

fun Fragment.requireHomePageToolBar(): Toolbar {
    return requireActivity().findViewById(R.id.included_fragment_home_page_toolbar)
}

fun Fragment.setOnMenuItemClickListenerOnHomePageToolBar(
    pinTask: () -> Unit,
    chooseTaskCategory: () -> Unit
) {
    requireHomePageToolBar().setOnMenuItemClickListener {
        var handled = false
        if (it.itemId == R.id.pin_task) {
            pinTask.invoke()
            handled = true
        } else if (it.itemId == R.id.choose_category) {
            chooseTaskCategory.invoke()
            handled = true
        }
        handled
    }
}

fun Fragment.focusOnViewAndShowKeyboard(currentView: SearchView) {
    currentView.requestFocus()
    val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.showSoftInput(currentView, InputMethodManager.SHOW_FORCED)
    currentView.requestFocus()
    currentView.setOnQueryTextFocusChangeListener { view, hasFocus ->
        if (hasFocus) {
            showInputMethod(view.findFocus())
        }
    }
}

fun Fragment.showInputMethod(view: View) {
    val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.showSoftInput(view, 0)
}

fun Fragment.inflateMenuOnHomePageToolBar(
    menuId: Int
) {
    val toolBar = requireHomePageToolBar()
    toolBar.inflateMenu(menuId)
}

fun Fragment.removeMenuFromHomePageToolBar() {
    requireHomePageToolBar().menu.clear()
}