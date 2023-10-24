package safronov.apps.taskmate.project.system_settings.extension.fragment

import android.os.Bundle
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

fun Fragment.goToFragmentError(errorMessage: String) {
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

fun Fragment.inflateMenuOnHomePageToolBar(
    menuId: Int
) {
    val toolBar = requireActivity().findViewById<Toolbar>(R.id.included_fragment_home_page_toolbar)
    toolBar.inflateMenu(menuId)
}