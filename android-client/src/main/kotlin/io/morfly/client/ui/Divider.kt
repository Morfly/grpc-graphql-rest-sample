package io.morfly.client.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun Divider(modifier: Modifier = Modifier) {
    Spacer(
        modifier
            .height(1.dp)
            .fillMaxWidth()
            .background(Color.LightGray)
    )
}