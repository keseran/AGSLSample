package com.example.agslsample.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RuntimeShader
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.example.agslsample.R

/**
 * 格子状の線を表示するView
 */
class LatticeShaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
): View(context, attrs, defStyleAttr, defStyleRes) {

    companion object {
        /* 画像のサイズは1008 x 756 */
        private const val DURATION = 4000f
        private const val SHADER_UNIFORMS = """
            uniform shader iShader;
            uniform float2 iResolution;
            uniform float iTime;
            uniform float iDuration;
        """
        private const val SHADER_MAIN = """
            half4 main(in float2 fragCoord) {
                float4 image = iShader.eval(fragCoord).rgba;
                int resultX = (int)(fragCoord.x / 20);
                if ((int)(fragCoord.x) - (resultX * 20) < 4) {
                    image.b = 1.0;
                    image.rga = float3(0.0);
                }
                int resultY = (int)(fragCoord.y / 20);
                if ((int)(fragCoord.y) - (resultY * 20) < 4) {
                    image.g = 1.0;
                    image.rba = float3(0.0);
                }
                return half4(image.xyz, 1.0);
            }
        """
    }
    private val runtimeShader = RuntimeShader(SHADER_UNIFORMS + SHADER_MAIN)
    private var paint: Paint
    private var bitmapWidth = 0f
    private var bitmapHeight = 0f

    // declare the ValueAnimator
    private val shaderAnimator = ValueAnimator.ofFloat(0f, DURATION)

    init {

        // use it to animate the time uniform
        shaderAnimator.duration = DURATION.toLong()
        shaderAnimator.repeatCount = ValueAnimator.INFINITE
        shaderAnimator.repeatMode = ValueAnimator.REVERSE
        shaderAnimator.interpolator = LinearInterpolator()

        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.image)
        bitmapWidth = bitmap.width.toFloat()
        bitmapHeight = bitmap.height.toFloat()
        val bitmapShader = BitmapShader(bitmap, Shader.TileMode.DECAL, Shader.TileMode.DECAL)

        runtimeShader.setInputBuffer("iShader", bitmapShader)
        runtimeShader.setFloatUniform("iDuration", DURATION )
//        setRenderEffect(RenderEffect.createBlurEffect(30f, 30f, Shader.TileMode.DECAL))
        shaderAnimator.addUpdateListener { animation ->
            runtimeShader.setFloatUniform("iTime", animation.animatedValue as Float )
            invalidate()

            if (animation.animatedValue as Float > 1000f) {
                shaderAnimator.cancel()
            }
        }
        paint = Paint().apply {
            shader = runtimeShader
        }

//        shaderAnimator.start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            resolveSizeAndState(bitmapWidth.toInt(), widthMeasureSpec, 0),
            resolveSizeAndState(bitmapHeight.toInt(), heightMeasureSpec, 0)
        )
    }

    override fun onDrawForeground(canvas: Canvas?) {
        canvas?.let {
            runtimeShader.setFloatUniform("iResolution", bitmapWidth, bitmapHeight)
            canvas.drawPaint(paint)
        }
    }
}
