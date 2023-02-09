package app.shamilton.sigmonled.ui.scaffold

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import app.shamilton.sigmonled.ui.pages.Pages
import app.shamilton.sigmonled.core.bluetooth.DeviceManager
import com.badoo.reaktive.observable.subscribe
import kotlinx.coroutines.launch

@Composable
fun Menu(navController: NavHostController, deviceManager: DeviceManager) {
    val viewModel = deviceManager.getViewModel()
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth().padding(12.dp),
    ) {
        Text(
            text = "SigmonLED",
            style = MaterialTheme.typography.h5,
        )
        Text(
            text = if(viewModel.isConnected) {
                try {
                    "Connected to ${deviceManager.connectedDevice?.name}"
                } catch (se: SecurityException) {
                    "Connected to ${deviceManager.connectedDevice?.address}"
                }
            } else if(viewModel.isConnecting) {
                "Connecting..."
            } else if(viewModel.isDisconnecting) {
                "Disconnecting..."
            } else {
                "Disconnected"
            },
            modifier = Modifier.alpha(0.5f),
        )
        Divider(modifier = Modifier.padding(vertical = 12.dp))

        var currentPage by remember { mutableStateOf(AppScaffold.currentPage) }
        AppScaffold.onPageNavigation.subscribe { currentPage = it }
        for(page in Pages.values()) {
            TextButton(
                onClick = {
                    navController.navigate(page.route)
                    val drawerState = AppScaffold.scaffoldState.drawerState
                    AppScaffold.scope.launch {
                        drawerState.close()
                    }
                },
                colors = if(currentPage == page) {
                    // Show transparent background if this is the current page
                    ButtonDefaults.textButtonColors(
                        backgroundColor = MaterialTheme.colors.primarySurface.copy(alpha = 0.2f),
                    )
                } else {
                   ButtonDefaults.textButtonColors()
                },
                modifier = Modifier.align(Alignment.Start).fillMaxWidth(),
                enabled = !page.disableOnDisconnect || viewModel.isConnected
            ) {
                Icon(page.icon, page.displayName)
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = page.displayName, fontWeight = FontWeight.Bold)
            }
        }
    }
}
