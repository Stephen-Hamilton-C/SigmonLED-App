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
import app.shamilton.sigmonled.core.bluetooth.DeviceManagerViewModel
import app.shamilton.sigmonled.ui.pages.Pages
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@Composable
fun Menu(navController: NavHostController, viewModel: DeviceManagerViewModel = viewModel()) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth(),
    ) {
        for(page in Pages.values()) {
            TextButton(
                onClick = {
                    navController.navigate(page.routeName)
                    val drawerState = AppScaffold.scaffoldState.drawerState
                    AppScaffold.scope.launch {
                        if (drawerState.isClosed)
                            drawerState.open()
                        else
                            drawerState.close()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !page.disableOnDisconnect || (page.disableOnDisconnect && viewModel.isConnected)
            ) {
                Text(text = page.displayName)
            }
        }
    }
}
