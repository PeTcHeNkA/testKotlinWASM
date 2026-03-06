package by.mrpetchenka.testkotlinwasm

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource

import testkotlinwasm.composeapp.generated.resources.Res
import testkotlinwasm.composeapp.generated.resources.compose_multiplatform

@Composable
fun App() {
    var showContent by remember { mutableStateOf(false) }
    val infiniteTransition = rememberInfiniteTransition(label = "background")
    
    // Background animation values
    val animOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(20000, easing = LinearEasing))
    )

    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0F172A)), // Deep dark blue
            contentAlignment = Alignment.Center
        ) {
            // Animated background blobs
            FloatingBlob(Color(0xFF3B82F6), offset = animOffset, size = 300f)
            FloatingBlob(Color(0xFF8B5CF6), offset = -animOffset * 0.8f, size = 250f)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize().safeContentPadding()
            ) {
                // Custom "Button" - Interactive Surface
                val interactionSource = remember { MutableInteractionSource() }
                val scale by animateFloatAsState(if (showContent) 0.95f else 1f)
                
                Surface(
                    modifier = Modifier
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) { showContent = !showContent },
                    shape = RoundedCornerShape(50),
                    color = if (showContent) Color.White.copy(alpha = 0.15f) else Color.White.copy(alpha = 0.1f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = if (showContent) "EXPLORE LESS" else "EXPLORE MORE",
                        modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.labelLarge.copy(
                            letterSpacing = 2.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                AnimatedVisibility(
                    visible = showContent,
                    enter = fadeIn() + scaleIn(initialScale = 0.8f),
                    exit = fadeOut() + scaleOut(targetScale = 0.8f)
                ) {
                    ContentCard()
                }
            }
        }
    }
}

@Composable
fun FloatingBlob(color: Color, offset: Float, size: Float) {
    Box(
        modifier = Modifier
            .offset(x = (offset % 200).dp, y = (offset % 150).dp)
            .size(size.dp)
            .blur(100.dp)
            .background(color.copy(alpha = 0.3f), CircleShape)
    )
}

@Composable
fun ContentCard() {
    val greeting = remember { Greeting().greet() }
    val rotation by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(10000, easing = LinearEasing))
    )

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(32.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(Res.drawable.compose_multiplatform),
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
                .rotate(rotation)
                .graphicsLayer {
                    shadowElevation = 20f
                }
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = greeting,
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Light,
                letterSpacing = 1.sp
            )
        )
        
        Text(
            text = "KOTLIN WASM POWERED",
            color = Color.White.copy(alpha = 0.5f),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
