package by.mrpetchenka.testkotlinwasm

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource

import testkotlinwasm.composeapp.generated.resources.Res
import testkotlinwasm.composeapp.generated.resources.compose_multiplatform

@Composable
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        
        // Interaction source for the bouncy button
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed by interactionSource.collectIsPressedAsState()
        
        // Spring animation for button scale
        val buttonScale by animateFloatAsState(
            targetValue = if (isPressed) 0.9f else 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
            label = "buttonBounce"
        )

        // Gradient background
        val backgroundBrush = Brush.linearGradient(
            colors = listOf(
                Color(0xFF64B5F6), 
                Color(0xFFBA68C8)  
            )
        )

        Column(
            modifier = Modifier
                .background(backgroundBrush)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { showContent = !showContent },
                modifier = Modifier.scale(buttonScale),
                interactionSource = interactionSource
            ) {
                Text(if (showContent) "Hide magic" else "Show magic")
            }
            
            Spacer(modifier = Modifier.height(24.dp))

            AnimatedVisibility(showContent) {
                val greeting = remember { Greeting().greet() }
                
                val infiniteTransition = rememberInfiniteTransition(label = "infinite")
                val rotation by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(2000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ),
                    label = "rotation"
                )

                // Glassmorphism card effect
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White.copy(alpha = 0.2f))
                        .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(24.dp))
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.compose_multiplatform),
                            contentDescription = "Compose Logo",
                            modifier = Modifier
                                .size(100.dp)
                                .rotate(rotation)
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Text(
                            text = "Compose: $greeting",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
