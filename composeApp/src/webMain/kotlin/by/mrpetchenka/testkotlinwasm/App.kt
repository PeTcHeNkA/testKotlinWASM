package by.mrpetchenka.testkotlinwasm

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource

import testkotlinwasm.composeapp.generated.resources.Res
import testkotlinwasm.composeapp.generated.resources.compose_multiplatform

@Composable
fun App() {
    var active by remember { mutableStateOf(false) }
    
    val infiniteTransition = rememberInfiniteTransition(label = "glitch")
    val glitchOffset by infiniteTransition.animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(50, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF050505)),
            contentAlignment = Alignment.Center
        ) {
            ScanlineOverlay()

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Cyber Button
                Box(
                    modifier = Modifier
                        .height(60.dp)
                        .width(220.dp)
                        .clickable { active = !active }
                        .drawBehind {
                            val brush = Brush.horizontalGradient(listOf(Color.Cyan, Color.Magenta, Color.Cyan))
                            val stroke = 4f
                            drawLine(brush, Offset(0f, 0f), Offset(size.width, 0f), stroke)
                            drawLine(brush, Offset(size.width, 0f), Offset(size.width, size.height), stroke)
                            drawLine(brush, Offset(size.width, size.height), Offset(0f, size.height), stroke)
                            drawLine(brush, Offset(0f, size.height), Offset(0f, 0f), stroke)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (active) "> DISCONNECT" else "> INITIALIZE",
                        color = Color.Cyan,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Spacer(modifier = Modifier.height(50.dp))

                AnimatedVisibility(
                    visible = active,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    val greeting = remember { Greeting().greet() }
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(contentAlignment = Alignment.Center) {
                            Image(
                                painter = painterResource(Res.drawable.compose_multiplatform),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(120.dp)
                                    .graphicsLayer { translationX = glitchOffset * 2; alpha = 0.5f },
                                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.Magenta)
                            )
                            Image(
                                painter = painterResource(Res.drawable.compose_multiplatform),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(120.dp)
                                    .graphicsLayer { translationX = -glitchOffset * 2; alpha = 0.5f },
                                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.Cyan)
                            )
                            Image(
                                painter = painterResource(Res.drawable.compose_multiplatform),
                                contentDescription = null,
                                modifier = Modifier.size(120.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(30.dp))

                        Text(
                            text = greeting.uppercase(),
                            color = Color.White,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .background(Color.Cyan.copy(alpha = 0.1f))
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ScanlineOverlay() {
    val infiniteTransition = rememberInfiniteTransition(label = "scanline")
    val linePos by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(4000, easing = LinearEasing))
    )

    Canvas(modifier = Modifier.fillMaxSize().graphicsLayer { alpha = 0.1f }) {
        val h = size.height
        val w = size.width
        for (i in 0..h.toInt() step 8) {
            drawLine(Color.White, Offset(0f, i.toFloat()), Offset(w, i.toFloat()), 1f)
        }
        drawRect(
            brush = Brush.verticalGradient(0f to Color.Transparent, 0.5f to Color.Cyan, 1f to Color.Transparent),
            topLeft = Offset(0f, h * linePos - 50f),
            size = androidx.compose.ui.geometry.Size(w, 100f)
        )
    }
}
