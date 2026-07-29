package com.iptv.master.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.iptv.master.domain.model.EPGProgram
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val HOUR_WIDTH = 120.dp
private val CHANNEL_HEIGHT = 80.dp
private val CHANNEL_LABEL_WIDTH = 140.dp

@Composable
fun EPGGrid(
    programs: Map<String, List<EPGProgram>>,
    onProgramClick: (EPGProgram) -> Unit,
    modifier: Modifier = Modifier
) {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(programs.entries.toList()) { (channelName, channelPrograms) ->
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .width(CHANNEL_LABEL_WIDTH)
                        .height(CHANNEL_HEIGHT)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = channelName,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .height(CHANNEL_HEIGHT)
                        .horizontalScroll(rememberScrollState())
                ) {
                    val now = System.currentTimeMillis()

                    channelPrograms.forEach { program ->
                        val startOffset = ((program.startTime % 86400000L) / 3600000f) * HOUR_WIDTH
                        val durationHours = (program.endTime - program.startTime) / 3600000f
                        val width = durationHours * HOUR_WIDTH

                        Card(
                            modifier = Modifier
                                .width(width)
                                .fillMaxHeight()
                                .padding(2.dp)
                                .then(
                                    if (now in program.startTime until program.endTime) {
                                        Modifier.clickable { onProgramClick(program) }
                                    } else {
                                        Modifier.clickable { onProgramClick(program) }
                                    }
                                ),
                            shape = RoundedCornerShape(4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (now in program.startTime until program.endTime) {
                                    MaterialTheme.colorScheme.primaryContainer
                                } else {
                                    MaterialTheme.colorScheme.surfaceVariant
                                }
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = if (now in program.startTime until program.endTime) 4.dp else 1.dp)
                        ) {
                            Column(modifier = Modifier.padding(4.dp)) {
                                Text(
                                    text = "${timeFormat.format(Date(program.startTime))} - ${timeFormat.format(Date(program.endTime))}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1
                                )
                                Text(
                                    text = program.title,
                                    style = MaterialTheme.typography.bodySmall,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (programs.values.flatten().isNotEmpty()) {
        CurrentTimeIndicator()
    }
}

@Composable
private fun CurrentTimeIndicator() {
    val now = System.currentTimeMillis()
    val hourOfDay = ((now % 86400000L) / 3600000f)
    val offsetX = CHANNEL_LABEL_WIDTH + (hourOfDay * HOUR_WIDTH)

    Canvas(modifier = Modifier.fillMaxSize()) {
        val startX = offsetX.toPx()
        drawLine(
            color = Color.Red,
            start = Offset(startX, 0f),
            end = Offset(startX, size.height),
            strokeWidth = 3f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f))
        )
    }
}
