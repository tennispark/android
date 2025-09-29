package com.luckydut97.tennispark.core.ui.components.common

import android.util.Patterns
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle

/**
 * 텍스트 내 URL을 감지해 클릭 가능한 링크로 표시하는 컴포저블.
 */
@Composable
fun LinkifiedText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
    linkColor: Color = Color(0xFF5AB97D),
    linkDecoration: TextDecoration = TextDecoration.Underline,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    onLinkClick: ((String) -> Unit)? = null,
    onTextClick: (() -> Unit)? = null
) {
    val uriHandler = LocalUriHandler.current
    val annotatedText = remember(text, linkColor, linkDecoration) {
        val matcher = Patterns.WEB_URL.matcher(text)
        buildAnnotatedString {
            var lastIndex = 0
            while (matcher.find()) {
                val start = matcher.start()
                val end = matcher.end()

                if (start > lastIndex) {
                    append(text.substring(lastIndex, start))
                }

                val url = text.substring(start, end)
                pushStringAnnotation(tag = LINK_TAG, annotation = url)
                withStyle(style = SpanStyle(color = linkColor, textDecoration = linkDecoration)) {
                    append(url)
                }
                pop()

                lastIndex = end
            }

            if (lastIndex < text.length) {
                append(text.substring(lastIndex))
            }
        }
    }

    ClickableText(
        modifier = modifier,
        text = annotatedText,
        style = style,
        maxLines = maxLines,
        overflow = overflow
    ) { offset ->
        val annotation = annotatedText.getStringAnnotations(LINK_TAG, offset, offset).firstOrNull()
        if (annotation != null) {
            val url = annotation.item
            if (onLinkClick != null) {
                onLinkClick(url)
            } else {
                runCatching { uriHandler.openUri(url) }
            }
        } else {
            onTextClick?.invoke()
        }
    }
}

private const val LINK_TAG = "URL"
