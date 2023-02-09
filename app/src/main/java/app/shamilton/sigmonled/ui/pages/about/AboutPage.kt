package app.shamilton.sigmonled.ui.pages.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import app.shamilton.sigmonled.BuildConfig
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AboutPage(modifier: Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .padding(12.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        val context = LocalContext.current
        val packageManager = context.packageManager
        val appIcon = packageManager.getApplicationIcon(context.applicationInfo)
        Image(
            painter = rememberDrawablePainter(appIcon),
            contentDescription = "SigmonLED Icon",
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
        )
        Text("About SigmonLED", style = MaterialTheme.typography.h5)
        Divider(modifier = Modifier.padding(top = 12.dp))

        // Version
        ListItem(
            text = { Text("Version") },
            secondaryText = { Text(BuildConfig.VERSION_NAME) },
            icon = { Icon(Icons.Rounded.Info, null) },
        )

        // Author
        ListItem(
            text = { Text("Author") },
            secondaryText = { Text("Stephen-Hamilton-C") },
            icon = { Icon(Icons.Rounded.Person, null) },
        )

        // Source code
        val uriHandler = LocalUriHandler.current
        val sourceCodeLink = "https://github.com/Stephen-Hamilton-C/SigmonLED-App"
        ListItem(
            text = { Text("Source Code") },
            secondaryText = { Text(sourceCodeLink) },
            icon = { Icon(Icons.Rounded.Code, null) },
            modifier = Modifier.clickable {
                uriHandler.openUri(sourceCodeLink)
            },
        )
        // License
        ListItem(
            text = { Text("License") },
            secondaryText = { Text("GNU General Public License, Version 3") },
            icon = { Icon(Icons.Rounded.Description, null) },
            modifier = Modifier.clickable {
                uriHandler.openUri("https://github.com/Stephen-Hamilton-C/SigmonLED-App/blob/main/LICENSE")
            }
        )

        Divider()

        // Libraries
        for(library in Libraries.values()) {
            ListItem(
                text = { Text(library.displayName) },
                secondaryText = { Text(library.license) },
                modifier = Modifier.clickable {
                    uriHandler.openUri(library.link)
                }
            )
        }
    }
}
