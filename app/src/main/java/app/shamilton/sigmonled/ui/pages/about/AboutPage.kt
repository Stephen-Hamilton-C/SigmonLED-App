package app.shamilton.sigmonled.ui.pages.about

import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun AboutPage(modifier: Modifier) {
    Column(modifier = modifier) {
        val context = LocalContext.current
        val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.packageManager.getPackageInfo(context.packageName, PackageManager.PackageInfoFlags.of(0))
        } else {
            context.packageManager.getPackageInfo(context.packageName, 0)
        }

        // TODO: Show icon
        Text("SigmonLED")
        Text("Version ${packageInfo.versionName}")
        Text("Written by Stephen-Hamilton-C")
        Text("Source code can be found at https://github.com/Stephen-Hamilton-C/SigmonLED")
        Text("This app and its source code are protected under the GPLv3 license. See the link above for the full license.")
    }
}
