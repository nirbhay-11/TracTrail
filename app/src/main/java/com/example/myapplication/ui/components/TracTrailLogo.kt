package com.example.myapplication.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TracTrailUniqueIcon(
    modifier: Modifier = Modifier,
    size: Dp = 36.dp
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary

    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(size * 0.28f))
            .background(
                Brush.linearGradient(
                    colors = listOf(primaryColor, secondaryColor)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(size * 0.18f)
        ) {
            val w = size.toPx() * 0.64f
            val h = size.toPx() * 0.64f

            // Geometric Ascending 'T' Crest & Trail
            val leftTrail = Path().apply {
                moveTo(w * 0.2f, h * 0.82f)
                lineTo(w * 0.5f, h * 0.22f)
                lineTo(w * 0.8f, h * 0.82f)
            }

            val crossBar = Path().apply {
                moveTo(w * 0.12f, h * 0.38f)
                lineTo(w * 0.88f, h * 0.38f)
            }

            drawPath(
                path = leftTrail,
                color = Color.White,
                style = Stroke(
                    width = w * 0.12f,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )

            drawPath(
                path = crossBar,
                color = Color.White,
                style = Stroke(
                    width = w * 0.11f,
                    cap = StrokeCap.Round
                )
            )

            // Star Peak Point
            drawCircle(
                color = Color.White,
                radius = w * 0.11f,
                center = Offset(w * 0.5f, h * 0.22f)
            )
        }
    }
}

@Composable
fun TracTrailTitleHeader(
    modifier: Modifier = Modifier,
    subtitleText: String? = null
) {
    Column(modifier = modifier) {
        Text(
            text = "TracTrail",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Black,
                fontSize = 22.sp,
                letterSpacing = (-0.5).sp
            ),
            color = MaterialTheme.colorScheme.onSurface
        )

        if (!subtitleText.isNullOrBlank()) {
            Text(
                text = subtitleText,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun TracTrailLogo(
    modifier: Modifier = Modifier,
    iconSize: Dp = 32.dp,
    showTagline: Boolean = false,
    subtitleText: String? = null,
    showIcon: Boolean = false
) {
    if (showIcon) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TracTrailUniqueIcon(size = iconSize)
            Spacer(modifier = Modifier.width(10.dp))
            TracTrailTitleHeader(subtitleText = subtitleText ?: if (showTagline) "Habits • Fitness • Nutrition" else null)
        }
    } else {
        TracTrailTitleHeader(
            modifier = modifier,
            subtitleText = subtitleText ?: if (showTagline) "Habits • Fitness • Nutrition" else null
        )
    }
}
