package com.example.compose

import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.RuntimeShader
import android.graphics.Shader
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private const val SHADER_UNIFORMS = """
    uniform shader iImage1Shader;
    uniform shader iImage2Shader;
    uniform float2 iResolution;
    uniform float2 iRatio;
"""
private const val SHADER_MAIN = """
    half4 main(in float2 fragCoord) {
        float4 image1 = iImage1Shader.eval(fragCoord).rgba;
        float4 image2 = iImage2Shader.eval(fragCoord).rgba;
        
        if (iResolution.x * iRatio.x <= fragCoord.x && iResolution.y * iRatio.y <= fragCoord.y) {
            return half4(image1);
        } else {
            return half4(image2);
        }
    }
"""

@ExperimentalAnimationApi
@Composable
fun SwitchImageByRatioShaderScreen() {
    android.util.Log.d("--tag--", "SwitchImageByRatioShaderScreen")
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

    shader.setFloatUniform("iResolution", bitmapWidth, bitmapHeight)
    shader.setFloatUniform("iRatio", 0f, 0f)

    SwitchImageByRatioShader(
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
private fun SwitchImageByRatioShader(
    shader: RuntimeShader,
    width: Float,
    height: Float,
) {
    android.util.Log.d("--tag--", "SwitchImageByRatioShader")
    val shaderBrush = ShaderBrush(shader)
    val ratioX = remember { mutableStateOf(0f) }
    val ratioY = remember { mutableStateOf(0f) }

    Surface(
        modifier = Modifier
            .width(width = width.dp)
            .height(height = height.dp + 144.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Canvas(modifier = Modifier
                    .width(width.dp)
                    .height(height.dp)
                ) {
                    shader.setFloatUniform("iRatio", ratioX.value, ratioY.value)
                    drawRect(shaderBrush)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "ratioX: ${ratioX.value}")
            Slider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .padding(start = 32.dp, end = 32.dp),
                value = ratioX.value,
                onValueChange = {
                    ratioX.value = it
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "ratioY: ${ratioY.value}")
            Slider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .padding(start = 32.dp, end = 32.dp),
                value = ratioY.value,
                onValueChange = {
                    ratioY.value = it
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview
@ExperimentalAnimationApi
@Composable
fun PreviewSwitchImageByRatioShaderScreen() {
    SwitchImageByRatioShader(RuntimeShader(""), 0f, 0f)
}
