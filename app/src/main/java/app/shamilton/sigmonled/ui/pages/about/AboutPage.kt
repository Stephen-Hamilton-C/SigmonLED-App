package app.shamilton.sigmonled.ui.pages.about

import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import app.shamilton.sigmonled.ui.theme.defaultTextStyle

@Composable
fun AboutPage(modifier: Modifier) {
    Column(modifier = modifier) {
        // Get packageInfo for versionName
        // Source: https://stackoverflow.com/a/6593822
        val context = LocalContext.current
        val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.packageManager.getPackageInfo(context.packageName, PackageManager.PackageInfoFlags.of(0))
        } else {
            context.packageManager.getPackageInfo(context.packageName, 0)
        }

        // Hyperlink
        // Source: https://stackoverflow.com/a/65656351
        val uriHandler = LocalUriHandler.current
        val annotatedLinkString: AnnotatedString = buildAnnotatedString {

            val link = "https://github.com/Stephen-Hamilton-C/SigmonLED-App"
            val str = "Source code can be found at $link"
            val startIndex = str.indexOf(link)
            val endIndex = startIndex + link.length
            append(str)
            addStyle(
                style = SpanStyle(
                    color = Color(0xff64B5F6),
                    textDecoration = TextDecoration.Underline,
                ), start = startIndex, end = endIndex
            )

            // attach a string annotation that stores a URL to the text "link"
            addStringAnnotation(
                tag = "URL",
                annotation = link,
                start = startIndex,
                end = endIndex
            )

        }

        // TODO: Show app icon
        Text("SigmonLED")
        Text("Version ${packageInfo.versionName}")
        Text("Written by Stephen-Hamilton-C")
        ClickableText(
            text = annotatedLinkString,
            onClick = {
                annotatedLinkString
                    .getStringAnnotations("URL", it, it)
                    .firstOrNull()?.let { stringAnnotation ->
                        uriHandler.openUri(stringAnnotation.item)
                    }
            },
            style = defaultTextStyle,
        )

        Text("This app and its source code are protected under the GPLv3 license. See the link above for the full license.")
    }
}
