package com.iptv.master.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iptv.master.domain.model.AppUpdate

@Composable
fun UpdateDialog(
    update: AppUpdate,
    onDownload: () -> Unit,
    onLater: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { if (!update.isMandatory) onLater() },
        title = {
            Text(
                text = "Update Available",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            androidx.compose.foundation.layout.Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Version ${update.latestVersion}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                androidx.compose.foundation.layout.Spacer(
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Text(
                    text = "What's new:",
                    style = MaterialTheme.typography.titleSmall
                )

                androidx.compose.foundation.layout.Spacer(
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Text(
                    text = update.changelog,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        confirmButton = {
            Button(onClick = onDownload) {
                Text("Download")
            }
        },
        dismissButton = {
            if (update.isMandatory) {
                TextButton(onClick = { /* no-op, update is mandatory */ }) {
                    Text("Close", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                OutlinedButton(onClick = onLater) {
                    Text("Later")
                }
            }
        },
        modifier = modifier
    )
}
