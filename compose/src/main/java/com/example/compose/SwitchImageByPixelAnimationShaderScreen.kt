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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

private const val DURATION = 10000
private const val SHADER_UNIFORMS = """
    uniform shader iImage1Shader;
    uniform shader iImage2Shader;
    uniform float2 iPosition;
"""
private const val SHADER_MAIN = """
    half4 main(in float2 fragCoord) {
        float4 image1 = iImage1Shader.eval(fragCoord).rgba;
        float4 image2 = iImage2Shader.eval(fragCoord).rgba;
        if (iPosition.y > fragCoord.y) {
            image1.xyz = image2.xyz;
        } else if (iPosition.x > fragCoord.x && iPosition.y + 30 > fragCoord.y) {
            image1.xyz = image2.xyz;
        }
        return half4(image1.xyz, 1.0);
    }
"""

@ExperimentalAnimationApi
@Composable
fun SwitchImageByPixelAnimationShaderScreen() {
    android.util.Log.d("--tag--", "SwitchImageByPixelAnimationShaderScreen")
    val res = LocalContext.current.resources

    val beforeBitmap = BitmapFactory.decodeResource(res, R.drawable.sakura01)
    val afterBitmap = BitmapFactory.decodeResource(res, R.drawable.sakura03)
    val shader = RuntimeShader(SHADER_UNIFORMS + SHADER_MAIN)
    val bitmapWidth = beforeBitmap.width.toFloat()
    val bitmapHeight = beforeBitmap.height.toFloat()

    val beforeBitmapShader = BitmapShader(beforeBitmap, Shader.TileMode.DECAL, Shader.TileMode.DECAL)
    shader.setInputBuffer("iImage1Shader", beforeBitmapShader)

    val afterBitmapShader = BitmapShader(afterBitmap, Shader.TileMode.DECAL, Shader.TileMode.DECAL)
    shader.setInputBuffer("iImage2Shader", afterBitmapShader)

    SwitchImageByPixelAnimationShader(
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
private fun SwitchImageByPixelAnimationShader(
    shader: RuntimeShader,
    width: Float,
    height: Float,
) {
    android.util.Log.d("--tag--", "SwitchImageByPixelAnimationShader")
    val shaderBrush = ShaderBrush(shader)
    val animatable = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(true) {
        launch {
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = DURATION),
                    repeatMode = RepeatMode.Restart
                )
            )
        }
    }

    var positionX = 0
    var positionY = 0
    Surface(
        modifier = Modifier
            .width(width = width.dp)
            .height(height = height.dp)
    ) {
        AnimatedContent(targetState = animatable) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                shader.setFloatUniform("iPosition", positionX.toFloat(), positionY.toFloat())
                drawRect(shaderBrush)
                positionX += 30
                if (positionX > width.dp.toPx()) {
                    positionY += 30

                    if (positionX >= width.dp.toPx() && positionY >= height.dp.toPx() + 30) {
                        coroutineScope.launch {
                            animatable.stop()
                        }
                    }
                    positionX = 0
                }
            }
            Column(modifier = Modifier.fillMaxSize()) {
                Text(text = "${it.value}")
                Text(text = "positionX: ${positionX}")
                Text(text = "positionY: ${positionY}")
            }
        }
    }
}

@Preview
@ExperimentalAnimationApi
@Composable
fun PreviewSwitchImageByPixelAnimationShader() {
    SwitchImageByPixelAnimationShader(RuntimeShader(""), 0f, 0f)
}
