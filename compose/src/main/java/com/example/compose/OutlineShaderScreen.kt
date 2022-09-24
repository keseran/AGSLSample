package com.example.compose

import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.RuntimeShader
import android.graphics.Shader
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private const val SHADER_UNIFORMS = """
    uniform shader iShader;
"""
private const val SHADER_MAIN = """
    half4 main(in float2 fragCoord) {
        float4 image = iShader.eval(fragCoord).rgba;
        if (fragCoord.x < 10 || fragCoord.x > 997) {
            image.r = 1.0;
            image.gba = float3(0.0);
        }
        if (fragCoord.y < 10 || fragCoord.y > 745) {
            image.r = 1.0;
            image.gba = float3(0.0);
        }
        return half4(image.xyz, 1.0);
    }
"""

@Composable
fun OutlineShaderScreen() {
    val context = LocalContext.current
    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.image)
    val bitmapWidth = bitmap.width.toFloat()
    val bitmapHeight = bitmap.height.toFloat()
    val bitmapShader = BitmapShader(bitmap, Shader.TileMode.DECAL, Shader.TileMode.DECAL)
    val shader = RuntimeShader(SHADER_UNIFORMS + SHADER_MAIN)
    shader.setInputBuffer("iShader", bitmapShader)

    OutlineShader(
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
    )
}

@Composable
private fun OutlineShader(
    shader: RuntimeShader,
    width: Float,
    height: Float,
) {
    val shaderBrush = ShaderBrush(shader)
    Canvas(
        modifier = Modifier
            .width(width = width.dp)
            .height(height = height.dp)
    ) {
        drawRect(shaderBrush)
    }
}

@Preview
@Composable
fun PreviewOutlineShader() {
    OutlineShader(RuntimeShader(""), 0f, 0f)
}
