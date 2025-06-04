package com.sundayting.wancmp.screen.jigsaw

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.input.pointer.util.addPointerInputChange
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toIntSize
import androidx.compose.ui.util.fastAll
import androidx.compose.ui.util.fastForEach
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import org.jetbrains.compose.resources.imageResource
import wancmp.composeapp.generated.resources.Res
import wancmp.composeapp.generated.resources.jigsaw_dog_example_1
import kotlin.math.absoluteValue

/**
 * 拼图游戏
 */
class JigsawScreen : Screen {

    @Composable
    override fun Content() {

        val screenModel = rememberScreenModel { JigsawScreenModel() }

        val state = screenModel.state

        Box {
            val imageBitmap = imageResource(Res.drawable.jigsaw_dog_example_1)
            Image(
                painter = remember(imageBitmap) { BitmapPainter(imageBitmap) },
                contentDescription = null,
                modifier = Modifier.fillMaxSize().scale(1.5f)
                    .drawWithCache {
                        val maskColor = Color.Black.copy(0.4f)
                        onDrawWithContent {
                            drawContent()
                            drawRect(maskColor)
                        }
                    },
                contentScale = ContentScale.Crop
            )
            BoxWithConstraints(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 30.dp, vertical = 50.dp)
            ) {
                val eachItemWidth = remember(maxWidth, state.gameSize) {
                    maxWidth / state.gameSize.width
                }
                val eachItemHeight = remember(maxHeight, state.gameSize) {
                    maxHeight / state.gameSize.height
                }
                state.jigsawList.fastForEach { bean ->
                    key(bean.index) {
                        val velocityTracker = remember { VelocityTracker() }
                        JigsawItem(
                            modifier = Modifier
                                .size(eachItemWidth, eachItemHeight)
                                .graphicsLayer {
                                    translationX = eachItemWidth.toPx() * bean.offset.x
                                    translationY = eachItemHeight.toPx() * bean.offset.y
                                }
                                .pointerInput(Unit) {
                                    detectDragGestures(
                                        onDragStart = {
                                            velocityTracker.resetTracking()
                                        },
                                        onDragEnd = {
                                            val velocity = velocityTracker.calculateVelocity()
                                            val velocityXDp =
                                                velocity.x.absoluteValue.toDp()
                                                    .takeIf { it > 300.dp }
                                                    ?: 0.dp
                                            val velocityYDp =
                                                velocity.y.absoluteValue.toDp()
                                                    .takeIf { it > 400.dp }
                                                    ?: 0.dp

                                            if (velocityYDp != 0.dp || velocityXDp != 0.dp) {
                                                if (velocityYDp > velocityXDp) {
                                                    //垂直移动
                                                    if (velocity.y < 0 && bean.upTargetOffset()
                                                            .isOffsetValidInJigsaw(state)
                                                    ) {
                                                        bean.offset = bean.upTargetOffset()
                                                    } else if (bean.downTargetOffset()
                                                            .isOffsetValidInJigsaw(state)
                                                    ) {
                                                        bean.offset = bean.downTargetOffset()
                                                    }
                                                } else {
                                                    //水平移动
                                                    if (velocity.x > 0 && bean.rightTargetOffset()
                                                            .isOffsetValidInJigsaw(state)
                                                    ) {
                                                        bean.offset = bean.rightTargetOffset()
                                                    } else if (bean.leftTargetOffset()
                                                            .isOffsetValidInJigsaw(state)
                                                    ) {
                                                        bean.offset = bean.leftTargetOffset()
                                                    }

                                                }
                                            }
                                        },
                                        onDragCancel = {
                                            velocityTracker.resetTracking()
                                        },
                                        onDrag = { change, dragAmount ->
                                            change.consume()
                                            velocityTracker.addPointerInputChange(change)
                                        }
                                    )
                                },
                            imageBitmap = imageBitmap,
                            index = bean.index,
                            jigsawSize = state.gameSize
                        )
                    }
                }

            }

        }


    }

    /**
     * 位移在拼图中是否可用
     */
    private fun IntOffset.isOffsetValidInJigsaw(state: JigSawScreenState): Boolean {
        return this.x >= 0 && this.x < state.gameSize.width && this.y >= 0 && this.y < state.gameSize.height && state.jigsawList.fastAll { it.offset != this }
    }

    companion object {

        class JigSawScreenState(
            val gameSize: IntSize
        ) {

            val jigsawList = mutableStateListOf<JigsawBean>()

        }

        class JigsawBean(
            val index: Int,
            offset: IntOffset
        ) {
            var offset by mutableStateOf(offset)
        }

        fun JigsawBean.upTargetOffset(): IntOffset {
            return offset + IntOffset(0, -1)
        }

        fun JigsawBean.downTargetOffset(): IntOffset {
            return offset + IntOffset(0, 1)
        }

        fun JigsawBean.leftTargetOffset(): IntOffset {
            return offset + IntOffset(-1, 0)
        }

        fun JigsawBean.rightTargetOffset(): IntOffset {
            return offset + IntOffset(1, 0)
        }

        @Composable
        private fun JigsawItem(
            modifier: Modifier = Modifier,
            imageBitmap: ImageBitmap,
            index: Int,
            jigsawSize: IntSize
        ) {
            Canvas(
                modifier.fillMaxSize().border(1.dp, Color.White),
            ) {
                val itemWidth = imageBitmap.width / jigsawSize.width
                val itemHeight = imageBitmap.height / jigsawSize.height
                val row = index / jigsawSize.width
                val col = index % jigsawSize.height
                val srcOffsetX = col * itemWidth
                val srcOffsetY = row * itemHeight
                drawImage(
                    image = imageBitmap,
                    srcOffset = IntOffset(srcOffsetX, srcOffsetY),
                    srcSize = IntSize(itemWidth, itemHeight),
                    dstSize = size.toIntSize()
                )
            }
        }

    }
}