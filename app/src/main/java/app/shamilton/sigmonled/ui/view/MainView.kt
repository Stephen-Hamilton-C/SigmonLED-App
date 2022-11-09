package app.shamilton.sigmonled.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import app.shamilton.sigmonled.ui.topbar.TopBar

@Composable
fun MainView() {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopBar(scaffoldState, coroutineScope) },
        drawerContent = { MenuDrawer() },
    ) { padding ->
        Column(Modifier.padding(padding)) {

        }
    }
}
