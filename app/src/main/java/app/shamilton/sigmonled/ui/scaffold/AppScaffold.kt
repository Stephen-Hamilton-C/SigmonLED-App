package app.shamilton.sigmonled.ui.scaffold

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.shamilton.sigmonled.core.ArduinoCommander
import app.shamilton.sigmonled.ui.bottombar.AppBottomBar
import app.shamilton.sigmonled.ui.pages.Pages
import app.shamilton.sigmonled.ui.pages.about.AboutPage
import app.shamilton.sigmonled.ui.pages.devices.DevicesPage
import app.shamilton.sigmonled.ui.pages.palette.PalettePage
import app.shamilton.sigmonled.ui.pages.staticcolor.StaticColorPage
import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

object AppScaffold {

    var scaffoldState = ScaffoldState(DrawerState(DrawerValue.Closed), SnackbarHostState())
        private set
    var scope = CoroutineScope(Dispatchers.Default)
        private set
    var currentPage = Pages.DEVICES
        private set(value) {
            if(field != value) {
                field = value
                onPageNavigation.onNext(value)
            }
        }
    val onPageNavigation = BehaviorSubject(currentPage)

    @Composable
    fun Component(commander: ArduinoCommander) {
        scaffoldState = rememberScaffoldState()
        scope = rememberCoroutineScope()
        val navController = rememberNavController()

        // Go to Devices page if device disconnecting while in a connected-only page
        commander.deviceManager.onDeviceDisconnected.subscribe {
            if(currentPage.disableOnDisconnect) {
                navController.navigate(Pages.DEVICES.route)
            }
        }

        Scaffold(
            scaffoldState = scaffoldState,
            topBar = { AppTopBar(commander) },
            bottomBar = { AppBottomBar.Component(commander.deviceManager) },
            drawerContent = { Menu(navController, commander.deviceManager) },
            floatingActionButton = { FloatingActionButtons(commander = commander) },
            floatingActionButtonPosition = FabPosition.End,
        ) {
            Content(Modifier.padding(it), navController, commander)
        }
    }

    @Composable
    private fun Content(modifier: Modifier, navController: NavHostController, commander: ArduinoCommander) {
        NavHost(navController = navController, startDestination = currentPage.route) {
            composable(Pages.DEVICES.route) {
                currentPage = Pages.DEVICES
                DevicesPage(modifier, commander.deviceManager)
            }
            composable(Pages.STATIC_COLOR.route) {
                currentPage = Pages.STATIC_COLOR
                StaticColorPage(modifier, commander)
            }
            composable(Pages.PALETTE.route) {
                currentPage = Pages.PALETTE
                PalettePage(modifier, commander)
            }
            composable(Pages.ABOUT.route) {
                currentPage = Pages.ABOUT
                AboutPage(modifier)
            }
        }
    }

}
