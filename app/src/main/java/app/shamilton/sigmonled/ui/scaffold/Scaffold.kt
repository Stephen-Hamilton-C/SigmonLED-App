package app.shamilton.sigmonled.ui.scaffold

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.shamilton.sigmonled.core.ArduinoCommander
import app.shamilton.sigmonled.core.bluetooth.DeviceManager
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

    var showNav by mutableStateOf(false)

    @Composable
    override fun Component(commander: ArduinoCommander) {
        scaffoldState = rememberScaffoldState()
        scope = rememberCoroutineScope()
        val navController = rememberNavController()

        // TODO: Make nav rail appear on swipe
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = { AppTopBar(commander.deviceManager) },
            floatingActionButton = { FloatingActionButtons(commander = commander) },
            floatingActionButtonPosition = FabPosition.End,
        ) {
            val navRailPadding by animateIntAsState(if(showNav) 72 else 0)
            val contentModifier = Modifier.padding(it).padding(start = navRailPadding.dp)

            val density = LocalDensity.current
            AnimatedVisibility(
                visible = showNav,
                enter = slideInHorizontally {
                    with(density) {
                        -72.dp.roundToPx()
                    }
                },
                exit = slideOutHorizontally {
                    with(density) {
                        -72.dp.roundToPx()
                    }
                }
            ) {
                NavRail(navController, commander.deviceManager)
            }

            Content(contentModifier, navController, commander)
        }
    }

    @Composable
    private fun NavRail(navController: NavHostController, deviceManager: DeviceManager) {
        val viewModel = deviceManager.getViewModel()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        NavigationRail(
            elevation = 1.dp,
        ) {
            for (page in Pages.values()) {
                NavigationRailItem(
                    icon = { Icon(page.icon, page.displayName) },
                    label = { Text(page.displayName) },
                    selected = page.route == currentRoute,
                    alwaysShowLabel = true,
                    enabled = !page.disableOnDisconnect || viewModel.isConnected,
                    onClick = { navController.navigate(page.route) },
                )
            }
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
