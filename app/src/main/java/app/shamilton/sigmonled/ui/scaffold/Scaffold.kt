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
import app.shamilton.sigmonled.ui.IComponent
import app.shamilton.sigmonled.ui.pages.Pages
import app.shamilton.sigmonled.ui.pages.devices.DeviceList
import app.shamilton.sigmonled.ui.pages.staticcolor.StaticColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

object AppScaffold : IComponent {

    var scaffoldState = ScaffoldState(DrawerState(DrawerValue.Closed), SnackbarHostState())
        private set
    var scope = CoroutineScope(Dispatchers.Default)
        private set

    @Composable
    override fun Component(commander: ArduinoCommander) {
        scaffoldState = rememberScaffoldState()
        scope = rememberCoroutineScope()
        val navController = rememberNavController()

        Scaffold(
            scaffoldState = scaffoldState,
            topBar = { AppTopBar(commander.deviceManager) },
            drawerContent = { Menu(navController, commander.deviceManager) },
            floatingActionButton = { FloatingActionButtons(commander = commander) },
            floatingActionButtonPosition = FabPosition.End,
        ) {
            Content(Modifier.padding(it), navController, commander)
        }
    }

    @Composable
    private fun Content(modifier: Modifier, navController: NavHostController, commander: ArduinoCommander) {
        NavHost(navController = navController, startDestination = Pages.DEVICES.route) {
            composable(Pages.DEVICES.route) {
                DeviceList(modifier, commander.deviceManager)
            }
            composable(Pages.STATIC_COLOR.route) {
                StaticColor(modifier, commander)
            }
        }
    }

}
