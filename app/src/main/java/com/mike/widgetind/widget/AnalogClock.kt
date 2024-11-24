package com.mike.widgetind.widget

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.util.Calendar

@Composable
fun AnalogClock(
    clockSize: Dp = 120.dp,
    hourHandColor: Color = Color.Black,
    minuteHandColor: Color = Color.DarkGray,
    secondHandColor: Color = Color.Red,
    faceColor: Color = Color.White
) {
    val currentTime = Calendar.getInstance()
    val hour = currentTime.get(Calendar.HOUR) % 12
    val minute = currentTime.get(Calendar.MINUTE)
    val second = currentTime.get(Calendar.SECOND)

    Canvas(modifier = Modifier.fillMaxSize()) {
        val radius = size.minDimension / 2
        val center = Offset(size.width / 2, size.height / 2)

        // Draw clock face
        drawCircle(
            color = faceColor,
            radius = radius,
            center = center,
            style = Stroke(width = 6f)
        )

        // Draw clock hands
        val hourAngle = (hour + minute / 60f) * 30f
        val minuteAngle = (minute + second / 60f) * 6f
        val secondAngle = second * 6f

        val hourHandLength = radius * 0.5f
        val minuteHandLength = radius * 0.7f
        val secondHandLength = radius * 0.9f

        // Hour hand
        drawLine(
            color = hourHandColor,
            start = center,
            end = Offset(
                x = center.x + hourHandLength * kotlin.math.sin(Math.toRadians(hourAngle.toDouble())).toFloat(),
                y = center.y - hourHandLength * kotlin.math.cos(Math.toRadians(hourAngle.toDouble())).toFloat()
            ),
            strokeWidth = 8f,
            cap = StrokeCap.Round
        )

        // Minute hand
        drawLine(
            color = minuteHandColor,
            start = center,
            end = Offset(
                x = center.x + minuteHandLength * kotlin.math.sin(Math.toRadians(minuteAngle.toDouble())).toFloat(),
                y = center.y - minuteHandLength * kotlin.math.cos(Math.toRadians(minuteAngle.toDouble())).toFloat()
            ),
            strokeWidth = 6f,
            cap = StrokeCap.Round
        )

        // Second hand
        drawLine(
            color = secondHandColor,
            start = center,
            end = Offset(
                x = center.x + secondHandLength * kotlin.math.sin(Math.toRadians(secondAngle.toDouble())).toFloat(),
                y = center.y - secondHandLength * kotlin.math.cos(Math.toRadians(secondAngle.toDouble())).toFloat()
            ),
            strokeWidth = 4f,
            cap = StrokeCap.Round
        )
    }
}
