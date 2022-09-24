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
    const float R_LUMINANCE = 0.298912;
    const float G_LUMINANCE = 0.586611;
    const float B_LUMINANCE = 0.114478;
"""
private const val SHADER_MAIN = """
    half4 main(in float2 fragCoord) {
        float4 image = iShader.eval(fragCoord).rgba;
        float v = image.x * R_LUMINANCE + image.y * G_LUMINANCE + image.z * B_LUMINANCE;
        image.x = v * 0.9;
        image.y = v * 0.7;
        image.z = v * 0.4;
        return half4(image);
    }
"""

@Composable
fun SepiaShaderScreen() {
    android.util.Log.d("--tag--", "SepiaShaderScreen")
    val res = LocalContext.current.resources

    val bitmap = BitmapFactory.decodeResource(res, R.drawable.sakura01)
    val shader = RuntimeShader(SHADER_UNIFORMS + SHADER_MAIN)
    val bitmapWidth = bitmap.width.toFloat()
    val bitmapHeight = bitmap.height.toFloat()

    val bitmapShader = BitmapShader(bitmap, Shader.TileMode.DECAL, Shader.TileMode.DECAL)
    shader.setInputBuffer("iShader", bitmapShader)

    SepiaShader(
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
private fun SepiaShader(
    shader: RuntimeShader,
    width: Float,
    height: Float,
) {
    android.util.Log.d("--tag--", "SepiaShader")
    val shaderBrush = ShaderBrush(shader)
    Canvas(modifier = Modifier
        .width(width.dp)
        .height(height.dp)
    ) {
        drawRect(shaderBrush)
    }
}

@Preview
@Composable
fun PreviewSepiaShaderScreen() {
    SepiaShader(RuntimeShader(""), 0f, 0f)
}
