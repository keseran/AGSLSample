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
    uniform float iMosaicScale;
"""
private const val SHADER_MAIN = """
    half4 main(in float2 fragCoord) {
        float2 uv = fragCoord;
        uv.x = floor(uv.x * iResolution.x / iMosaicScale) / (iResolution.x / iMosaicScale) + (iMosaicScale / 2.0) / iResolution.x;
        uv.y = floor(uv.y * iResolution.y / iMosaicScale) / (iResolution.y / iMosaicScale) + (iMosaicScale / 2.0) / iResolution.y;

        float4 image = iShader.eval(uv).rgba;
        return half4(image);
    }
"""

@Composable
fun MosaicShaderScreen() {
    android.util.Log.d("--tag--", "MosaicShaderScreen")
    val res = LocalContext.current.resources

    val bitmap = BitmapFactory.decodeResource(res, R.drawable.sakura01)
    val shader = RuntimeShader(SHADER_UNIFORMS + SHADER_MAIN)
    val bitmapWidth = bitmap.width.toFloat()
    val bitmapHeight = bitmap.height.toFloat()

    val bitmapShader = BitmapShader(bitmap, Shader.TileMode.DECAL, Shader.TileMode.DECAL)
    shader.setInputBuffer("iShader", bitmapShader)
    shader.setFloatUniform("iResolution", 1f, 1f)
    shader.setFloatUniform("iMosaicScale", 1f)

    MosaicShader(
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

@Composable
private fun MosaicShader(
    shader: RuntimeShader,
    width: Float,
    height: Float,
) {
    android.util.Log.d("--tag--", "MosaicShader")
    val shaderBrush = ShaderBrush(shader)
    val blockX = remember { mutableStateOf(1f) }
    val blockY = remember { mutableStateOf(1f) }
    val mosaicScale = remember { mutableStateOf(1f) }

    Surface(
        modifier = Modifier
            .width(width = width.dp)
            .height(height = height.dp + 256.dp)
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
                shader.setFloatUniform("iMosaicScale", mosaicScale.value)
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
                valueRange = 1f..100f,
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
                valueRange = 1f..100f,
                onValueChange = {
                    blockY.value = it
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "mosaicScale: ${mosaicScale.value}")
            Slider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .padding(start = 32.dp, end = 32.dp),
                value = mosaicScale.value,
                valueRange = 1f..100f,
                onValueChange = {
                    mosaicScale.value = it
                }
            )
        }
    }
}

@Preview
@Composable
fun PreviewMosaicShaderScreen() {
    MosaicShader(RuntimeShader(""), 0f, 0f)
}
