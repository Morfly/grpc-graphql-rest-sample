package io.morfly.client.ui.comments

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun SendComment(onSendComment: (comment: String) -> Unit) {
    var text by remember { mutableStateOf("") }

    Row(
        Modifier
            .fillMaxWidth()
    ) {
        TextField(
            value = text,
            onValueChange = { text = it },
            Modifier.weight(1f)
        )
        Icon(
            Icons.Default.Send,
            contentDescription = "Send comment",
            Modifier
                .clickable {
                    onSendComment(text)
                    text = ""
                }
                .padding(16.dp)
        )
    }
}