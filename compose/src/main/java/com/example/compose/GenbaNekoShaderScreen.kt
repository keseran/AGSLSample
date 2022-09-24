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
    uniform float iDuration;
    uniform shader iLeftShader;
    uniform shader iRightShader;
"""
private const val SHADER_MAIN = """
    half4 main(in float2 fragCoord) {
        float4 image = iLeftShader.eval(fragCoord).rgba;
        return half4(image.xyz, 1.0);
    }
"""

@Composable
fun GenbaNekoShaderScreen() {
    val res = LocalContext.current.resources

    val leftBitmap = BitmapFactory.decodeResource(res, R.drawable.yoshi_left)
    val rightBitmap = BitmapFactory.decodeResource(res, R.drawable.yoshi_right)
    val shader = RuntimeShader(SHADER_UNIFORMS + SHADER_MAIN)
    val bitmapWidth = leftBitmap.width.toFloat()
    val bitmapHeight = leftBitmap.height.toFloat()

    val leftBitmapShader = BitmapShader(leftBitmap, Shader.TileMode.DECAL, Shader.TileMode.DECAL)
    shader.setInputBuffer("iLeftShader", leftBitmapShader)

    val rightBitmapShader = BitmapShader(rightBitmap, Shader.TileMode.DECAL, Shader.TileMode.DECAL)
    shader.setInputBuffer("iRightShader", rightBitmapShader)

    GenbaNekoShader(
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
private fun GenbaNekoShader(
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
fun PreviewGenbaNekoShaderScreen() {
    GenbaNekoShader(RuntimeShader(""), 0f, 0f)
}
