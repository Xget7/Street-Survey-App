package com.app.relevamientoapp.view

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EventDialog(
    modifier: Modifier = Modifier,
    @SuppressLint("SupportAnnotationUsage") @StringRes errorMessage: String?,
    onDismiss: (() -> Unit)? = null
) {
    AlertDialog(
        modifier = modifier
            .padding(16.dp),
        onDismissRequest = { onDismiss?.invoke() },
        title = {
            Text(
                "Error",
                style = TextStyle(
                    color = androidx.compose.ui.graphics.Color.Red,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        },
        text = {
            errorMessage?.let {
                Text(
                    text = it,
                    style = TextStyle(
                        color = MaterialTheme.colors.primaryVariant,
                        fontSize = 16.sp
                    )
                )
            }

        },
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { onDismiss?.invoke() }) {
                    Text(text = "Accept", style = MaterialTheme.typography.button)
                }
            }
        },
        backgroundColor = MaterialTheme.colors.background
    )
}