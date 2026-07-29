package com.iptv.master.player

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

data class GestureCallbacks(
    val onBrightnessChange: (Float) -> Unit = {},
    val onVolumeChange: (Float) -> Unit = {},
    val onDoubleTapLeft: () -> Unit = {},
    val onDoubleTapRight: () -> Unit = {},
    val onSingleTap: () -> Unit = {}
)

fun Modifier.playerGestureHandler(
    callbacks: GestureCallbacks,
    enabled: Boolean = true,
    sensitivity: Float = 0.01f
): Modifier {
    if (!enabled) return this
    return this.then(
        pointerInput(Unit) {
            detectVerticalDragGestures(
                onDragEnd = { },
                onVerticalDrag = { change, dragAmount ->
                    change.consume()
                    val x = change.position.x
                    val totalWidth = size.width.toFloat()
                    val normalizedX = x / totalWidth
                    val delta = -dragAmount * sensitivity

                    if (normalizedX < 0.5f) {
                        callbacks.onBrightnessChange(delta)
                    } else {
                        callbacks.onVolumeChange(delta)
                    }
                }
            )
        }
    ).then(
        pointerInput(Unit) {
            detectDoubleTapAndSingleTap(
                totalWidth = size.width.toFloat(),
                onDoubleTapLeft = callbacks.onDoubleTapLeft,
                onDoubleTapRight = callbacks.onDoubleTapRight,
                onSingleTap = callbacks.onSingleTap
            )
        }
    )
}

private suspend fun PointerInputScope.detectDoubleTapAndSingleTap(
    totalWidth: Float,
    onDoubleTapLeft: () -> Unit,
    onDoubleTapRight: () -> Unit,
    onSingleTap: () -> Unit
) {
    detectTapGestures(
        onDoubleTap = { offset ->
            if (offset.x < totalWidth / 2) {
                onDoubleTapLeft()
            } else {
                onDoubleTapRight()
            }
        },
        onTap = { onSingleTap() }
    )
}

@Composable
fun PlayerGestureHandler(
    onBrightnessChange: (Float) -> Unit,
    onVolumeChange: (Float) -> Unit,
    onDoubleTapLeft: () -> Unit,
    onDoubleTapRight: () -> Unit,
    onSingleTap: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    val callbacks = GestureCallbacks(
        onBrightnessChange = onBrightnessChange,
        onVolumeChange = onVolumeChange,
        onDoubleTapLeft = onDoubleTapLeft,
        onDoubleTapRight = onDoubleTapRight,
        onSingleTap = onSingleTap
    )
    androidx.compose.material3.Surface(
        modifier = modifier.playerGestureHandler(callbacks = callbacks, enabled = enabled),
        color = androidx.compose.ui.graphics.Color.Transparent
    ) { }
}
