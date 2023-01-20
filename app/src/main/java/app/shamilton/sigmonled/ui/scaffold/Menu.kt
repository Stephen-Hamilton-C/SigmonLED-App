package app.shamilton.sigmonled.ui.scaffold

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import app.shamilton.sigmonled.ui.pages.Pages
import app.shamilton.sigmonled.core.bluetooth.DeviceManager
import kotlinx.coroutines.launch

@Composable
fun Menu(navController: NavHostController, deviceManager: DeviceManager) {
    val viewModel = deviceManager.getViewModel()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth(),
    ) {
        for(page in Pages.values()) {
            TextButton(
                onClick = {
                    navController.navigate(page.route)
                    val drawerState = AppScaffold.scaffoldState.drawerState
                    AppScaffold.scope.launch {
                        drawerState.close()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !page.disableOnDisconnect || viewModel.isConnected
            ) {
                Text(text = page.displayName)
            }
        }
    }
}
