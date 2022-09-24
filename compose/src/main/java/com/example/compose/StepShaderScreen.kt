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
        if (fragCoord.x > 1000.0) {
            fragCoord.y += 200.0;
        } else if (fragCoord.x > 900.0) {
            fragCoord.y += 180.0;
        } else if (fragCoord.x > 800.0) {
            fragCoord.y += 160.0;
        } else if (fragCoord.x > 700.0) {
            fragCoord.y += 140.0;
        } else if (fragCoord.x > 600.0) {
            fragCoord.y += 120.0;
        } else if (fragCoord.x > 500.0) {
            fragCoord.y += 100.0;
        } else if (fragCoord.x > 400.0) {
            fragCoord.y += 80.0;
        } else if (fragCoord.x > 300.0) {
            fragCoord.y += 60.0;
        } else if (fragCoord.x > 200.0) {
            fragCoord.y += 40.0;
        } else if (fragCoord.x > 100.0) {
            fragCoord.y += 20.0;
        } 
        float4 image = iShader.eval(fragCoord).rgba;
        return half4(image.xyz, 1.0);
    }
"""

@Composable
fun StepShaderScreen() {
    val context = LocalContext.current
    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.image)
    val bitmapWidth = bitmap.width.toFloat()
    val bitmapHeight = bitmap.height.toFloat()
    val bitmapShader = BitmapShader(bitmap, Shader.TileMode.DECAL, Shader.TileMode.DECAL)
    val shader = RuntimeShader(SHADER_UNIFORMS + SHADER_MAIN)
    shader.setInputBuffer("iShader", bitmapShader)

    StepShader(
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
private fun StepShader(
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
fun PreviewStepShaderScreen() {
    StepShader(RuntimeShader(""), 0f, 0f)
}
