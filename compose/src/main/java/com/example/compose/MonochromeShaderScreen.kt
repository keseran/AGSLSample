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
    const float3 monochromeScale = float3(R_LUMINANCE, G_LUMINANCE, B_LUMINANCE);
"""
private const val SHADER_MAIN = """
    half4 main(in float2 fragCoord) {
        float4 image = iShader.eval(fragCoord).rgba;
        float grayColor = dot(image.rgb, monochromeScale);
        return half4(float3(grayColor), 1.0);
    }
"""

@Composable
fun MonochromeShaderScreen() {
    android.util.Log.d("--tag--", "MonochromeShaderScreen")
    val res = LocalContext.current.resources

    val bitmap = BitmapFactory.decodeResource(res, R.drawable.sakura01)
    val shader = RuntimeShader(SHADER_UNIFORMS + SHADER_MAIN)
    val bitmapWidth = bitmap.width.toFloat()
    val bitmapHeight = bitmap.height.toFloat()

    val bitmapShader = BitmapShader(bitmap, Shader.TileMode.DECAL, Shader.TileMode.DECAL)
    shader.setInputBuffer("iShader", bitmapShader)

    MonochromeShader(
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
private fun MonochromeShader(
    shader: RuntimeShader,
    width: Float,
    height: Float,
) {
    android.util.Log.d("--tag--", "MonochromeShader")
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
fun PreviewMonochromeShaderScreen() {
    MonochromeShader(RuntimeShader(""), 0f, 0f)
}
