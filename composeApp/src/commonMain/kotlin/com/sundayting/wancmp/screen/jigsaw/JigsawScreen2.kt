package com.sundayting.wancmp.screen.jigsaw

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.withSaveLayer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import org.jetbrains.compose.resources.imageResource
import wancmp.composeapp.generated.resources.Res
import wancmp.composeapp.generated.resources.jigsaw_dog_example_2

class JigsawScreen2 : Screen {

    private val defaultLayerPaint = Paint()

    @Composable
    override fun Content() {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            val imageBitmap = imageResource(Res.drawable.jigsaw_dog_example_2)

            Canvas(
                Modifier
                    .fillMaxSize()
            ) {
                val canvasWidth = size.width
                val canvasHeight = size.height

                val imageWidth = imageBitmap.width.toFloat()
                val imageHeight = imageBitmap.height.toFloat()

                // 1. 计算缩放比例以适配 Canvas 高度
                val scale = canvasHeight / imageHeight

                // 2. 计算缩放后的图片尺寸
                val scaledImageWidth = imageWidth * scale
                val scaledImageHeight = imageHeight * scale // 这将等于 canvasHeight

                // 3. 计算目标绘制参数 (dst)
                // 图片在 Canvas 中的目标尺寸
                val dstSize = IntSize(
                    width = scaledImageWidth.toInt(),
                    height = scaledImageHeight.toInt() // 等于 canvasHeight.toInt()
                )

                // 图片在 Canvas 中的目标左上角偏移
                // 这里我们让图片在水平方向上居中
                val dstOffset = IntOffset(
                    x = ((canvasWidth - scaledImageWidth) / 2f).toInt(),
                    y = 0 // 从顶部开始
                )

                // 4. 源绘制参数 (src) - 我们要绘制整个原始图片
                val srcOffset = IntOffset.Zero // 从原始图片的 (0,0) 开始
                val srcSize = IntSize(imageBitmap.width, imageBitmap.height) // 原始图片的完整尺寸

                drawImage(
                    image = imageBitmap,
                    srcOffset = srcOffset,
                    srcSize = srcSize,
                    dstOffset = dstOffset,
                    dstSize = dstSize,
                    filterQuality = FilterQuality.Medium,
                    blendMode = BlendMode.SrcIn
                )


                rotate(degrees = 30f) {
                    drawIntoCanvas { canvas ->
                        canvas.withSaveLayer(
                            size.toRect(),
                            defaultLayerPaint
                        ) {
                            drawCircle(
                                Color.White,
                                radius = size.maxDimension / 2f,
                            )
                            drawImage(
                                image = imageBitmap,
                                srcOffset = srcOffset,
                                srcSize = srcSize,
                                dstOffset = dstOffset,
                                dstSize = dstSize,
                                filterQuality = FilterQuality.Medium,
                                blendMode = BlendMode.SrcIn
                            )
                        }
                    }
                }

                rotate(degrees = -50f) {
                    drawIntoCanvas { canvas ->
                        canvas.withSaveLayer(
                            size.toRect(),
                            defaultLayerPaint
                        ) {
                            drawCircle(
                                Color.White,
                                radius = size.maxDimension / 2f - 80.dp.toPx(),
                            )
                            drawImage(
                                image = imageBitmap,
                                srcOffset = srcOffset,
                                srcSize = srcSize,
                                dstOffset = dstOffset,
                                dstSize = dstSize,
                                filterQuality = FilterQuality.Medium,
                                blendMode = BlendMode.SrcIn
                            )
                        }
                    }
                }

                rotate(degrees = 180f) {
                    drawIntoCanvas { canvas ->
                        canvas.withSaveLayer(
                            size.toRect(),
                            defaultLayerPaint
                        ) {
                            drawCircle(
                                Color.White,
                                radius = size.maxDimension / 2f - 80.dp.toPx() * 2,
                            )
                            drawImage(
                                image = imageBitmap,
                                srcOffset = srcOffset,
                                srcSize = srcSize,
                                dstOffset = dstOffset,
                                dstSize = dstSize,
                                filterQuality = FilterQuality.Medium,
                                blendMode = BlendMode.SrcIn
                            )
                        }
                    }
                }

                drawIntoCanvas { canvas ->
                    canvas.withSaveLayer(
                        size.toRect(),
                        defaultLayerPaint
                    ) {
                        drawCircle(
                            Color.White,
                            radius = size.maxDimension / 2f - 80.dp.toPx() * 3,
                        )
                        drawImage(
                            image = imageBitmap,
                            srcOffset = srcOffset,
                            srcSize = srcSize,
                            dstOffset = dstOffset,
                            dstSize = dstSize,
                            filterQuality = FilterQuality.Medium,
                            blendMode = BlendMode.SrcIn
                        )
                    }
                }

                repeat(4) {
                    drawCircle(
                        color = Color.White,
                        radius = size.maxDimension / 2 - (80.dp * it).toPx(),
                        style = Stroke(
                            width = 2.dp.toPx()
                        )
                    )
                }


            }
        }

    }
}