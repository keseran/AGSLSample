package com.example.agslsample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.compose.FrostedGlassShaderScreen
import com.example.compose.GenbaNekoAnimationShaderScreen
import com.example.compose.GenbaNekoShaderScreen
import com.example.compose.ImageShaderScreen
import com.example.compose.LatticeShaderScreen
import com.example.compose.MainScreen
import com.example.compose.MonochromeShaderScreen
import com.example.compose.MosaicShaderScreen
import com.example.compose.NegaPosiShaderScreen
import com.example.compose.OutlineShaderScreen
import com.example.compose.SepiaShaderScreen
import com.example.compose.SpiralShaderScreen
import com.example.compose.StepShaderScreen
import com.example.compose.SwitchImageByAlphaAnimationShaderScreen
import com.example.compose.SwitchImageByPixelAnimationShaderScreen
import com.example.compose.SwitchImageByRatioShaderScreen
import com.example.compose.ui.theme.AgslSampleTheme

class MainActivity : ComponentActivity() {

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AgslSampleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    LazyColumn {
                        item {
                            MainScreen()
                        }
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        item {
                            ImageShaderScreen()
                        }
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        item {
                            OutlineShaderScreen()
                        }
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        item {
                            LatticeShaderScreen()
                        }
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        item {
                            StepShaderScreen()
                        }
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        item {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                GenbaNekoShaderScreen()
                                GenbaNekoAnimationShaderScreen()
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        item {
                            SwitchImageByAlphaAnimationShaderScreen()
                        }
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        item {
                            SwitchImageByPixelAnimationShaderScreen()
                        }
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        item {
                            SwitchImageByRatioShaderScreen()
                        }
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        item {
                            MonochromeShaderScreen()
                        }
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        item {
                            NegaPosiShaderScreen()
                        }
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        item {
                            SepiaShaderScreen()
                        }
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        item {
                            MosaicShaderScreen()
                        }
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        item {
                            FrostedGlassShaderScreen()
                        }
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        item {
                            SpiralShaderScreen()
                        }
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}
