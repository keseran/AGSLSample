package com.example.compose

import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.RuntimeShader
import android.graphics.Shader
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
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
    uniform shader iShader;
    uniform float2 iResolution;
    uniform float2 iCenter;
    uniform float iRadius;
    uniform float iSpiralStrength;
"""
private const val SHADER_MAIN = """
    half4 main(in float2 fragCoord) {
        float2 pos = (fragCoord * iResolution) - iCenter;
        float len = length(pos);
        if (len >= iRadius) {
            float4 image = iShader.eval(fragCoord).rgba;
            return half4(image);
        }
        
        float spiral = min(max(1.0 - (len / iRadius), 0.0), 1.0) * iSpiralStrength;
        float x = pos.x * cos(spiral) - pos.y * sin(spiral);
        float y = pos.y * sin(spiral) - pos.y * cos(spiral);
        float2 retPos = (float2(x, y) + iCenter) / iResolution;
        float4 image = iShader.eval(retPos).rgba;
        return half4(image);
    }
"""

@Composable
fun SpiralShaderScreen() {
    android.util.Log.d("--tag--", "SpiralShaderScreen")
    val res = LocalContext.current.resources

    val bitmap = BitmapFactory.decodeResource(res, R.drawable.sakura01)
    val shader = RuntimeShader(SHADER_UNIFORMS + SHADER_MAIN)
    val bitmapWidth = bitmap.width.toFloat()
    val bitmapHeight = bitmap.height.toFloat()

    val bitmapShader = BitmapShader(bitmap, Shader.TileMode.DECAL, Shader.TileMode.DECAL)
    shader.setInputBuffer("iShader", bitmapShader)
    shader.setFloatUniform("iResolution", 1f, 1f)
    shader.setFloatUniform("iCenter", bitmapWidth / 2, bitmapHeight / 2)
    shader.setFloatUniform("iRadius", 0f)
    shader.setFloatUniform("iSpiralStrength", 0f)

    SpiralShader(
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
        },
        originalWidth = bitmapWidth,
        originalHeight = bitmapHeight,
    )
}

@Composable
private fun SpiralShader(
    shader: RuntimeShader,
    width: Float,
    height: Float,
    originalWidth: Float,
    originalHeight: Float,
) {
    android.util.Log.d("--tag--", "SpiralShader")
    val shaderBrush = ShaderBrush(shader)
    val blockX = remember { mutableStateOf(1f) }
    val blockY = remember { mutableStateOf(1f) }
    val centerX = remember { mutableStateOf(originalWidth / 2) }
    val centerY = remember { mutableStateOf(originalHeight / 2) }
    val radius = remember { mutableStateOf(0f) }
    val spiralStrength = remember { mutableStateOf(0f) }

    Surface(
        modifier = Modifier
            .width(width = width.dp)
            .height(height = height.dp + 512.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = height.dp)
        ) {
            Canvas(
                modifier = Modifier
                    .width(width.dp)
                    .height(height.dp)
            ) {
                shader.setFloatUniform("iResolution", blockX.value, blockY.value)
                shader.setFloatUniform("iRadius", radius.value)
                shader.setFloatUniform("iCenter", centerX.value, centerY.value)
                shader.setFloatUniform("iSpiralStrength", spiralStrength.value)
                drawRect(shaderBrush)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "blockX: ${blockX.value}")
            Slider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .padding(start = 32.dp, end = 32.dp),
                value = blockX.value,
                valueRange = 1f..10f,
                onValueChange = {
                    blockX.value = it
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "blockY: ${blockY.value}")
            Slider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .padding(start = 32.dp, end = 32.dp),
                value = blockY.value,
                valueRange = 1f..10f,
                onValueChange = {
                    blockY.value = it
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "centerX: ${centerX.value}")
            Slider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .padding(start = 32.dp, end = 32.dp),
                value = centerX.value,
                valueRange = 0f..originalWidth,
                onValueChange = {
                    centerX.value = it
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "centerY: ${centerY.value}")
            Slider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .padding(start = 32.dp, end = 32.dp),
                value = centerY.value,
                valueRange = 0f..originalHeight,
                onValueChange = {
                    centerY.value = it
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "radius: ${radius.value}")
            Slider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .padding(start = 32.dp, end = 32.dp),
                value = radius.value,
                valueRange = 0f..originalHeight / 2,
                onValueChange = {
                    radius.value = it
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "spiralStrength: ${spiralStrength.value}")
            Slider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .padding(start = 32.dp, end = 32.dp),
                value = spiralStrength.value,
                valueRange = 0f..40f,
                onValueChange = {
                    spiralStrength.value = it
                }
            )
        }
    }
}

@Preview
@Composable
fun PreviewSpiralShaderScreen() {
    SpiralShader(RuntimeShader(""), 0f, 0f, 0f, 0f)
}
