package com.example.compose

import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.RuntimeShader
import android.graphics.Shader
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

private const val DURATION = 3000
private const val SHADER_UNIFORMS = """
    uniform float iRatio;
    uniform shader iImage1Shader;
    uniform shader iImage2Shader;
"""
private const val SHADER_MAIN = """
    half4 main(in float2 fragCoord) {
        float4 image1 = iImage1Shader.eval(fragCoord).rgba;
        float4 image2 = iImage2Shader.eval(fragCoord).rgba;
        float3 color = image1.xyz * (1.0 - iRatio) + image2.xyz * iRatio;
        return half4(color.xyz, 1.0);
    }
"""

@ExperimentalAnimationApi
@Composable
fun SwitchImageByAlphaAnimationShaderScreen() {
    android.util.Log.d("--tag--", "SwitchImageByAlphaAnimationShaderScreen")
    val res = LocalContext.current.resources

    val leftBitmap = BitmapFactory.decodeResource(res, R.drawable.sakura02)
    val rightBitmap = BitmapFactory.decodeResource(res, R.drawable.sakura03)
    val shader = RuntimeShader(SHADER_UNIFORMS + SHADER_MAIN)
    val bitmapWidth = leftBitmap.width.toFloat()
    val bitmapHeight = leftBitmap.height.toFloat()

    val leftBitmapShader = BitmapShader(leftBitmap, Shader.TileMode.DECAL, Shader.TileMode.DECAL)
    shader.setInputBuffer("iImage1Shader", leftBitmapShader)

    val rightBitmapShader = BitmapShader(rightBitmap, Shader.TileMode.DECAL, Shader.TileMode.DECAL)
    shader.setInputBuffer("iImage2Shader", rightBitmapShader)

    SwitchImageByAlphaAnimationShader(
        shader = shader,
        width = with(LocalDensity.current) {
            val dp = bitmapWidth.dp.toPx()
            val resultWidth = bitmapWidth / dp
            bitmapWidth * resultWidth
        },
        height = with(LocalDensity.current) {
            val dp = bitmapHeight.dp.toPx()
            val resultHeight = bitmapHeight / dp
            bitmapHeight * resultHeight
        }
    )
}

@ExperimentalAnimationApi
@Composable
private fun SwitchImageByAlphaAnimationShader(
    shader: RuntimeShader,
    width: Float,
    height: Float,
) {
    android.util.Log.d("--tag--", "SwitchImageByAlphaAnimationShader")
    val shaderBrush = ShaderBrush(shader)
    val animatable = remember { Animatable(0f) }

    LaunchedEffect(true) {
        launch {
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = DURATION),
                    repeatMode = RepeatMode.Reverse
                )
            )
        }
    }

    Surface(
        modifier = Modifier
            .width(width = width.dp)
            .height(height = height.dp)
    ) {
        AnimatedContent(targetState = animatable) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                shader.setFloatUniform("iRatio", it.value)
                drawRect(shaderBrush)
            }
            Text(text = "${it.value}")
        }
    }
}

@Preview
@ExperimentalAnimationApi
@Composable
fun PreviewSwitchImageByAlphaAnimationShaderScreen() {
    SwitchImageByAlphaAnimationShader(RuntimeShader(""), 0f, 0f)
}
