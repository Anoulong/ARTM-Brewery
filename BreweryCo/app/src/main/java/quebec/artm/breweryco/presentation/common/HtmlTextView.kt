package quebec.artm.breweryco.presentation.common

import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.text.util.LinkifyCompat

@Composable
fun HtmlTextView(
    modifier: Modifier,
    htmlText: String? = String()
) {
    val context = LocalContext.current
    val customLinkifyText = remember { TextView(context) }

    AndroidView(
        factory = { customLinkifyText },
        modifier = modifier,
    ) { textView ->
        textView.text = HtmlCompat.fromHtml(htmlText.orEmpty(), HtmlCompat.FROM_HTML_MODE_COMPACT)
        textView.textSize = 16F
        textView.setTextIsSelectable(true)
        textView.ellipsize = TextUtils.TruncateAt.END
        textView.isSingleLine = false
        textView.movementMethod = LinkMovementMethod.getInstance()
        LinkifyCompat.addLinks(textView, Linkify.WEB_URLS)
    }
}
