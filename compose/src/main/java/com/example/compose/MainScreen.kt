package com.example.compose

import android.graphics.RuntimeShader
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

private const val DURATION = 4000f
private const val COLOR_SHADER_SRC =
    """
        uniform float2 iResolution;
        uniform float iTime;
        uniform float iDuration;
        half4 main(in float2 fragCoord) {
            float2 scaled = abs(1.0-mod(fragCoord/iResolution.xy+iTime/(iDuration/2.0),2.0));
            return half4(scaled, 0, 1.0);
        }
    """

@Composable
fun MainScreen() {
    CircleShader()
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CircleShader() {
    val colorShader = RuntimeShader(COLOR_SHADER_SRC)
    val shaderBrush = remember { ShaderBrush(colorShader) }

    val animatable = remember { Animatable(0f) }

    LaunchedEffect(true) {
        launch { animatable.animateTo(1f, animationSpec = tween(2000)) }
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        colorShader.setFloatUniform("iDuration", DURATION)
        AnimatedContent(targetState = animatable) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .background(Color.Blue)
            ) {
                colorShader.setFloatUniform("iResolution", size.width, size.height)
                colorShader.setFloatUniform("iTime", animatable.value * 3000)
                drawCircle(brush = shaderBrush)
            }
            Text(text = "${animatable.value}")
        }
    }
}

@Preview
@Composable
fun PreviewCircleShader() {
    CircleShader()
}
